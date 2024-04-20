package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.isAvailable())
                .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

    public Item toItem(ItemDto itemDto, Long owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .isAvailable(itemDto.isAvailable())
                .owner(itemDto.getId())
                .request(itemDto.getRequest() != null ? itemDto.getRequest() : null)
                .build();
    }
}
