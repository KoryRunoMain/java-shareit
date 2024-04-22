package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.IItemStorage;
import ru.practicum.shareit.item.storage.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {

    private final UserService userService;
    private final ItemMapper itemMapper;
    private final IItemStorage itemStorage;

    @Transactional
    public ItemDto get(Long itemId) {
        log.info("Ok!");
        return itemMapper.toItemDto(itemStorage.getItemId(itemId));
    }

    @Transactional
    public ItemDto create(ItemDto itemDto, Long userId) {
        log.info("Ok!");
        UserDto user = userService.getUserById(userId);
        if (user != null) {
            return itemMapper.toItemDto(itemStorage.create(itemMapper.toItem(itemDto, userId)));
        } else {
            return null;
        }
    }

    @Transactional
    public Item update(ItemDto itemDto, Long ownerId, Long itemId) {
        Item oldItem = itemStorage.getItemId(itemId);
        if (oldItem == null) {
            throw new NotFoundException("Item not found!");
        }
        if (!oldItem.getOwner().equals(ownerId)) {
            throw new NotFoundException("Not found!");
        }
        oldItem.setName(itemDto.getName() != null ? itemDto.getName() : oldItem.getName());
        oldItem.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : oldItem.getDescription());
        oldItem.setAvailable(itemDto.isAvailable());
        return itemStorage.update(oldItem);
    }

    @Transactional
    public ItemDto delete(Long itemId, Long ownerId) {
        Item item = itemStorage.getItemId(itemId);
        if (!item.getOwner().equals(ownerId)) {
            throw new NotFoundException("Not Found!");
        }
        log.info("Ok!");
        return itemMapper.toItemDto(itemStorage.delete(itemId));
    }

    @Transactional
    public void deleteItemsByOwner(Long owner) {
        itemStorage.deleteItemsByOwner(owner);
        log.info("Ok!");
    }

    @Transactional
    public List<ItemDto> getItemByOwner(Long owner) {
        log.info("Ok!");
        return itemStorage.getItemByOwner(owner).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ItemDto> getItemSearch(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        String searchText = text.toLowerCase();
        return itemStorage.getItemSearch(searchText)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
