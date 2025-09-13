package com.hotelmanagement.repository;

import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findByRoomId(Long roomId);

    Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);

    @Query("""
        SELECT b FROM Booking b 
        WHERE b.room.id = :roomId 
        AND b.status IN ('CONFIRMED', 'CHECKED_IN') 
        AND NOT (b.checkOutDate <= :checkIn OR b.checkInDate >= :checkOut)
    """)
    List<Booking> findConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b 
        WHERE b.room.id = :roomId 
        AND b.status IN ('CONFIRMED', 'CHECKED_IN') 
        AND NOT (b.checkOutDate <= :checkIn OR b.checkInDate >= :checkOut)
    """)
    boolean existsConflictingBooking(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}