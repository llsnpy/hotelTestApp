package by.mironenko.hotelTestApp.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import by.mironenko.hotelTestApp.exception.GlobalExceptionHandler;
import by.mironenko.hotelTestApp.exception.NotFoundException;
import by.mironenko.hotelTestApp.service.HotelService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class HotelControllerGetTest {

  private MockMvc mockMvc;

  @Mock
  private HotelService hotelService;

  @InjectMocks
  private HotelController hotelController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(hotelController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  void getAllHotels_shouldReturnList() throws Exception {
    var hotels = Arrays.asList(
        new HotelShortResponse(
            1L,
            "Double tree",
            "Description 1",
            "123 Nezavisosti, Minsk, 222842, Belarus",
            "+375 17 123-45-78"
        ),
        new HotelShortResponse(
            2L,
            "Hotel Europe",
            "Description 2",
            "28 Internacionalnaya, Minsk, 222842, Belarus",
            "+375 17 123-45-77"
        )
    );

    when(hotelService.getAllHotels()).thenReturn(hotels);

    mockMvc.perform(get("/property-view/hotels"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));

    verify(hotelService, times(1)).getAllHotels();
  }

  @Test
  void getAllHotels_WhenNoHotels_ShouldReturnEmptyList() throws Exception {
    when(hotelService.getAllHotels()).thenReturn(List.of());

    mockMvc.perform(get("/property-view/hotels"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));

    verify(hotelService, times(1)).getAllHotels();
  }

  @Test
  void getHotelById_withInvalidId_ShouldReturnNotFound() throws Exception {
    var invalidId = 999L;
    when(hotelService.getHotelById(invalidId)).thenThrow(new NotFoundException("Hotel not found with id: " + invalidId));

    mockMvc.perform(get("/property-view/hotels/{id}", invalidId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Hotel not found with id: 999"));

    verify(hotelService, times(1)).getHotelById(invalidId);
  }

  @Test
  void getHistogram_withInvalidParam_ShouldReturnBadRequest() throws Exception {
    var invalidParam = "invalidParam";
    when(hotelService.getHistogram(invalidParam))
        .thenThrow(new IllegalArgumentException("Invalid histogram parameter: " + invalidParam + ". Allowed: brand, city, country"));

    mockMvc.perform(get("/property-view/histogram/{param}", invalidParam))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid histogram parameter: invalidParam. Allowed: brand, city, country"));

    verify(hotelService, times(1)).getHistogram(invalidParam);
  }
}
