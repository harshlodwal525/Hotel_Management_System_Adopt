package com.hotelmanagement.repository;

import com.hotelmanagement.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocationIgnoreCase(String location);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Hotel> findByLocationContainingIgnoreCase(@Param("location") String location);

    List<Hotel> findByRatingGreaterThanEqual(Integer rating);
}