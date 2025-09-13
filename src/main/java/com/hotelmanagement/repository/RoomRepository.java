package com.hotelmanagement.repository;

import com.hotelmanagement.entity.Room;
import com.hotelmanagement.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);

    Optional<Room> findByHotelIdAndRoomNumber(Long hotelId, String roomNumber);

    List<Room> findByType(RoomType type);

    List<Room> findByAvailabilityTrue();

    @Query("""
        SELECT r FROM Room r 
        WHERE r.hotel.location = :location 
        AND r.availability = true 
        AND r.id NOT IN (
            SELECT b.room.id FROM Booking b 
            WHERE b.status IN ('CONFIRMED', 'CHECKED_IN') 
            AND NOT (b.checkOutDate <= :checkIn OR b.checkInDate >= :checkOut)
        )
    """)
    List<Room> findAvailableRooms(
            @Param("location") String location,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("""
        SELECT r FROM Room r 
        WHERE r.availability = true 
        AND r.id NOT IN (
            SELECT b.room.id FROM Booking b 
            WHERE b.status IN ('CONFIRMED', 'CHECKED_IN') 
            AND NOT (b.checkOutDate <= :checkIn OR b.checkInDate >= :checkOut)
        )
    """)
    List<Room> findAllAvailableRooms(
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}