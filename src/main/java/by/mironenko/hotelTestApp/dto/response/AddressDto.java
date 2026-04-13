package by.mironenko.hotelTestApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Address information for a hotel")
public record AddressDto(
    @Schema(description = "Country")
    String country,
    @Schema(description = "City")
    String city,
    @Schema(description = "Street")
    String street,
    @Schema(description = "House number")
    String houseNumber,
    @Schema(description = "Post code")
    String postCode
) { }
