package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

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

    public Booking toBooking(InputBookingDto inputBookingDto, UserDto userDto, ItemDto itemDto) {
        return inputBookingDto == null ? null : Booking.builder()
                .start(inputBookingDto.getStart())
                .end(inputBookingDto.getEnd())
                .item(itemMapper.toItem(itemDto))
                .booker(userMapper.toUser(userDto))
                .status(BookingStatus.WAITING)
                .build();
    }

    public ShortBookingDto toItemBookingDto(Booking booking) {
        return booking == null ? null : ShortBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

}
