package ru.practicum.shareit.booking.dto;

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
public class BookingDto {
    private LocalDateTime start;
    private LocalDateTime end;
    @NotBlank
    private Long item;
    @NotBlank
    private Long booker;
    @NotNull
    private BookingStatus status;
}
