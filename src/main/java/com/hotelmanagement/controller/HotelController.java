package com.hotelmanagement.controller;

import com.hotelmanagement.dto.request.HotelRequest;
import com.hotelmanagement.dto.request.RoomRequest;
import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.HotelResponse;
import com.hotelmanagement.dto.response.RoomResponse;
import com.hotelmanagement.service.HotelService;
import com.hotelmanagement.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Hotel Management", description = "Hotel and room management endpoints (Admin only)")
public class HotelController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new hotel", description = "Add a new hotel to the system")
    public ResponseEntity<ApiResponse> createHotel(@Valid @RequestBody HotelRequest request) {
        HotelResponse response = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Hotel created successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update hotel details", description = "Update existing hotel information")
    public ResponseEntity<ApiResponse> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelRequest request) {
        HotelResponse response = hotelService.updateHotel(id, request);
        return ResponseEntity.ok(ApiResponse.success("Hotel updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete hotel", description = "Delete a hotel from the system")
    public ResponseEntity<ApiResponse> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(ApiResponse.success("Hotel deleted successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get hotel by ID", description = "Retrieve hotel details by ID")
    public ResponseEntity<ApiResponse> getHotel(@PathVariable Long id) {
        HotelResponse response = hotelService.getHotel(id);
        return ResponseEntity.ok(ApiResponse.success("Hotel retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all hotels", description = "Retrieve all hotels in the system")
    public ResponseEntity<ApiResponse> getAllHotels() {
        List<HotelResponse> response = hotelService.getAllHotels();
        return ResponseEntity.ok(ApiResponse.success("Hotels retrieved successfully", response));
    }

    @PostMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add room to hotel", description = "Add a new room to an existing hotel")
    public ResponseEntity<ApiResponse> addRoom(@PathVariable Long hotelId, @Valid @RequestBody RoomRequest request) {
        RoomResponse response = roomService.createRoom(hotelId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Room added successfully", response));
    }

    @GetMapping("/{hotelId}/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get hotel rooms", description = "Get all rooms for a specific hotel")
    public ResponseEntity<ApiResponse> getHotelRooms(@PathVariable Long hotelId) {
        List<RoomResponse> response = roomService.getRoomsByHotel(hotelId);
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", response));
    }
}
