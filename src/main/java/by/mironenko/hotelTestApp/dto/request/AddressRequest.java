package by.mironenko.hotelTestApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to create hotel address")
public record AddressRequest(
    @Schema(description = "House number", example = "123")
    @NotNull(message = "House number is required")
    Integer houseNumber,

    @Schema(description = "Street", example = "Nezavisosti", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "Street is required")
    @NotNull(message = "Street is required")
    String street,

    @Schema(description = "City", example = "Minsk", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "City is required")
    String city,

    @Schema(description = "Country", example = "Belarus", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "Country is required")
    String country,

    @Schema(description = "Postal code", example = "222842", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "Post code is required")
    String postCode
) { }
