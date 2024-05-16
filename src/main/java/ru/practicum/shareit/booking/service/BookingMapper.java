
package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.item.service.ItemMapper;

@Component
@AllArgsConstructor
public class BookingMapper {
    private UserMapper userMapper;
    private ItemMapper itemMapper;

    public BookingDto toBookingDto(Booking booking) {
        return booking == null ? null : BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .item(itemMapper.toItemDto(booking.getItem()))
                .booker(userMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDto bookingDto) {
        return bookingDto == null ? null : Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(itemMapper.toItem(bookingDto.getItem(), bookingDto.getBooker().getId()))
                .booker(userMapper.toUser(bookingDto.getBooker()))
                .status(bookingDto.getStatus())
                .build();
    }

}