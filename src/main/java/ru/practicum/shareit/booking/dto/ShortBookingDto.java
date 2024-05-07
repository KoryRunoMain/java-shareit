package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ShortBookingDto {

    private Long id;

    private Long bookerId;

    private LocalDateTime start;

    private LocalDateTime end;

}
