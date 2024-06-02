package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {

    private Long id;
    private Long requestId;
    private String name;
    private String description;
    private Boolean available;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private User owner;
    private List<CommentDto> comments;

}
