package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.RoomRequest;
import com.hotelmanagement.dto.response.RoomResponse;
import com.hotelmanagement.entity.*;
import com.hotelmanagement.enums.RoomType;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.HotelRepository;
import com.hotelmanagement.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomResponse createRoom(Long hotelId, RoomRequest request) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        if (roomRepository.findByHotelIdAndRoomNumber(hotelId, request.getRoomNumber()).isPresent()) {
            throw new RuntimeException("Room number already exists in this hotel");
        }

        Room room = createRoomByType(request.getType());
        room.setHotel(hotel);
        room.setRoomNumber(request.getRoomNumber());
        room.setType(request.getType());
        room.setPricePerNight(request.getPricePerNight());
        room.setAvailability(true);

        Room savedRoom = roomRepository.save(room);
        log.info("Room created successfully: {} in hotel {}", savedRoom.getRoomNumber(), hotel.getName());

        return mapToResponse(savedRoom);
    }

    private Room createRoomByType(RoomType roomType) {
        return switch (roomType) {
            case SINGLE -> SingleRoom.builder().build();
            case DOUBLE -> DoubleRoom.builder().build();
            case SUITE -> SuiteRoom.builder().build();
        };
    }

    public List<RoomResponse> searchAvailableRooms(String location, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) {
            throw new RuntimeException("Check-in date cannot be after check-out date");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new RuntimeException("Check-in date cannot be in the past");
        }

        List<Room> availableRooms;
        if (location != null && !location.trim().isEmpty()) {
            availableRooms = roomRepository.findAvailableRooms(location.trim(), checkIn, checkOut);
        } else {
            availableRooms = roomRepository.findAllAvailableRooms(checkIn, checkOut);
        }

        return availableRooms.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RoomResponse> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        return mapToResponse(room);
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .hotelId(room.getHotel().getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType())
                .pricePerNight(room.getPricePerNight())
                .availability(room.getAvailability())
                .description(room.getRoomDescription())
                .capacity(room.getCapacity())
                .build();
    }
}