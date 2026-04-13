package by.mironenko.hotelTestApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Detailed information about a hotel")
public record HotelDetailResponse(
    @Schema(description = "ID")
    Long id,
    @Schema(description = "Name")
    String name,
    @Schema(description = "Description")
    String description,
    @Schema(description = "Brand")
    String brand,
    @Schema(description = "Address")
    AddressDto address,
    @Schema(description = "Contacts")
    ContactsDto contacts,
    @Schema(description = "Arrival time info")
    ArrivalTimeDto arrivalTime,
    @Schema(description = "Amenities available at the hotel")
    List<String> amenities
) { }
