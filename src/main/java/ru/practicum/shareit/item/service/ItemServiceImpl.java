package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemStorage itemStorage;

    @Override
    public ItemDto get(Long itemId) {
        ItemDto getItemDto = itemMapper.toItemDto(itemStorage.getItemId(itemId));
        log.info("method: get |Request/Response|" + "itemId:{} / itemId:{}",
                itemId, getItemDto);
        return getItemDto;
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        if (userService.get(userId) == null) {
            throw new NotFoundException("fail: create.getUser() User is Null!");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("fail: create.getAvailable() Available is Null!");
        }
        ItemDto createdItemDto = itemMapper.toItemDto(itemStorage.create(itemMapper.toItem(itemDto, userId)));
        log.info("method: create |Request/Response|" + "itemDto:{}, userId:{} / createdItemDto:{}",
                itemDto, userId, createdItemDto);
        return createdItemDto;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long ownerId) {
        Item itemToUpdate = itemStorage.getItemId(itemId);
        if (userService.get(ownerId) == null || !itemToUpdate.getOwner().equals(ownerId)) {
            throw new NotFoundException("fail: update.getOwner() Item Not Found!");
        }
        itemMapper.updateItemDto(itemDto, itemToUpdate, itemId);
        ItemDto updatedItemDto = itemMapper.toItemDto(itemStorage.update(itemMapper.toItem(itemDto, ownerId)));
        log.info("method: update |Request/Response|" + "itemDto:{}, itemId:{}, userId:{} / createdItemDto:{}",
                itemDto, itemId, ownerId, updatedItemDto);
        return updatedItemDto;
    }

    @Override
    public ItemDto delete(Long itemId, Long ownerId) {
        Item item = itemStorage.getItemId(itemId);
        if (!item.getOwner().equals(ownerId)) {
            throw new NotFoundException("fail: delete.getOwner() Owner of item Not Found!");
        }
        ItemDto deletedItemDto = itemMapper.toItemDto(itemStorage.delete(itemId));
        log.info("method: delete |Request/Response|" + "itemId:{} / deletedItemDto:{}",
                itemId, ownerId);
        return deletedItemDto;
    }

    @Override
    public void deleteItemsByOwner(Long owner) {
        itemStorage.deleteItemsByOwner(owner);
        log.info("method: deleteItemsByOwner |Request|" + "owner:{}",
                owner);
    }

    @Override
    public List<ItemDto> getItemByOwner(Long owner) {
        List<ItemDto> items = itemStorage.getItemByOwner(owner).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("method: getItemByOwner |Response|" + "items:{}",
                items);
        return items;
    }

    @Override
    public List<ItemDto> getItemSearch(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        String searchText = text.toLowerCase();
        List<ItemDto> items = itemStorage.getItemSearch(searchText)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("method: getItemSearch |Request/Response|" + "search:{} / items:{}",
                text, items);
        return items;
    }
}
