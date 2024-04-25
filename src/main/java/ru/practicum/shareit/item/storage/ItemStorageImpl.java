package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private Map<Long, Item> items;
    private Long id;

    public ItemStorageImpl() {
        id = 0L;
        items = new HashMap<>();
    }

    private Long createId() {
        return ++id;
    }

    @Override
    public Item create(Item item) {
        Long id = createId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(),item);
        return item;
    }

    @Override
    public Item delete(Long itemId) {
        return items.remove(itemId);
    }

    @Override
    public Item getItemId(Long id) {
        return items.get(id);
    }

    @Override
    public void deleteItemsByOwner(Long owner) {
        items.values().removeIf(item -> item.getOwner().equals(owner));
    }

    @Override
    public List<Item> getItemByOwner(Long owner) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(owner))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemSearch(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}
