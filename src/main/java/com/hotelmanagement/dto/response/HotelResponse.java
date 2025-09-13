package com.hotelmanagement.dto.response;

import lombok.Data;
import lombok.Builder;

import java.util.List;

@Data
@Builder
public class HotelResponse {
    private Long id;
    private String name;
    private String location;
    private Integer rating;
    private List<RoomResponse> rooms;
}