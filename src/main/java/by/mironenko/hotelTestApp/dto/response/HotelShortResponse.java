package by.mironenko.hotelTestApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Short information about hotel")
public record HotelShortResponse(
    @Schema(description = "ID")
    Long id,
    @Schema(description = "Name")
    String name,
    @Schema(description = "Description")
    String description,
    @Schema(description = "Address")
    String address,
    @Schema(description = "Contact phone number")
    String phone
) { }
