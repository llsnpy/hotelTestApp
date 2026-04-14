package by.mironenko.hotelTestApp.service;

import by.mironenko.hotelTestApp.dto.request.CreateHotelRequest;
import by.mironenko.hotelTestApp.dto.response.HotelDetailResponse;
import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import java.util.List;
import java.util.Map;

public interface HotelService {
  List<HotelShortResponse> getAllHotels();
  HotelDetailResponse getHotelById(final Long id);
  List<HotelShortResponse> search(
      final String name,
      final String brand,
      final String city,
      final String country,
      final List<String> amenities
  );
  HotelShortResponse createHotel(final CreateHotelRequest request);
  void addAmenities(final Long hotelId, final List<String> amenities);
  Map<String, Long> getHistogram(final String param);
}
