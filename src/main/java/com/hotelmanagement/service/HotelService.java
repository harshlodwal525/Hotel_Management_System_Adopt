package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.HotelRequest;
import com.hotelmanagement.dto.response.HotelResponse;
import com.hotelmanagement.entity.Hotel;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelResponse createHotel(HotelRequest request) {
        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .location(request.getLocation())
                .rating(request.getRating())
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully: {}", savedHotel.getName());

        return mapToResponse(savedHotel);
    }

    public HotelResponse updateHotel(Long hotelId, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        hotel.setName(request.getName());
        hotel.setLocation(request.getLocation());
        hotel.setRating(request.getRating());

        Hotel updatedHotel = hotelRepository.save(hotel);
        log.info("Hotel updated successfully: {}", updatedHotel.getName());

        return mapToResponse(updatedHotel);
    }

    public void deleteHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        hotelRepository.delete(hotel);
        log.info("Hotel deleted successfully: {}", hotel.getName());
    }

    public HotelResponse getHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        return mapToResponse(hotel);
    }

    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<HotelResponse> getHotelsByLocation(String location) {
        return hotelRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private HotelResponse mapToResponse(Hotel hotel) {
        return HotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .location(hotel.getLocation())
                .rating(hotel.getRating())
                .build();
    }
}
