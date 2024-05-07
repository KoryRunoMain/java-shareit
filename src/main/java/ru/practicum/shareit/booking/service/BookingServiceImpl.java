package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidStateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto create(InputBookingDto inputBookingDto, Long bookerId) {
        if (inputBookingDto.getStart().isAfter(inputBookingDto.getEnd()) ||
                inputBookingDto.getStart().equals(inputBookingDto.getEnd())) {
            throw new ValidationException("fail: invalid booking time!");
        }

        UserDto userDto = userService.getById(bookerId);
        ItemDto itemDto = itemService.getById(inputBookingDto.getItemId());
        if (itemService.getOwnerId(itemDto.getId()).equals(bookerId)) {
            throw new NotFoundException("fail: owner can not be a booker!");
        }

        Booking booking = bookingMapper.toBooking(inputBookingDto, userDto, itemDto);
        if (!booking.getItem().getAvailable()) {
            throw new ValidationException("fail: item cannot be booked!");
        }

        repository.save(booking);
        BookingDto createdBookingDto = bookingMapper.toBookingDto(booking);
        log.info("method: createBooking |Request/Response|" + " inputBookingDto:{}, bookerId:{} /" +
                " createdBookingDto:{}", inputBookingDto, bookerId, createdBookingDto);
        return bookingMapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto approve(Long userId, Long bookingId, Boolean isApproved) {
        userService.getById(userId);

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("fail: bookingId Not Found!"));

        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("fail: booking is already approved!");
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("fail: only user can approve booking!");
        }

        BookingStatus newStatus = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        repository.save(booking);
        BookingDto approvedBookingDto = bookingMapper.toBookingDto(booking);
        log.info("method: approve |Request/Response|" + "userId:{}, bookingId:{}, isApproved:{} / " +
                        " bookingStatus:{}, approvedBookingDto:{}",
                userId, bookingId, isApproved, newStatus, approvedBookingDto);
        return approvedBookingDto;
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("fail: bookingId Not Found!"));

        if (!(booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId))) {
            throw new NotFoundException("fail: userId not equals bookerId or equals ownerId!");
        }

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        log.info("method: getById |Request/Response|" + "userId:{}, bookingId:{} / bookingDto:{}",
                userId, bookingId, bookingDto);
        return bookingDto;
    }

    @Override
    public List<BookingDto> getAllUserBookings(Long userId, String state) {
        userService.getById(userId);
        validateState(state);

        switch (BookingState.valueOf(state)) {
            case ALL:
                return getBookingDtoList(repository.findByBookerIdOrderByStartDesc(
                        userId));
            case CURRENT:
                return getBookingDtoList(repository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now()));
            case PAST:
                return getBookingDtoList(repository.findByBookerIdAndEndIsBeforeOrderByStartDesc(
                        userId, LocalDateTime.now()));
            case FUTURE:
                return getBookingDtoList(repository.findByBookerIdAndStartIsAfterOrderByStartDesc(
                        userId, LocalDateTime.now()));
            case WAITING:
                return getBookingDtoList(repository.findByBookerIdAndStartIsAfterAndStatusOrderByStartDesc(
                        userId, LocalDateTime.now(), BookingStatus.WAITING));
            case REJECTED:
                return getBookingDtoList(repository.findByBookerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.REJECTED));
            default:
                throw new InvalidStateException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(Long ownerId, String state) {
        userService.getById(ownerId);
        validateState(state);

        switch (BookingState.valueOf(state)) {
            case ALL:
                return getBookingDtoList(repository.findByItemOwnerIdOrderByStartDesc(
                        ownerId));
            case CURRENT:
                return getBookingDtoList(repository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now(), LocalDateTime.now()));
            case PAST:
                return getBookingDtoList(repository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                        ownerId, LocalDateTime.now()));
            case FUTURE:
                return getBookingDtoList(repository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now()));
            case WAITING:
                return getBookingDtoList(repository.findByItemOwnerIdAndStartIsAfterAndStatusOrderByStartDesc(
                        ownerId, LocalDateTime.now(), BookingStatus.WAITING));
            case REJECTED:
                return getBookingDtoList(repository.findByItemOwnerIdAndStatusOrderByStartDesc(
                        ownerId, BookingStatus.REJECTED));
            default:
                throw new InvalidStateException("Unknown state: " + state);
        }
    }

    private List<BookingDto> getBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void validateState(String state) {
        try {
            BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStateException("Unknown state: " + state);
        }
    }

}
