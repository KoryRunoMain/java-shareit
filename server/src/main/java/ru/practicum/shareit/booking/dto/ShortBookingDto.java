package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ShortBookingDto {

    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;

}
