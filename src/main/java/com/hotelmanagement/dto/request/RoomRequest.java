package com.hotelmanagement.dto.request;

import com.hotelmanagement.enums.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRequest {
    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType type;

    @NotNull(message = "Price per night is required")
    @Positive(message = "Price must be positive")
    private BigDecimal pricePerNight;
}