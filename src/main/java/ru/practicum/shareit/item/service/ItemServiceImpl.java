package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemStorage itemStorage;

    @Override
    public ItemDto get(Long itemId) {
        log.info("Ok!");
        return itemMapper.toItemDto(itemStorage.getItemId(itemId));
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Not Found!");
        }
        Item item = itemMapper.toItem(itemDto, userId);
        ItemDto createItemDto = itemMapper.toItemDto(itemStorage.create(item));
        log.info("create.Ok!");
        return createItemDto;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long ownerId) {
        Item oldItem = itemStorage.getItemId(itemId);
        if (oldItem == null) {
            throw new NotFoundException("Not found!");
        }
        if (!oldItem.getOwner().equals(ownerId)) {
            throw new NotFoundException("Not found!");
        }
        oldItem.setName(itemDto.getName() != null ? itemDto.getName() : oldItem.getName());
        oldItem.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : oldItem.getDescription());
        oldItem.setAvailable(itemDto.isAvailable());
        log.info("Ok!");
        return itemMapper.toItemDto(itemStorage.update(itemMapper.toItem(itemDto, ownerId)));
    }

    @Override
    public ItemDto delete(Long itemId, Long ownerId) {
        Item item = itemStorage.getItemId(itemId);
        if (!item.getOwner().equals(ownerId)) {
            throw new NotFoundException("Not Found!");
        }
        log.info("Ok!");
        return itemMapper.toItemDto(itemStorage.delete(itemId));
    }

    @Override
    public void deleteItemsByOwner(Long owner) {
        itemStorage.deleteItemsByOwner(owner);
        log.info("Ok!");
    }

    @Override
    public List<ItemDto> getItemByOwner(Long owner) {
        log.info("Ok!");
        return itemStorage.getItemByOwner(owner).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemSearch(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        String searchText = text.toLowerCase();
        log.info("Ok!");
        return itemStorage.getItemSearch(searchText)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
