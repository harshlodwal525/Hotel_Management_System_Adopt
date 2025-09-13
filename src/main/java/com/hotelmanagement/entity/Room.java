package com.hotelmanagement.entity;

import com.hotelmanagement.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "rooms")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "room_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @NotBlank(message = "Room number is required")
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RoomType type;

    @NotNull(message = "Price per night is required")
    @Positive(message = "Price per night must be positive")
    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Boolean availability = true;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public abstract String getRoomDescription();

    public abstract int getCapacity();
}
