package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private BookingMapper bookingMapper;

    private static final Long ITEM_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long BOOKING_ID = 1L;
    private static final Long REQUEST_ID = 1L;

    //Users
    private final User user = new User(USER_ID, "user", "user@user.user");
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");
    //Items
    private final Item item = new Item(ITEM_ID, "item", "descriptionItem", true, null, user, null, null, null);
    private final ItemDto itemDto = new ItemDto(ITEM_ID, REQUEST_ID, "item", "descriptionItem", true, null, null, user, null);

    private final Booking booking = Booking.builder()
            .id(BOOKING_ID)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now().plusDays(1))
            .item(item)
            .booker(user)
            .status(BookingStatus.WAITING)
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(BOOKING_ID)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now().plusDays(1))
            .item(itemDto)
            .booker(userDto)
            .status(BookingStatus.WAITING)
            .build();
    private final InputBookingDto inputBookingDto = InputBookingDto.builder()
            .itemId(ITEM_ID)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now().plusDays(1))
            .build();


    @Test
    void toBookingDto_successfully() {
        BookingDto actBookingDto = bookingMapper.toBookingDto(booking);
        assertEquals(actBookingDto.getId(), bookingDto.getId());
        assertEquals(actBookingDto.getStatus(), bookingDto.getStatus());
    }

    @Test
    void toBooking_successfully() {
        Booking actBooking = bookingMapper.toBooking(inputBookingDto, userDto, itemDto);
        assertNotNull(actBooking);
        assertEquals(actBooking.getStart(), inputBookingDto.getStart());
        assertEquals(actBooking.getEnd(), inputBookingDto.getEnd());
    }

    @Test
    void toItemBookingDto_successfully() {
        ShortBookingDto actShortBooking = bookingMapper.toItemBookingDto(booking);
        assertEquals(actShortBooking.getId(), bookingDto.getId());
    }

}
