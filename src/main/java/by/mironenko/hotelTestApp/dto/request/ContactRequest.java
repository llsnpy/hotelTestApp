package by.mironenko.hotelTestApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to create hotel contact information")
public record ContactRequest(
    @Schema(
        description = "Phone number in format +xxx xx xxx-xx-xx",
        example = "+375 29 170-75-85",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+\\d{3} \\d{2} \\d{3}-\\d{2}-\\d{2}$", message = "Isn't format +xxx xx xxx-xx-xx")
    String phone,

    @Schema(description = "Email", example = "dt.info@hilton.by", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email")
    String email
) { }
