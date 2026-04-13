package by.mironenko.hotelTestApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Request to create a new hotel")
public record CreateHotelRequest(
    @Schema(description = "Name", example = "Double Tree")
    @NotBlank(message = "Hotel name is required")
    String name,
    @Schema(description = "Description")
    @Length(max = 2000, message = "Description cannot exceed 2000 characters")
    String description,
    @Schema(description = "Brand", example = "Hilton")
    @NotBlank(message = "Hotel brand is required")
    @Length(max = 250, message = "Brand cannot exceed 250 characters")
    String brand,
    @Schema(description = "Address")
    @Valid
    @NotNull(message = "Address is required")
    AddressRequest address,
    @Schema(description = "Contact information")
    @Valid
    @NotNull(message = "Contacts are required")
    ContactRequest contacts,
    @Schema(description = "Information about arrival time")
    @Valid
    ArrivalTimeRequest arrivalTime
) { }
