package com.hotelmanagement.dto.response;

import com.hotelmanagement.enums.RoomType;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private Long hotelId;
    private String roomNumber;
    private RoomType type;
    private BigDecimal pricePerNight;
    private Boolean availability;
    private String description;
    private Integer capacity;
}