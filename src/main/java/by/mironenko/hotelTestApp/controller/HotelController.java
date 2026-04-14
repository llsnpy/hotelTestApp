package by.mironenko.hotelTestApp.controller;

import by.mironenko.hotelTestApp.dto.request.CreateHotelRequest;
import by.mironenko.hotelTestApp.dto.response.HotelDetailResponse;
import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import by.mironenko.hotelTestApp.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotel controller", description = "API for managing hotels")
public class HotelController {

  private final HotelService defaultHotelService;

  @GetMapping("/hotels")
  @Operation(summary = "Get all hotels", description = "Returns a list of all hotels (short info)")
  public List<HotelShortResponse> getAllHotels() {
    return defaultHotelService.getAllHotels();
  }

  @GetMapping("/hotels/{id}")
  @Operation(summary = "Get hotel details", description = "Return detail information about hotel")
  public HotelDetailResponse getHotelById(
      @Parameter(description = "Hotel ID", example = "1")
      @PathVariable final Long id) {
    return defaultHotelService.getHotelById(id);
  }

  @GetMapping("/search")
  @Operation(summary = "Search hotels", description = "Search hotels by params")
  public List<HotelShortResponse> searchHotel(
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final String brand,
      @RequestParam(required = false) final String city,
      @RequestParam(required = false) final String country,
      @RequestParam(required = false) final List<String> amenities) {
    return defaultHotelService.search(name, brand, city, country, amenities);
  }

  @GetMapping("/histogram/{param}")
  @Operation(summary = "Histogram", description = "Get count grouped by param")
  public Map<String, Long> getHistogram(@PathVariable final String param) {
    return defaultHotelService.getHistogram(param);
  }

  @PostMapping("/hotels")
  @Operation(summary = "Create hotel", description = "Create a new hotel")
  public ResponseEntity<HotelShortResponse> createHotel(
      @Valid @RequestBody final CreateHotelRequest  createHotelRequest) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(defaultHotelService.createHotel(createHotelRequest));
  }

  @PostMapping("/hotels/{id}/amenities")
  @Operation(summary = "Add amenities to hotel", description = "Add amenities to hotel by id")
  public ResponseEntity<Void> addAmenities(
      @PathVariable final Long id,
      @RequestBody final List<String> amenities) {
    defaultHotelService.addAmenities(id, amenities);
    return ResponseEntity.ok().build();
  }
}
