package by.mironenko.hotelTestApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Information about check-in and check-out times")
public record ArrivalTimeDto(
    @Schema(description = "Check-in time in HH:mm format")
    String checkIn,
    @Schema(description = "Check-out time in HH:mm format")
    String checkOut
) { }
