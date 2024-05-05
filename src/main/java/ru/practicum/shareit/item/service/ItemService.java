package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

@Service
public interface ItemService {

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto save(ItemDto itemDto, Long itemId, Long userId);

    ItemDto getById(Long itemId, Long userId);

    List<ItemDto> getAll(Long owner);

    List<ItemDto> search(String text);

    void delete(Long itemId, Long ownerId);

}
