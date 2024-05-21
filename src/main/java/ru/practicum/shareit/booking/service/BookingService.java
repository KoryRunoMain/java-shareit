package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.util.List;

public interface BookingService {

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getAllUserBookings(Long userId, String state, int from, int size);

    List<BookingDto> getAllOwnerBookings(Long owner, String state, int from, int size);

    BookingDto create(InputBookingDto inputBookingDto, Long userId);

    BookingDto approve(Long userId, Long bookingId, Boolean state);

}
