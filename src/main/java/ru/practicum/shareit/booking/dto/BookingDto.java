package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingDto {

    private LocalDateTime start;

    private LocalDateTime end;

    private Long item;

    private Long booker;

    private String status;

}
