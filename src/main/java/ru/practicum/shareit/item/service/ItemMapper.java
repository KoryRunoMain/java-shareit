package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.service.ItemRequestMapper;

@Component
public class ItemMapper {

    private ItemRequestMapper itemRequestMapper;

    public ItemDto toItemDto(Item item) {
        return item == null ? null : ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest().getId())
                .build();
    }

    public Item toItem(ItemDto itemDto, Long owner) {
        return itemDto == null ? null : Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemRequestMapper.toItemRequest(itemDto.getRequest()))
                .owner(owner)
                .build();
    }

    public void updateItemDto(ItemDto itemDto, Item itemToUpdate, Long itemId) {
        itemDto.setName(itemDto.getName() != null ? itemDto.getName() : itemToUpdate.getName());
        itemDto.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : itemToUpdate.getDescription());
        itemDto.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : itemToUpdate.getAvailable());
        itemDto.setId(itemDto.getId() != null ? itemDto.getId() : itemId);
    }
}
