package by.mironenko.hotelTestApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to create information about arrival time")
public record ArrivalTimeRequest(
    @Schema(description = "Check-in time", example = "14:00")
    @NotBlank(message = "Check-in time is required")
    String checkIn,
    @Schema(description = "Check-out time", example = "12:00")
    String checkOut
) { }
