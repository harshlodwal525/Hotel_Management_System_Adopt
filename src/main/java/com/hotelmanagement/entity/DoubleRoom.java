package com.hotelmanagement.entity;

import com.hotelmanagement.enums.RoomType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DOUBLE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class DoubleRoom extends Room {

    @Override
    public String getRoomDescription() {
        return "Double room with double bed, perfect for couples";
    }

    @Override
    public int getCapacity() {
        return 2;
    }
}

