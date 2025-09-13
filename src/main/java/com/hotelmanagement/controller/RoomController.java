package com.hotelmanagement.controller;

import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.RoomResponse;
import com.hotelmanagement.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Room Search", description = "Room search and availability endpoints")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'RECEPTIONIST')")
    @Operation(summary = "Search available rooms",
            description = "Search for available rooms by location and date range")
    public ResponseEntity<ApiResponse> searchAvailableRooms(
            @Parameter(description = "Location to search for hotels")
            @RequestParam(required = false) String location,

            @Parameter(description = "Check-in date (dd-MM-yyyy)", required = true)
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate checkIn,

            @Parameter(description = "Check-out date (dd-MM-yyyy)", required = true)
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate checkOut) {

        List<RoomResponse> response = roomService.searchAvailableRooms(location, checkIn, checkOut);
        return ResponseEntity.ok(ApiResponse.success("Available rooms retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'RECEPTIONIST')")
    @Operation(summary = "Get room by ID", description = "Retrieve room details by ID")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable Long id) {
        RoomResponse response = roomService.getRoomById(id);
        return ResponseEntity.ok(ApiResponse.success("Room retrieved successfully", response));
    }
}