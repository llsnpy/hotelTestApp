package by.mironenko.hotelTestApp.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.mironenko.hotelTestApp.exception.GlobalExceptionHandler;
import by.mironenko.hotelTestApp.exception.NotFoundException;
import by.mironenko.hotelTestApp.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class HotelControllerUpdateTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock
  private HotelService hotelService;

  @InjectMocks
  private HotelController hotelController;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders
        .standaloneSetup(hotelController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  void addAmenities_withInvalidHotelId_ShouldReturnNotFound() throws Exception {
    var invalidId = 999L;
    List<String> amenities = Arrays.asList("WiFi", "Pool");

    doThrow(new NotFoundException("Hotel not found with id: " + invalidId))
        .when(hotelService).addAmenities(invalidId, amenities);

    mockMvc.perform(post("/property-view/hotels/{id}/amenities", invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(amenities)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Hotel not found with id: 999"));

    verify(hotelService, times(1)).addAmenities(invalidId, amenities);
  }

  @Test
  void addAmenities_withEmptyList_ShouldReturnBadRequest() throws Exception {
    var hotelId = 1L;
    List<String> emptyAmenities = List.of();

    doThrow(new IllegalArgumentException("Amenities list cannot be empty"))
        .when(hotelService).addAmenities(hotelId, emptyAmenities);

    mockMvc.perform(post("/property-view/hotels/{id}/amenities", hotelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(emptyAmenities)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Amenities list cannot be empty"));

    verify(hotelService, times(1)).addAmenities(hotelId, emptyAmenities);
  }
}
