package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {

    private Long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
    private List<ItemDto> items;

}