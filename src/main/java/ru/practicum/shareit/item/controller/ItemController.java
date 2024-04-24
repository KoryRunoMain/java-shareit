package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@CrossOrigin({
        "http://localhost:5173/",
        "http://127.0.0.1:5173/"})
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String OWNER_ID = "X-Sharer-User-Id";
    private ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.get(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemByOwner(@RequestHeader(OWNER_ID) Long userId) {
        return itemService.getItemByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemSearch(@RequestParam String text) {
        return itemService.getItemSearch(text);
    }

    @PostMapping
    public ItemDto create(@Validated @RequestBody ItemDto itemDto,
                                    @RequestHeader(OWNER_ID) Long userId) {
        return itemService.create(itemDto, userId);
    }

    @PostMapping("/{itemId}")
    public ItemDto delete(@PathVariable Long itemId,
                          @RequestHeader(OWNER_ID) Long userId) {
        return itemService.delete(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Validated @RequestBody ItemDto itemDto,
                          @PathVariable Long itemId,
                          @RequestHeader(OWNER_ID) Long userId) {
        return itemService.update(itemDto, itemId, userId);
    }
}
