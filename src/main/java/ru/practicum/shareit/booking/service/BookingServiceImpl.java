package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private UserService userService;
    private BookingMapper bookingMapper;
    private BookingRepository bookingRepository;

    @Override
    public BookingDto create(BookingDto bookingDto, Long userId) {
        if (userService.getById(userId) == null) {
            throw new NotFoundException("fail: create.getUser() User is Null!");
        }
        BookingDto createdBookingDto = bookingMapper.toBookingDto(
                bookingRepository.save(bookingMapper.toBooking(bookingDto))
        );
        log.info("/////////////////////////////////////////////////////////////////////////////////////");
        return createdBookingDto;
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
