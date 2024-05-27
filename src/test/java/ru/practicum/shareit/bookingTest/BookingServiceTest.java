package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserMapper userMapper;

    private static final Long BOOKING_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long OWNER_ID = 2L;
    private static final Long BOOKER_ID = 3L;
    private static final Long REQUEST_ID = 1L;

    //Users
    private final User user = new User(USER_ID, "user", "user@user.user");
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");
    private final User booker = new User(BOOKER_ID, "booker", "booker@booker.booker");
    private final UserDto bookerDto = new UserDto(BOOKER_ID, "booker", "booker@booker.booker");
    //Items
    private final Item item = new Item(ITEM_ID, "item", "descriptionItem", true, null, user, null, null, null);
    private final ItemDto itemDto = new ItemDto(ITEM_ID, REQUEST_ID, "item", "descriptionItem", true, null, null, user, null);
    private final InputBookingDto inputBookingDto = new InputBookingDto(ITEM_ID, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
    //Bookings
    private final Booking booking = new Booking(BOOKING_ID, inputBookingDto.getStart(), inputBookingDto.getEnd(), item, booker, BookingStatus.WAITING);
    private final BookingDto bookingDto = new BookingDto(BOOKING_ID, ITEM_ID, booking.getStart(), booking.getEnd(), itemDto, bookerDto, BookingStatus.APPROVED);
    private final List<BookingDto> bookingDtoList = List.of(bookingDto);

    @BeforeEach
    void setUp() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
    }

    @Test
    void test_1_getById_And_ReturnBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);
        assertEquals(bookingDto, bookingService.getById(user.getId(), booking.getId()));
        verify(bookingRepository).findById(anyLong());
        verify(bookingMapper).toBookingDto(any(Booking.class));
    }

    @Test
    void test_2_getUserBookings_And_Return_All() {
        List<Booking> bookings = List.of(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings, PageRequest.of(0, 10), bookings.size());
        when(bookingRepository.findByBookerIdOrderByStartDesc(eq(USER_ID), eq(PageRequest.of(0, 10)))).thenReturn(bookingPage);
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);
        List<BookingDto> allUserBookings = bookingService.getAllUserBookings(USER_ID, "ALL", 0, 10);
        assertEquals(bookingDtoList, allUserBookings);
    }

    @Test
    void test_3_getAllOwnerBookings_And_ReturnBookingList() {
        List<Booking> bookings = List.of(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings, PageRequest.of(0, 10), bookings.size());
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(OWNER_ID, PageRequest.of(0, 10))).thenReturn(bookingPage);
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);
        List<BookingDto> allOwnerBookings = bookingService.getAllOwnerBookings(OWNER_ID, "ALL", 0, 10);
        assertEquals(bookingDtoList, allOwnerBookings);
    }

    @Test
    void test_4_create_And_ReturnBooking() {
        when(itemService.getById(ITEM_ID)).thenReturn(itemDto);
        when(userService.getById(BOOKER_ID)).thenReturn(userDto);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(bookingMapper.toBooking(any(InputBookingDto.class), any(UserDto.class), any(ItemDto.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingDto createdBookingDto = bookingService.create(inputBookingDto, BOOKER_ID);
        assertEquals(bookingDto, createdBookingDto);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void test_5_create_And_ReturnErrorOwnerCanNotBeBooker() {
        when(itemService.getById(any(Long.class))).thenReturn(itemDto);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.create(inputBookingDto, USER_ID), "fail: owner can not be a booker!");
        assertEquals("fail: owner can not be a booker!", exception.getMessage());
    }

    @Test
    void test_6_approve_And_ReturnBooking() {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);
        BookingDto approvedBooking = bookingService.approve(user.getId(), BOOKING_ID, true);
        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

}
