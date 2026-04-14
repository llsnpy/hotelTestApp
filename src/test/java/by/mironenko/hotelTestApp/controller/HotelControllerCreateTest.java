package by.mironenko.hotelTestApp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.mironenko.hotelTestApp.dto.request.AddressRequest;
import by.mironenko.hotelTestApp.dto.request.ArrivalTimeRequest;
import by.mironenko.hotelTestApp.dto.request.ContactRequest;
import by.mironenko.hotelTestApp.dto.request.CreateHotelRequest;
import by.mironenko.hotelTestApp.dto.response.HotelShortResponse;
import by.mironenko.hotelTestApp.exception.GlobalExceptionHandler;
import by.mironenko.hotelTestApp.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class HotelControllerCreateTest {

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
  void createHotel_withValidInput_shouldReturnCreatedHotel() throws Exception {
    var validRequest = createValidHotelRequest();
    var response = new HotelShortResponse(
        1L,
        "Double tree",
        "Description",
        "123 Nezavisosti, Minsk, 222842, Belarus",
        "+375 17 123-45-78"
    );

    when(hotelService.createHotel(any(CreateHotelRequest.class))).thenReturn(response);

    mockMvc.perform(post("/property-view/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Double tree"))
        .andExpect(jsonPath("$.description").value("Description"))
        .andExpect(jsonPath("$.address").value("123 Nezavisosti, Minsk, 222842, Belarus"))
        .andExpect(jsonPath("$.phone").value("+375 17 123-45-78"));

    verify(hotelService, times(1)).createHotel(any(CreateHotelRequest.class));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidHotelRequests")
  void createHotel_withInvalidInputs_shouldReturnValidationErrors(
      String testName,
      CreateHotelRequest invalid,
      String expectedField,
      String expectedMessage
  ) throws Exception {
    mockMvc.perform(post("/property-view/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(String.format("$.%s", expectedField)).value(expectedMessage));

    verify(hotelService, never()).createHotel(any());
  }

  private static Stream<Arguments> invalidHotelRequests() {
    var validAddress = createValidAddress();
    var validContacts = createValidContacts();
    var validArrivalTime = createValidArrivalTime();

    return Stream.of(
        Arguments.of(
            "Name is null",
            new CreateHotelRequest(null, "Description", "Hilton", validAddress, validContacts, validArrivalTime),
            "name",
            "Hotel name is required"
        ),
        Arguments.of(
            "Name is blank",
            new CreateHotelRequest("", "Description", "Hilton", validAddress, validContacts, validArrivalTime),
            "name",
            "Hotel name is required"
        ),
        Arguments.of(
            "Name is only spaces",
            new CreateHotelRequest("   ", "Description", "Hilton", validAddress, validContacts, validArrivalTime),
            "name",
            "Hotel name is required"
        ),
        Arguments.of(
            "Brand is null",
            new CreateHotelRequest("Double Tree", "Description", null, validAddress, validContacts, validArrivalTime),
            "brand",
            "Hotel brand is required"
        ),
        Arguments.of(
            "Brand is blank",
            new CreateHotelRequest("Double Tree", "Description", "", validAddress, validContacts, validArrivalTime),
            "brand",
            "Hotel brand is required"
        ),
        Arguments.of(
            "Brand exceeds max length (251 chars)",
            new CreateHotelRequest("Double Tree", "Description", "A".repeat(251), validAddress, validContacts,
                validArrivalTime),
            "brand",
            "Brand cannot exceed 250 characters"
        ),
        Arguments.of(
            "Description exceeds max length (2001 chars)",
            new CreateHotelRequest("Double Tree", "A".repeat(2001), "Hilton", validAddress, validContacts,
                validArrivalTime),
            "description",
            "Description cannot exceed 2000 characters"
        ),
        Arguments.of(
            "Address is null",
            new CreateHotelRequest("Double Tree", "Description", "Hilton", null, validContacts, validArrivalTime),
            "address",
            "Address is required"
        ),
        Arguments.of(
            "Contacts is null",
            new CreateHotelRequest("Double Tree", "Description", "Hilton", validAddress, null, validArrivalTime),
            "contacts",
            "Contacts are required"
        ),
        Arguments.of(
            "ArrivalTime is null",
            new CreateHotelRequest("Double Tree", "Description", "Hilton", validAddress, validContacts, null),
            "arrivalTime",
            "Arrival time is required"
        )
    );
  }

  @ParameterizedTest(name = "Phone: {0}")
  @MethodSource("invalidPhoneNumbers")
  void createHotel_withInvalidPhoneNumber_ShouldReturnValidationError(
      String phone,
      String expectedErrorMessage
  ) throws Exception {
    var validAddress = createValidAddress();
    var invalidContacts = new ContactRequest(phone, "test@mail.com");
    var validArrivalTime = createValidArrivalTime();

    var request = new CreateHotelRequest(
        "Double Tree", "Description", "Hilton",
        validAddress, invalidContacts, validArrivalTime
    );

    mockMvc.perform(post("/property-view/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['contacts.phone']").value(expectedErrorMessage));
  }

  private static Stream<Arguments> invalidPhoneNumbers() {
    return Stream.of(
        Arguments.of("375291707585", "Isn't format +xxx xx xxx-xx-xx"),
        Arguments.of("+375291707585", "Isn't format +xxx xx xxx-xx-xx"),
        Arguments.of("+375 29 1707585", "Isn't format +xxx xx xxx-xx-xx"),
        Arguments.of("+375 29 170-75-8", "Isn't format +xxx xx xxx-xx-xx"),
        Arguments.of("+37 29 170-75-85", "Isn't format +xxx xx xxx-xx-xx"),
        Arguments.of("+375 2 170-75-85", "Isn't format +xxx xx xxx-xx-xx")
    );
  }

  @ParameterizedTest(name = "Email: {0}")
  @MethodSource("invalidEmails")
  void createHotel_withInvalidEmail_ShouldReturnValidationError(
      String email,
      String expectedMessage
  ) throws Exception {
    var validAddress = createValidAddress();
    var invalidContacts = new ContactRequest("+375 29 170-75-85", email);
    var validArrivalTime = createValidArrivalTime();

    var request = new CreateHotelRequest(
        "Double Tree", "Description", "Hilton",
        validAddress, invalidContacts, validArrivalTime
    );

    mockMvc.perform(post("/property-view/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['contacts.email']").value(expectedMessage));
  }

  private static Stream<Arguments> invalidEmails() {
    return Stream.of(
        Arguments.of("test", "Invalid email"),
        Arguments.of("test@", "Invalid email"),
        Arguments.of("@mail.com", "Invalid email"),
        Arguments.of("test@mail", "Invalid email"),
        Arguments.of("test@mail.c", "Invalid email"),
        Arguments.of("test space@mail.com", "Invalid email")
    );
  }

  @ParameterizedTest(name = "checkIn: {0}, checkOut: {1}")
  @MethodSource("invalidArrivalTimes")
  void createHotel_withInvalidArrivalTime_ShouldReturnValidationError(
      String checkIn,
      String checkOut,
      String expectedField,
      String expectedMessage
  ) throws Exception {
    var validAddress = createValidAddress();
    var validContacts = createValidContacts();
    var invalidArrivalTime = new ArrivalTimeRequest(checkIn, checkOut);

    var request = new CreateHotelRequest(
        "Double Tree", "Description", "Hilton",
        validAddress, validContacts, invalidArrivalTime
    );

    mockMvc.perform(post("/property-view/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(String.format("$['arrivalTime.%s']", expectedField)).value(expectedMessage));
  }

  private static Stream<Arguments> invalidArrivalTimes() {
    return Stream.of(
        Arguments.of(null, null, "checkIn", "Check-in time is required"),
        Arguments.of("25:00", null, "checkIn", "Isn't format HH:mm"),
        Arguments.of("14:60", null, "checkIn", "Isn't format HH:mm"),
        Arguments.of("4:00", null, "checkIn", "Isn't format HH:mm"),
        Arguments.of("14:0", null, "checkIn", "Isn't format HH:mm"),
        Arguments.of("24:00", null, "checkIn", "Isn't format HH:mm"),
        Arguments.of("14:00", "25:00", "checkOut", "Isn't format HH:mm"),
        Arguments.of("14:00", "12:60", "checkOut", "Isn't format HH:mm"),
        Arguments.of("14:00", "2:00", "checkOut", "Isn't format HH:mm")
    );
  }

  private CreateHotelRequest createValidHotelRequest() {
    return new CreateHotelRequest(
        "Double Tree",
        "Nice hotel",
        "Hilton",
        createValidAddress(),
        createValidContacts(),
        createValidArrivalTime()
    );
  }

  private static AddressRequest createValidAddress() {
    return new AddressRequest(
        123,
        "Nezavisosti",
        "Minsk",
        "Belarus",
        "222842"
    );
  }

  private static ContactRequest createValidContacts() {
    return new ContactRequest(
        "+375 13 123-45-78",
        "dt.info@hilton.by"
    );
  }

  private static ArrivalTimeRequest createValidArrivalTime() {
    return new ArrivalTimeRequest("14:00", "12:00");
  }
}
