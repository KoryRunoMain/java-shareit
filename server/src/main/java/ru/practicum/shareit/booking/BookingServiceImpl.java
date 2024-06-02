package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.exception.InvalidStateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

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

        ItemDto itemDto = itemService.getById(inputBookingDto.getItemId());
        if (itemDto.getOwner().getId().equals(bookerId)) {
            throw new NotFoundException("fail: owner can not be a booker!");
        }

        if (!itemDto.getAvailable()) {
            throw new ValidationException("fail: item cannot be booked!");
        }

        UserDto userDto = userService.getById(bookerId);
        Booking booking = bookingMapper.toBooking(inputBookingDto, userDto, itemDto);
        Booking savedBooking = repository.save(booking);
        BookingDto createdBookingDto = bookingMapper.toBookingDto(savedBooking);
        log.info("method: createBooking |Request/Response|" + " inputBookingDto:{}, bookerId:{} /" +
                " createdBookingDto:{}", inputBookingDto, bookerId, createdBookingDto);
        return bookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto approve(Long userId, Long bookingId, Boolean status) {
        userService.getById(userId);

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("fail: bookingId Not Found!"));

        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("fail: booking is already approved!");
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("fail: only user can approve booking!");
        }

        BookingStatus newStatus = status ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        repository.save(booking);

        BookingDto approvedBookingDto = bookingMapper.toBookingDto(booking);
        log.info("method: approve |Request/Response|" + " userId:{}, bookingId:{}, isApproved:{} / " +
                        " bookingStatus:{}, approvedBookingDto:{}",
                userId, bookingId, status, newStatus, approvedBookingDto);
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
        log.info("method: getById |Request/Response|" + " userId:{}, bookingId:{} / bookingDto:{}",
                userId, bookingId, bookingDto);
        return bookingDto;
    }

    @Override
    public List<BookingDto> getAllUserBookings(Long userId, String state, int from, int size) {
        userService.getById(userId);
        int pageNumber = from / size;
        Pageable pageRequest = PageRequest.of(pageNumber, size);

        switch (BookingState.valueOf(state)) {
            case ALL:
                List<BookingDto> allList = getBookingDtoList(repository.findByBookerIdOrderByStartDesc(userId, pageRequest));
                log.info("method: getAllUserBookings |Request/Response|" + " userId={}, state={} / list={}",
                        userId, state, allList);
                return allList;
            case CURRENT:
                List<BookingDto> currentList = getBookingDtoList(repository.findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageRequest));
                log.info("method: getAllUserBookings |Request/Response|" + " userId={}, state={} / list={}",
                        userId, state, currentList);
                return currentList;
            case PAST:
                List<BookingDto> pastList = getBookingDtoList(repository.findByBookerIdAndEndIsBeforeOrderByStartDesc(
                        userId, LocalDateTime.now(), pageRequest));
                log.info("method: getAllUserBookings |Request/Response|" + " userId={}, state={} / list={}",
                        userId, state, pastList);
                return pastList;
            case FUTURE:
                List<BookingDto> futureList = getBookingDtoList(repository.findByBookerIdAndStartIsAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), pageRequest));
                log.info("method: getAllUserBookings |Request/Response|" + " userId={}, state={} / list={}",
                        userId, state, futureList);
                return futureList;
            case WAITING:
                List<BookingDto> waitingList = getBookingDtoList(repository.findByBookerIdAndStartIsAfterAndStatusOrderByStartDesc(
                        userId, LocalDateTime.now(), BookingStatus.WAITING, pageRequest));
                log.info("method: getAllUserBookings |Request/Response|" + " userId={}, state={} / list={}",
                        userId, state, waitingList);
                return waitingList;
            case REJECTED:
                List<BookingDto> rejectedList = getBookingDtoList(repository.findByBookerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.REJECTED, pageRequest));
                log.info("method: getAllUserBookings |Request/Response|" + " userId={}, state={} / list={}",
                        userId, state, rejectedList);
                return rejectedList;
            default:
                throw new InvalidStateException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(Long ownerId, String state, int from, int size) {
        userService.getById(ownerId);
        int pageNumber = from / size;
        Pageable pageRequest = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "id"));

        switch (BookingState.valueOf(state)) {
            case ALL:
                List<BookingDto> allList = getBookingDtoList(repository.findByItemOwnerIdOrderByStartDesc(ownerId, pageRequest));
                log.info("method: getAllOwnerBookings |Request/Response|" + " ownerId={}, state={} / list={}",
                        ownerId, state, allList);
                return allList;
            case CURRENT:
                List<BookingDto> currentList = getBookingDtoList(repository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now(), LocalDateTime.now(), pageRequest));
                log.info("method: getAllOwnerBookings |Request/Response|" + " ownerId={}, state={} / list={}",
                        ownerId, state, currentList);
                return currentList;
            case PAST:
                List<BookingDto> pastList = getBookingDtoList(repository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                        ownerId, LocalDateTime.now(), pageRequest));
                log.info("method: getAllOwnerBookings |Request/Response|" + " ownerId={}, state={} / list={}",
                        ownerId, state, pastList);
                return pastList;
            case FUTURE:
                List<BookingDto> futureList = getBookingDtoList(repository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                        ownerId, LocalDateTime.now(), pageRequest));
                log.info("method: getAllOwnerBookings |Request/Response|" + " ownerId={}, state={} / list={}",
                        ownerId, state, futureList);
                return futureList;
            case WAITING:
                List<BookingDto> waitingList = getBookingDtoList(repository.findByItemOwnerIdAndStartIsAfterAndStatusOrderByStartDesc(
                        ownerId, LocalDateTime.now(), BookingStatus.WAITING, pageRequest));
                log.info("method: getAllOwnerBookings |Request/Response|" + " ownerId={}, state={} / list={}",
                        ownerId, state, waitingList);
                return waitingList;
            case REJECTED:
                List<BookingDto> rejectedList = getBookingDtoList(repository.findByItemOwnerIdAndStatusOrderByStartDesc(
                        ownerId, BookingStatus.REJECTED, pageRequest));
                log.info("method: getAllOwnerBookings |Request/Response|" + " ownerId={}, state={} / list={}",
                        ownerId, state, rejectedList);
                return rejectedList;
            default:
                throw new InvalidStateException("Unknown state: " + state);
        }
    }

    private List<BookingDto> getBookingDtoList(Page<Booking> bookings) {
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

}