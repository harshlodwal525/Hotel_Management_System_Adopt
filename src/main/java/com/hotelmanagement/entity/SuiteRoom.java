package com.hotelmanagement.entity;
import com.hotelmanagement.enums.RoomType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SUITE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class SuiteRoom extends Room {

    @Override
    public String getRoomDescription() {
        return "Luxury suite with separate living area and premium amenities";
    }

    @Override
    public int getCapacity() {
        return 4;
    }
}
