package by.mironenko.hotelTestApp.service.impl;

import by.mironenko.hotelTestApp.dto.request.CreateHotelRequest;
import by.mironenko.hotelTestApp.dto.response.HotelDetailResponse;
import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import by.mironenko.hotelTestApp.exception.NotFoundException;
import by.mironenko.hotelTestApp.mapper.HotelMapper;
import by.mironenko.hotelTestApp.model.Address;
import by.mironenko.hotelTestApp.model.Amenity;
import by.mironenko.hotelTestApp.model.Hotel;
import by.mironenko.hotelTestApp.repository.amenity.AmenityRepository;
import by.mironenko.hotelTestApp.repository.hotel.HotelRepository;
import by.mironenko.hotelTestApp.service.HotelService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultHotelService implements HotelService {

  private final HotelRepository hotelRepository;
  private final AmenityRepository amenityRepository;
  private final HotelMapper hotelMapper;

  @Override
  @Transactional(readOnly = true)
  public List<HotelShortResponse> getAllHotels() {
    return hotelRepository.findAll().stream()
        .map(hotelMapper::toShortResponse)
        .toList();
  }

  @Override
  public HotelDetailResponse getHotelById(final Long id) {
    final Hotel hotel = hotelRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Hotel not found with id: " + id));
    return hotelMapper.toDetailResponse(hotel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<HotelShortResponse> search(
      final String name,
      final String brand,
      final String city,
      final String country,
      final List<String> amenities) {
    final Set<String> normalizedAmenities = amenities == null
        ? Set.of()
        : amenities.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toSet());

    return hotelRepository.findAll().stream()
        .filter(h -> isBlank(name) || containsIgnoreCase(h.getName(), name))
        .filter(h -> isBlank(brand) || equalsIgnoreCase(h.getBrand(), brand))
        .filter(h -> isBlank(city) || (h.getAddress() != null
            && equalsIgnoreCase(h.getAddress().getCity(), city)))
        .filter(h -> isBlank(country) || (h.getAddress() != null
            && equalsIgnoreCase(h.getAddress().getCountry(), country)))
        .filter(h -> normalizedAmenities.isEmpty() || hasAnyAmenity(h, normalizedAmenities))
        .map(hotelMapper::toShortResponse)
        .toList();
  }

  @Override
  public HotelShortResponse createHotel(final CreateHotelRequest request) {
    final Hotel hotel = hotelMapper.toEntity(request);
    final Hotel saved = hotelRepository.save(hotel);
    return hotelMapper.toShortResponse(saved);
  }

  @Override
  public void addAmenities(final Long hotelId, final List<String> amenities) {
    final Hotel hotel = hotelRepository.findById(hotelId)
        .orElseThrow(() -> new NotFoundException("Hotel not found with id: " + hotelId));

    if (amenities == null || amenities.isEmpty()) {
      return;
    }

    for (String amenityName : amenities) {
      if (amenityName == null || amenityName.isBlank()) {
        continue;
      }

      final String normalizedName = amenityName.trim();
      final Amenity amenity = amenityRepository.findByName(normalizedName)
          .orElseGet(() -> amenityRepository.save(Amenity.builder().name(normalizedName).build()));

      hotel.getAmenities().add(amenity);
    }

    hotelRepository.save(hotel);
  }

  @Override
  @Transactional(readOnly = true)
  public Map<String, Long> getHistogram(final String param) {
    final List<Hotel> hotels = hotelRepository.findAll();
    return switch (param == null ? "" : param.toLowerCase()) {
      case "brand" -> hotels.stream()
          .map(Hotel::getBrand)
          .filter(Objects::nonNull)
          .collect(Collectors.groupingBy(v -> v, LinkedHashMap::new, Collectors.counting()));

      case "city" -> hotels.stream()
          .map(Hotel::getAddress)
          .filter(Objects::nonNull)
          .map(Address::getCity)
          .filter(Objects::nonNull)
          .collect(Collectors.groupingBy(v -> v, LinkedHashMap::new, Collectors.counting()));

      case "country" -> hotels.stream()
          .map(Hotel::getAddress)
          .filter(Objects::nonNull)
          .map(Address::getCountry)
          .filter(Objects::nonNull)
          .collect(Collectors.groupingBy(v -> v, LinkedHashMap::new, Collectors.counting()));

      case "amenities" -> hotels.stream()
          .flatMap(h -> h.getAmenities().stream())
          .map(Amenity::getName)
          .filter(Objects::nonNull)
          .collect(Collectors.groupingBy(v -> v, LinkedHashMap::new, Collectors.counting()));

      default -> throw new IllegalArgumentException(
          String.format("Unsupported histogram param: %s. Allowed: brand, city, country, amenities", param)
      );
    };
  }

  private boolean hasAnyAmenity(final Hotel hotel, final Set<String> requestedAmenities) {
    if (requestedAmenities.isEmpty()) {
      return true;
    }
    if (hotel.getAmenities() == null || hotel.getAmenities().isEmpty()) {
      return false;
    }
    var amenities = hotel.getAmenities().stream()
        .map(Amenity::getName)
        .filter(Objects::nonNull)
        .map(String::toLowerCase)
        .collect(Collectors.toSet());
    return amenities.containsAll(requestedAmenities);
  }

  private boolean isBlank(final String value) {
    return value == null || value.isBlank();
  }

  private boolean equalsIgnoreCase(final String left, final String right) {
    return left != null && left.equalsIgnoreCase(right);
  }

  private boolean containsIgnoreCase(final String source, final String part) {
    return source != null && part != null
        && source.toLowerCase().contains(part.toLowerCase().trim());
  }
}
