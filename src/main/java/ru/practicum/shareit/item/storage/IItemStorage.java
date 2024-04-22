package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface IItemStorage {

    Item create(Item item);

    Item update(Item item);

    Item delete(Long itemId);

    Item getItemId(Long id);

    void deleteItemsByOwner(Long owner);

    List<Item> getItemByOwner(Long owner);

    List<Item> getItemSearch(String text);
}
