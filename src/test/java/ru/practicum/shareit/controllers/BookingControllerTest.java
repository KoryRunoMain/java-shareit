package ru.practicum.shareit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService service;

    @Autowired
    private MockMvc mvc;

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
                .id(1L)
                .itemId(10L)
                .start(LocalDateTime.of(2024, 06,24, 14, 00, 00))
                .end(LocalDateTime.of(2024, 06,30, 14, 00, 00))
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
        when(service.create(any(InputBookingDto.class), anyLong())).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
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
        verify(service).create(inputBookingDtoCaptor.capture(), userIdCaptor.capture());

        InputBookingDto capturedBookingDto = inputBookingDtoCaptor.getValue();
        assertThat(capturedBookingDto.getItemId(), equalTo(inputBookingDto.getItemId()));
        assertThat(capturedBookingDto.getStart(), equalTo(inputBookingDto.getStart()));
        assertThat(capturedBookingDto.getEnd(), equalTo(inputBookingDto.getEnd()));
        Long capturedUserId = userIdCaptor.getValue();
        assertThat(capturedUserId, equalTo(1L));
    }

    @Test
    void test_2_getAllOwnerBookings_And_ReturnStatusOk() throws Exception {
        when(service.getAllOwnerBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookingDtoList);
        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(service).getAllOwnerBookings(1L, "ALL", 0, 10);
    }

    @Test
    void test_3_getAllUserBookings_And_ReturnStatusOk() throws Exception {
        when(service.getAllUserBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookingDtoList);
        mvc.perform(get("/bookings/?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(service).getAllUserBookings(eq(1L), eq("ALL"), eq(0), eq(10));
    }

    @Test
    void test_4_getBooking_And_ReturnStatusOk() throws Exception {
        when(service.getById(anyLong(), anyLong())).thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(service).getById(eq(1L), eq(bookingDto.getId()));
    }

    @Test
    void test_5_approveBooking_And_ReturnStatusOk() throws Exception {
        when(service.approve(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
