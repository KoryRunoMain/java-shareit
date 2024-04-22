package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.IItemStorage;
import ru.practicum.shareit.item.storage.ItemMapper;
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
        Optional<Object> user = userService.getUserById(userId);
        if (user.isPresent()) {
            return itemMapper.toItemDto(itemStorage.create(itemMapper.toItem(itemDto, userId)));
        } else {
            return null;
        }
    }

    @Transactional
    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        Item oldItem = Objects.requireNonNull(itemStorage.getItemId(itemId));
        userService.getUserById(ownerId)
                .filter(user -> user.equals(ownerId))
                .orElseThrow(() ->
                        new NotFoundException("Not found!")
                );
        itemDto.setName(itemDto.getName() != null ? itemDto.getName() : oldItem.getName());
        itemDto.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : oldItem.getDescription());
        itemDto.setAvailable(oldItem.isAvailable());
        itemDto.setId(itemDto.getId() != null ? itemDto.getId() : itemId);
        log.info("Ok!");
        return itemMapper.toItemDto(itemStorage.update(
                itemMapper.toItem(itemDto, ownerId)));
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
