package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto findItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    throw new NotFoundException("item id Not Found!");
                });
        // написать обновление данных в Booking
        // написать выдачу комментариев по id вещи

        log.info("method: get |Request/Response|" + "itemId:{} / itemId:{}",
                itemId, item);
        return  itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        if (userService.findUserById(userId) == null) {
            throw new NotFoundException("fail: create.getUser() User is Null!");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("fail: create.getAvailable() Available is Null!");
        }
        ItemDto createdItemDto = itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, userId)));
        log.info("method: create |Request/Response|" + "itemDto:{}, userId:{} / createdItemDto:{}",
                itemDto, userId, createdItemDto);
        return createdItemDto;
    }

    @Override
    public ItemDto saveItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item itemToUpdate = itemMapper.toItem(findItemById(itemId), ownerId);
        if (userService.findUserById(ownerId) == null || !itemToUpdate.getOwner().equals(ownerId)) {
            throw new NotFoundException("fail: update.getOwner() Item Not Found!");
        }
        itemMapper.updateItemDto(itemDto, itemToUpdate, itemId);
        ItemDto updatedItemDto = itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, ownerId)));
        log.info("method: update |Request/Response|" + "itemDto:{}, itemId:{}, userId:{} / createdItemDto:{}",
                itemDto, itemId, ownerId, updatedItemDto);
        return updatedItemDto;
    }

    @Override
    public void deleteItemById(Long itemId, Long ownerId) {
//        Item item = itemRepository.findById(itemId);
//        if (!item.getOwner().equals(ownerId)) {
//            throw new NotFoundException("fail: delete.getOwner() Owner of item Not Found!");
//        }
        itemRepository.deleteById(itemId);
        log.info("method: delete |Request/Response|" + "itemId:{} / deletedItemDto:{}",
                itemId, ownerId);
    }

    @Override
    public List<ItemDto> findItemByOwner(Long owner) {
        List<ItemDto> items = itemRepository.findAllById(Collections.singleton(owner)).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("method: getItemByOwner |Response|" + "items:{}",
                items);
        return items;
    }

    // обновить логику в JPA контексте (pageable)
    @Override
    public List<ItemDto> searchItem(String text) {
//        if (text == null || text.isBlank()) {
//            return Collections.emptyList();
//        }
//        String searchText = text.toLowerCase();
//        List<ItemDto> items = itemRepository.searchAva(searchText)
//                .stream()
//                .map(itemMapper::toItemDto)
//                .collect(Collectors.toList());
//        log.info("method: getItemSearch |Request/Response|" + "search:{} / items:{}",
//                text, items);
        return Collections.emptyList();
    }
}
