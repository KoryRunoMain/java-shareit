package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.FrontController;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController extends FrontController {

    private static final String OWNER_ID = "X-Sharer-User-Id";
    private ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.get(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemByOwner(@RequestHeader(OWNER_ID) Long owner) {
        return itemService.getItemByOwner(owner);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemSearch(@RequestParam String text) {
        return itemService.getItemSearch(text);
    }

    @PostMapping
    public ItemDto create(@Validated @RequestBody ItemDto item,
                                    @RequestHeader(OWNER_ID) Long userId) {
        return itemService.create(item, userId);
    }

    @PostMapping("/{itemId}")
    public ItemDto delete(@PathVariable Long itemId,
                          @RequestHeader(OWNER_ID) Long owner) {
        return itemService.delete(itemId, owner);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Validated @RequestBody ItemDto item,
                          @PathVariable Long itemId,
                          @RequestHeader(OWNER_ID) Long ownerId) {
        return itemService.update(item, itemId, ownerId);
    }
}
