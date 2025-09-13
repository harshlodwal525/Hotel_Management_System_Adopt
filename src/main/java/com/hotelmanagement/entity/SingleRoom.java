package com.hotelmanagement.entity;

import com.hotelmanagement.enums.RoomType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SINGLE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class SingleRoom extends Room {

    @Override
    public String getRoomDescription() {
        return "Single room with one bed, suitable for individual travelers";
    }

    @Override
    public int getCapacity() {
        return 1;
    }
}