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
        log.info("get.Ok!");
        return getItemDto;
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        if (userService.get(userId) == null) {
            throw new NotFoundException("create.NotFound!");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("create.InValidField!");
        }
        ItemDto createItemDto = itemMapper.toItemDto(itemStorage.create(itemMapper.toItem(itemDto, userId)));
        log.info("create.Ok!");
        return createItemDto;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long ownerId) {
        Item itemToUpdate = itemStorage.getItemId(itemId);

        itemDto.setName(itemDto.getName() != null ? itemDto.getName() : itemToUpdate.getName());
        itemDto.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : itemToUpdate.getDescription());
        itemDto.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : itemToUpdate.getAvailable());
        itemDto.setId(itemDto.getId() != null ? itemDto.getId() : itemId);

        if (userService.get(ownerId) == null || !itemToUpdate.getOwner().equals(ownerId)) {
            throw new NotFoundException("update.NotFound!");
        }
        ItemDto updatedItemDto = itemMapper.toItemDto(itemStorage.update(itemMapper.toItem(itemDto, ownerId)));
        log.info("update.Ok!");
        return updatedItemDto;
    }

    @Override
    public ItemDto delete(Long itemId, Long ownerId) {
        Item item = itemStorage.getItemId(itemId);
        if (!item.getOwner().equals(ownerId)) {
            throw new NotFoundException("delete.NotFound!");
        }
        ItemDto deleteItemDto = itemMapper.toItemDto(itemStorage.delete(itemId));
        log.info("delete.Ok!");
        return deleteItemDto;
    }

    @Override
    public void deleteItemsByOwner(Long owner) {
        itemStorage.deleteItemsByOwner(owner);
        log.info("deleteItemsByOwner.Ok!");
    }

    @Override
    public List<ItemDto> getItemByOwner(Long owner) {
        List<ItemDto> items = itemStorage.getItemByOwner(owner).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("getItemByOwner.Ok!");
        return items;
    }

    @Override
    public List<ItemDto> getItemSearch(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        String searchText = text.toLowerCase();
        List<ItemDto> items = itemStorage.getItemSearch(searchText)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("getItemSearch.Ok!");
        return items;
    }

}
