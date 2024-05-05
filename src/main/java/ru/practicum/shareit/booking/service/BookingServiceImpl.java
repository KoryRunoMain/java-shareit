package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public class BookingServiceImpl implements BookingService {

    @Override
    public BookingDto create(BookingDto bookingDto, Long userId) {
        return null;
    }

    @Override
    public BookingDto approve(Long userId, Long bookingId, Boolean state) {
        return null;
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getAllUserBookings(Long userId, String state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(Long owner, String state) {
        return null;
    }

}
