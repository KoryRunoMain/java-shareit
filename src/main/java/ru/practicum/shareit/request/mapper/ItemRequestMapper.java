package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

@Component
@AllArgsConstructor
public class ItemRequestMapper {
    private UserService userService;
    private UserMapper userMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, Long userId) {
        return itemRequest == null ? null : ItemRequestDto.builder()
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestor(userService.getById(userId))
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
