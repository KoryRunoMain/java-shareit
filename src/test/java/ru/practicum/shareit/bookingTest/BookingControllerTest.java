package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 10L;
    private static final Long BOOKING_ID = 1L;
    private static final Long WRONG_ID = 10L;

    private InputBookingDto inputBookingDto;
    private BookingDto bookingDto;
    private List<BookingDto> bookingDtoList;

    @BeforeEach
    void setUp() {
        ItemDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        bookingDto = BookingDto.builder()
                .id(BOOKING_ID)
                .itemId(ITEM_ID)
                .start(LocalDateTime.of(2024, 06, 24, 14, 00, 00))
                .end(LocalDateTime.of(2024, 06, 30, 14, 00, 00))
                .item(new ItemDto())
                .booker(new UserDto())
                .status(BookingStatus.WAITING)
                .build();

        inputBookingDto = new InputBookingDto(
                bookingDto.getItemId(),
                bookingDto.getStart(),
                bookingDto.getEnd()
        );
        bookingDtoList = Collections.singletonList(bookingDto);
    }

    @Test
    void test_1_createBooking_And_ReturnStatusOk() throws Exception {
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", USER_ID)
                        .content(mapper.writeValueAsString(inputBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ArgumentCaptor<InputBookingDto> inputBookingDtoCaptor = ArgumentCaptor.forClass(InputBookingDto.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookingService).create(inputBookingDtoCaptor.capture(), userIdCaptor.capture());

        InputBookingDto capturedBookingDto = inputBookingDtoCaptor.getValue();
        assertThat(capturedBookingDto.getItemId(), equalTo(inputBookingDto.getItemId()));
        assertThat(capturedBookingDto.getStart(), equalTo(inputBookingDto.getStart()));
        assertThat(capturedBookingDto.getEnd(), equalTo(inputBookingDto.getEnd()));
        Long capturedUserId = userIdCaptor.getValue();
        assertThat(capturedUserId, equalTo(USER_ID));
    }

    @Test
    void test_2_createBookingWithInvalidData_And_ReturnException() throws Exception {
        InputBookingDto wrongInputBookingDto = InputBookingDto.builder()
                .start(LocalDateTime.of(2000, 05, 28, 10, 00, 00))
                .end(LocalDateTime.of(2024, 05, 28, 10, 00, 00))
                .itemId(1L)
                .build();
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(wrongInputBookingDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, never()).create(wrongInputBookingDto, USER_ID);
    }

    @Test
    void test_3_createBookingUserNotOwner_And_ReturnException() throws Exception {
        when(bookingService.create(any(), anyLong())).thenThrow(new NotFoundException(""));
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", WRONG_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputBookingDto)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void test_4_getAllOwnerBookings_And_ReturnStatusOk() throws Exception {
        when(bookingService.getAllOwnerBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookingDtoList);
        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", USER_ID)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).getAllOwnerBookings(USER_ID, "ALL", 0, 10);
    }

    @Test
    void test_5_getAllUserBookings_And_ReturnStatusOk() throws Exception {
        when(bookingService.getAllUserBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookingDtoList);
        mvc.perform(get("/bookings/?state=ALL")
                        .header("X-Sharer-User-Id", USER_ID)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).getAllUserBookings(eq(USER_ID), eq("ALL"), eq(0), eq(10));
    }

    @Test
    void test_6_getBooking_And_ReturnStatusOk() throws Exception {
        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).getById(eq(BOOKING_ID), eq(bookingDto.getId()));
    }

    @Test
    void test_7_getBookingWithNotFoundBookingId_And_ReturnException() throws Exception {
        when(bookingService.getById(anyLong(), anyLong())).thenThrow(new NotFoundException(""));
        mvc.perform(get("/bookings/{bookingId}", WRONG_ID)
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputBookingDto)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void test_8_approveBooking_And_ReturnStatusOk() throws Exception {
        when(bookingService.approve(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
