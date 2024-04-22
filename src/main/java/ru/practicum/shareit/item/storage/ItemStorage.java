package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.IItemStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class ItemStorage implements IItemStorage {

    private Map<Long, Item> items;
    private Long id;

    public ItemStorage() {
        items = new HashMap<>();
        id = 0L;
    }

    @Override
    public Item create(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        log.info("create: itemId {}", item.getId());
        return item;
    }

    @Override
    public Item update(Item item) {
        Item updateItem = items.put(item.getId(),item);
        assert updateItem != null;
        log.info("update: itemId {}", updateItem.getId());
        return updateItem;
    }

    @Override
    public Item delete(Long itemId) {
        Item item = items.get(itemId);
        items.remove(itemId);
        log.info("delete: itemId {}", item);
        return item;
    }

    @Override
    public Item getItemId(Long id) {
        Item item = items.get(id);
        log.info("getItemId {}", item);
        return item;
    }

    @Override
    public void deleteItemsByOwner(Long owner) {
        items.values().removeIf(item -> item.getOwner().equals(owner));
        log.info("deleteItemsByOwner {} complete", owner);
    }

    @Override
    public List<Item> getItemByOwner(Long owner) {
        List<Item> getItemByOwner = items.values().stream()
                .filter(item -> item.getOwner().equals(owner))
                .collect(Collectors.toList());
        log.info("getItemByOwner {}", getItemByOwner);
        return getItemByOwner;
    }

    @Override
    public List<Item> getItemSearch(String text) {
        List<Item> getSearch = items.values().stream()
                .filter(Item::isAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase())
                                ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
        log.info("getSearch {}", getSearch);
        return getSearch;
    }
}
