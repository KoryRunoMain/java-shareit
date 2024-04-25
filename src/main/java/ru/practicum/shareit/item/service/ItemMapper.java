package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return item == null ? null : ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.isAvailable())
                .request(item.getRequest())
                .build();
    }

    public Item toItem(ItemDto itemDto, Long owner) {
        return itemDto == null ? null : Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .isAvailable(itemDto.isAvailable())
                .request(itemDto.getRequest())
                .owner(owner)
                .build();
    }
}
