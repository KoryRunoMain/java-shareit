package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

@Service
public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto saveItem(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDto findItemById(Long itemId);

    List<ItemDto> findItemByOwner(Long owner);

    List<ItemDto> searchItem(String text);

    void deleteItemById(Long itemId, Long ownerId);

}
