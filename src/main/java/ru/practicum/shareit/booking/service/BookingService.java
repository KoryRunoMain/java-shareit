package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

public interface BookingService {

    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto approve(Long userId, Long bookingId, Boolean state);

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getAllUserBookings(Long userId, String state);

    List<BookingDto> getAllOwnerBookings(Long owner, String state);

}
