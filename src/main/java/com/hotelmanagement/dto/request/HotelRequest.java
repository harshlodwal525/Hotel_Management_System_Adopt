package com.hotelmanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HotelRequest {
    @NotBlank(message = "Hotel name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;
}