package by.mironenko.hotelTestApp.service.impl;

import by.mironenko.hotelTestApp.dto.request.CreateHotelRequest;
import by.mironenko.hotelTestApp.dto.response.HotelDetailResponse;
import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import by.mironenko.hotelTestApp.mapper.HotelMapper;
import by.mironenko.hotelTestApp.repository.amenity.AmenityRepository;
import by.mironenko.hotelTestApp.repository.hotel.HotelRepository;
import by.mironenko.hotelTestApp.service.HotelService;
import java.util.List;
import java.util.Map;
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
  public HotelDetailResponse getHotelById(Long id) {
    return null;
  }

  @Override
  public List<HotelShortResponse> search(String name, String brand, String city, String country,
      List<String> amenities) {
    return List.of();
  }

  @Override
  public HotelShortResponse createHotel(CreateHotelRequest request) {
    return null;
  }

  @Override
  public void addAmenities(Long hotelId, List<String> amenities) {

  }

  @Override
  public Map<String, Long> getHistogram(String param) {
    return Map.of();
  }
}
