package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

@Service
public interface ItemService {

    ItemDto get(Long itemId);

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDto delete(Long itemId, Long ownerId);

    void deleteItemsByOwner(Long owner);

    List<ItemDto> getItemByOwner(Long owner);

    List<ItemDto> getItemSearch(String text);

}
