package by.mironenko.hotelTestApp.mapper;

import by.mironenko.hotelTestApp.dto.request.CreateHotelRequest;
import by.mironenko.hotelTestApp.dto.response.AddressDto;
import by.mironenko.hotelTestApp.dto.response.ArrivalTimeDto;
import by.mironenko.hotelTestApp.dto.response.ContactsDto;
import by.mironenko.hotelTestApp.dto.response.HotelDetailResponse;
import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import by.mironenko.hotelTestApp.model.Address;
import by.mironenko.hotelTestApp.model.Amenity;
import by.mironenko.hotelTestApp.model.ArrivalTime;
import by.mironenko.hotelTestApp.model.Contact;
import by.mironenko.hotelTestApp.model.Hotel;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

  public HotelShortResponse toShortResponse(final Hotel hotel) {
    if (hotel == null) {
      return null;
    }

    final String addressString = formatAddress(hotel.getAddress());
    final String phone = hotel.getContact() != null ? hotel.getContact().getPhone() : null;

    return new HotelShortResponse(
        hotel.getId(),
        hotel.getName(),
        hotel.getDescription(),
        addressString,
        phone
    );
  }

  public HotelDetailResponse toDetailResponse(final Hotel hotel) {
    if (hotel == null) {
      return null;
    }

    final AddressDto addressDto = toAddressDto(hotel.getAddress());
    final ContactsDto contactsDto = toContactsDto(hotel.getContact());
    final ArrivalTimeDto arrivalTimeDto = toArrivalTimeDto(hotel.getArrivalTime());

    final List<String> amenities = hotel.getAmenities() == null
        ? Collections.emptyList()
        : hotel.getAmenities().stream()
            .map(Amenity::getName)
            .sorted()
            .toList();

    return new HotelDetailResponse(
        hotel.getId(),
        hotel.getName(),
        hotel.getDescription(),
        hotel.getBrand(),
        addressDto,
        contactsDto,
        arrivalTimeDto,
        amenities
    );
  }

  public Hotel toEntity(final CreateHotelRequest request) {
    if (request == null) {
      return null;
    }

    final Address address = request.address() == null
        ? null
        : new Address(
            request.address().houseNumber(),
            request.address().street(),
            request.address().city(),
            request.address().country(),
            request.address().postCode()
        );

    final Contact contact = request.contacts() == null
        ? null
        : new Contact(
            request.contacts().phone(),
            request.contacts().email()
        );

    final ArrivalTime arrivalTime = request.arrivalTime() == null
        ? null
        : new ArrivalTime(
            request.arrivalTime().checkIn(),
            request.arrivalTime().checkOut()
        );

    return Hotel.builder()
        .name(request.name())
        .description(request.description())
        .brand(request.brand())
        .address(address)
        .contact(contact)
        .arrivalTime(arrivalTime)
        .build();
  }

  private AddressDto toAddressDto(final Address address) {
    if (address == null) {
      return null;
    }

    return new AddressDto(
        address.getCountry(),
        address.getCity(),
        address.getStreet(),
        address.getHouseNumber() == null ? null : String.valueOf(address.getHouseNumber()),
        address.getPostCode() == null ? null : address.getPostCode()
    );
  }

  private ContactsDto toContactsDto(final Contact contact) {
    if (contact == null) {
      return null;
    }
    return new ContactsDto(contact.getPhone(), contact.getEmail());
  }

  private ArrivalTimeDto toArrivalTimeDto(final ArrivalTime arrivalTime) {
    if (arrivalTime == null) {
      return null;
    }
    return new ArrivalTimeDto(arrivalTime.getCheckIn(), arrivalTime.getCheckOut());
  }

  private String formatAddress(final Address address) {
    if (address == null) {
      return null;
    }

    return String.format(
        "%s %s, %s, %s, %s",
        address.getHouseNumber() == null ? "" : address.getHouseNumber(),
        nullSafe(address.getStreet()),
        nullSafe(address.getCity()),
        nullSafe(address.getPostCode()),
        nullSafe(address.getCountry())
    ).replaceAll("\\s+,", ",").trim();
  }

  private String nullSafe(final String value) {
    return value == null ? "" : value;
  }
}
