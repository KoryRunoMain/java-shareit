package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemRequestMapper {
    private UserMapper userMapper;
    private ItemMapper itemMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestor(userMapper.toUserDto(itemRequest.getRequestor()))
                .items(itemRequest.getItems() != null ? itemRequest.getItems()
                        .stream()
                        .map(itemMapper::toItemDto)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return itemRequestDto == null ? null : ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(userMapper.toUser(itemRequestDto.getRequestor()))
                .created(itemRequestDto.getCreated())
                .build();
    }

}
