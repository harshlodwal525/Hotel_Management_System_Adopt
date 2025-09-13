package com.hotelmanagement.controller;

import com.hotelmanagement.dto.request.BookingRequest;
import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.BookingResponse;
import com.hotelmanagement.service.BookingService;
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
@RequestMapping("/bookings")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Booking Management", description = "Booking creation and management endpoints")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create a new booking", description = "Book a room with check-in and check-out dates")
    public ResponseEntity<ApiResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully", response));
    }

    @GetMapping("/mybooking")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get my bookings", description = "Fetch all bookings for the logged-in user")
    public ResponseEntity<ApiResponse> getMyBookings() {
        List<BookingResponse> response = bookingService.getMyBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Cancel booking", description = "Cancel a booking by ID")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully"));
    }
}