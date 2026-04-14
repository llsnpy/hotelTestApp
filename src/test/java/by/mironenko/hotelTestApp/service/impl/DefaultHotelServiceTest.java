package by.mironenko.hotelTestApp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.mironenko.hotelTestApp.dto.request.AddressRequest;
import by.mironenko.hotelTestApp.dto.request.ArrivalTimeRequest;
import by.mironenko.hotelTestApp.dto.request.ContactRequest;
import by.mironenko.hotelTestApp.dto.request.CreateHotelRequest;
import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import by.mironenko.hotelTestApp.mapper.HotelMapper;
import by.mironenko.hotelTestApp.model.Amenity;
import by.mironenko.hotelTestApp.model.Hotel;
import by.mironenko.hotelTestApp.repository.amenity.AmenityRepository;
import by.mironenko.hotelTestApp.repository.hotel.HotelRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultHotelServiceTest {

  @Mock
  private HotelRepository hotelRepository;
  @Mock
  private AmenityRepository amenityRepository;
  @Mock
  private HotelMapper hotelMapper;

  @InjectMocks
  private DefaultHotelService hotelService;

  @Test
  void createHotel_success() {
    var request = createValidRequest();
    var hotelEntity = new Hotel();
    var savedHotel = new Hotel();
    savedHotel.setId(1L);
    var expectedResponse = new HotelShortResponse(
        1L,
        "Double Tree",
        "Description",
        "123 Nezavisosti, Minsk, 222842, Belarus",
        "+375 17 123-45-78"
    );

    when(hotelMapper.toEntity(request)).thenReturn(hotelEntity);
    when(hotelRepository.save(hotelEntity)).thenReturn(savedHotel);
    when(hotelMapper.toShortResponse(savedHotel)).thenReturn(expectedResponse);

    var result = hotelService.createHotel(request);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(1L);

    verify(hotelRepository, times(1)).save(hotelEntity);
  }

  @Test
  void addAmenities_success() {
    var hotelId = 1L;
    var amenities = List.of("WiFi", "Pool");

    var hotel = Hotel.builder()
        .id(hotelId)
        .name("Double Tree")
        .build();

    var wifi = Amenity.builder().id(1L).name("WiFi").build();
    var pool = Amenity.builder().id(2L).name("Pool").build();

    when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
    when(amenityRepository.findByName("WiFi")).thenReturn(Optional.of(wifi));
    when(amenityRepository.findByName("Pool")).thenReturn(Optional.empty());
    when(amenityRepository.save(any(Amenity.class))).thenReturn(pool);
    when(hotelRepository.save(hotel)).thenReturn(hotel);

    hotelService.addAmenities(hotelId, amenities);

    assertThat(hotel.getAmenities()).hasSize(2);
    verify(hotelRepository, times(1)).save(hotel);
  }

  private CreateHotelRequest createValidRequest() {
    return new CreateHotelRequest(
        "Double Tree",
        "Description",
        "Hilton",
        new AddressRequest(123, "Nezavisosti", "Minsk", "Belarus", "222842"),
        new ContactRequest("+375 17 123-45-78", "dt.info@hilton.by"),
        new ArrivalTimeRequest("14:00", "12:00")
    );
  }
}
