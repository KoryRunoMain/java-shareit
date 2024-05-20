package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final BookingService service;


    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId,
                                 @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getBooking: userId{}, bookingId{}", userId, bookingId);
        return service.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllUserBookings(@RequestParam(defaultValue = "ALL") String state,
                                               @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getAllUserBookings: userId{}, state{}", userId, state);
        return service.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(@RequestParam(defaultValue = "ALL") String state,
                                                @RequestHeader(OWNER_ID) Long owner) {
        log.info("Get-request getAllOwnerBookings: ownerId{}, state{}", owner, state);
        return service.getAllOwnerBookings(owner, state);
    }

    @PostMapping
    public BookingDto createBooking(@Validated @RequestBody InputBookingDto inputBookingDto,
                                    @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request createBooking: userId{}, bookingDto{}", userId, inputBookingDto);
        return service.create(inputBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader(OWNER_ID) Long userId) {
        log.info("Path-request approveBooking: userId{}, bookingId{}, approve{}", userId, bookingId, approved);
        return service.approve(userId, bookingId, approved);
    }

}
