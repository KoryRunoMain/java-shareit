package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.booking.enums.BookingStatus.WAITING;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private static final Long USER_ID = 1L;
    private static final Long BOOKER_ID = 2L;
    private static final Long ITEM_ID = 1L;
    private static final Long BOOKING_ID = 1L;
    private Booking booking;
    private Item item;

    @BeforeEach
    void setUp() {
        User user = new User(USER_ID, "user", "user@user.user");
        User booker = new User(BOOKER_ID, "user2", "user2@user.user");
        item = new Item(ITEM_ID, "Arduino", "Arduino", true, null, user, null, null, null);
        booking = new Booking(BOOKING_ID, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, booker, WAITING);
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
    }

    @Test
    void findByItemId_successfully() {
        List<Booking> foundedBookings = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
        assertThat(foundedBookings.size(), equalTo(1));
        assertThat(foundedBookings.get(0), equalTo(booking));
    }

    @Test
    void findByBookerId_Current_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime timeBefore = booking.getStart().minusMinutes(1);
        LocalDateTime timeAfter = booking.getEnd().plusMinutes(1);
        Page<Booking> foundedBookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                BOOKER_ID, timeAfter, timeBefore, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(1));
        assertThat(foundedBookings.getContent(), equalTo(List.of(booking)));
    }

    @Test
    void findByBookerId_Past_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime timeAfter = booking.getEnd().plusMinutes(1);
        Page<Booking> foundedBookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(
                BOOKER_ID, timeAfter, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(1));
        assertThat(foundedBookings.getContent(), equalTo(List.of(booking)));
    }

    @Test
    void findByBookerId_Future_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime timeBefore = booking.getStart().minusMinutes(1);
        Page<Booking> foundedBookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(
                BOOKER_ID, timeBefore, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(1));
        assertThat(foundedBookings.getContent(), equalTo(List.of(booking)));
    }

    @Test
    void findByBooker_Rejected_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> foundedBookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(
                BOOKER_ID, WAITING, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(1));
        assertThat(foundedBookings.getContent(), equalTo(List.of(booking)));
    }

    @Test
    void findByOwnerId_Current_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime timeBefore = booking.getStart().minusMinutes(1);
        LocalDateTime timeAfter = booking.getEnd().plusMinutes(1);
        Page<Booking> foundedBookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                USER_ID, timeAfter, timeBefore, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(1));
        assertThat(foundedBookings.getContent(), equalTo(List.of(booking)));
    }

    @Test
    void findByOwnerId_Past_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime timeAfter = booking.getEnd().plusMinutes(1);
        Page<Booking> foundedBookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(
                USER_ID, timeAfter, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(0));
    }

    @Test
    void findByOwnerId_Future_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime timeBefore = booking.getStart().minusMinutes(1);
        Page<Booking> foundedBookings = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                USER_ID, timeBefore, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(0));
    }

    @Test
    void findByOwnerId_Rejected_successfully() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> foundedBookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                USER_ID, WAITING, pageable);
        assertThat(foundedBookings.getContent().size(), equalTo(1));
        assertThat(foundedBookings.getContent(), equalTo(List.of(booking)));
    }

}
