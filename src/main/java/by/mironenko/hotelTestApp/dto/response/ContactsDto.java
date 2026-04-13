package by.mironenko.hotelTestApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contact information for a hotel")
public record ContactsDto(
    @Schema(description = "Contact phone number")
    String phone,
    @Schema(description = "Email")
    String email
) { }
