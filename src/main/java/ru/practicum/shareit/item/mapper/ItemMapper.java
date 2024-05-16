package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return item == null ? null : ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getItemRequest() != null ? item.getItemRequest().getId() : null)
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        return itemDto == null ? null : Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public void updateItemDto(ItemDto itemDto, Item itemToUpdate) {
        itemToUpdate.setName(Optional.ofNullable(itemDto.getName())
                .orElse(itemToUpdate.getName()));
        itemToUpdate.setDescription(Optional.ofNullable(itemDto.getDescription())
                .orElse(itemToUpdate.getDescription()));
        itemToUpdate.setAvailable(Optional.ofNullable(itemDto.getAvailable())
                .orElse(itemToUpdate.getAvailable()));
    }

}