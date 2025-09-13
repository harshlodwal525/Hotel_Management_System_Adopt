package com.hotelmanagement.dto.response;

import com.hotelmanagement.enums.BookingStatus;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long roomId;
    private String roomNumber;
    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private LocalDateTime createdAt;
}