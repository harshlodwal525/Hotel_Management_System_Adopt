package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.BookingRequest;
import com.hotelmanagement.dto.response.BookingResponse;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.Room;
import com.hotelmanagement.entity.User;
import com.hotelmanagement.enums.BookingStatus;
import com.hotelmanagement.exception.*;
import com.hotelmanagement.repository.BookingRepository;
import com.hotelmanagement.repository.RoomRepository;
import com.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final ReentrantLock bookingLock = new ReentrantLock();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingResponse createBooking(BookingRequest request) {
        validateBookingRequest(request);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));
        bookingLock.lock();
        try {
            if (bookingRepository.existsConflictingBooking(request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate())) {
                throw new RoomAlreadyBookedException("Room is already booked for the selected dates");
            }

            long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
            BigDecimal totalAmount = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

            boolean paymentSuccess = paymentService.processPayment("CARD", "dummy-card-details", totalAmount);
            if (!paymentSuccess) {
                throw new RuntimeException("Payment processing failed");
            }

            Booking booking = Booking.builder()
                    .user(user)
                    .room(room)
                    .checkInDate(request.getCheckInDate())
                    .checkOutDate(request.getCheckOutDate())
                    .status(BookingStatus.CONFIRMED)
                    .build();

            Booking savedBooking = bookingRepository.save(booking);
            log.info("Booking created successfully for user: {} and room: {}", username, room.getRoomNumber());

            return mapToResponse(savedBooking);

        } finally {
            bookingLock.unlock();
        }
    }

    private void validateBookingRequest(BookingRequest request) {
        if (request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date cannot be in the past");
        }

        if (request.getCheckInDate().isAfter(request.getCheckOutDate()) ||
                request.getCheckInDate().isEqual(request.getCheckOutDate())) {
            throw new InvalidBookingException("Check-out date must be after check-in date");
        }

        if (ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate()) > 365) {
            throw new InvalidBookingException("Booking period cannot exceed 365 days");
        }
    }

    public List<BookingResponse> getMyBookings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void cancelBooking(Long bookingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, user.getId())
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException("Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new InvalidBookingException("Cannot cancel a completed booking");
        }

        if (booking.getCheckInDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new InvalidBookingException("Cannot cancel booking less than 24 hours before check-in");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("Booking cancelled successfully: {}", bookingId);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .roomId(booking.getRoom().getId())
                .roomNumber(booking.getRoom().getRoomNumber())
                .hotelName(booking.getRoom().getHotel().getName())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
