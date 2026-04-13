package by.mironenko.hotelTestApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to create hotel contact information")
public record ContactRequest(
    @Schema(description = "Phone number", example = "+375291707585")
    @NotBlank(message = "Phone number is required")
    String phone,
    @Schema(description = "Email", example = "reserv@mariot.by")
    @NotBlank(message = "Email is required")
    String email
) { }
