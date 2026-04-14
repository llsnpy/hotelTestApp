package by.mironenko.hotelTestApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to create information about arrival time")
public record ArrivalTimeRequest(
    @Schema(description = "Check-in time", example = "14:00", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "Check-in time is required")
    @Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]$", message = "Isn't format HH:mm")
    String checkIn,

    @Schema(description = "Check-out time", example = "12:00")
    @Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]$", message = "Isn't format HH:mm")
    String checkOut
) { }
