package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.enums.BookingStatus;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingDto {

    @NotBlank
    private LocalDateTime start;

    @NotBlank
    private LocalDateTime end;

    @NotBlank
    private Long item;

    @NotBlank
    private Long booker;

    private BookingStatus status;

}
