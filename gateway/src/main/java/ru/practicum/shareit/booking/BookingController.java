package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.InvalidStateException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
	private static final String OWNER_ID = "X-Sharer-User-Id";
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(OWNER_ID) long userId,
											  @RequestParam(name = "state", defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with userId={}, state {}, from={}, size={}", userId, stateParam, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingsByOwner(@RequestParam(defaultValue = "ALL") String state,
														@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
														@Positive @RequestParam(defaultValue = "10") Integer size,
														@RequestHeader(OWNER_ID) long userId) {
		BookingState stateParam = BookingState.from(state)
				.orElseThrow(() -> new InvalidStateException("Unknown state: " + state));
		log.info("Get booking with userId={}, state {}, from={}, size={}", userId, state, from, size);
		return bookingClient.getBookingsByOwner(userId, stateParam, from, size);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
											 @RequestHeader(OWNER_ID) long userId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(userId, bookingId);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid BookItemRequestDto requestDto,
										 @RequestHeader(OWNER_ID) long userId) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.create(userId, requestDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBooking(@PathVariable Long bookingId,
												@RequestParam @NotNull Boolean approved,
												@RequestHeader(OWNER_ID) long userId) {
		log.info("patch approved bookingId={} userId={}", bookingId, userId);
		return bookingClient.approve(bookingId, userId, approved);
	}

}
