package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

@Service
public interface ItemService {

    ItemDto getItemById(Long itemId, Long userId);

    ItemDto getById(Long itemId);

    Long getOwnerId(Long itemId);

    List<ItemDto> getAll(Long ownerId);

    List<ItemDto> search(String text);

    ItemDto create(ItemDto itemDto, Long userId);

    CommentDto createComment(Long itemId, Long userId, CommentDto commentDto);

    ItemDto save(ItemDto itemDto, Long itemId, Long userId);

}
