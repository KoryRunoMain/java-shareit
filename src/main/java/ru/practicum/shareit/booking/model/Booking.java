package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.enums.BookingStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @NotBlank
    private Long item;
    @NotBlank
    private Long booker;
    @NotNull
    private BookingStatus status;
}
