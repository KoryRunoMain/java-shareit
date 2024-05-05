package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private ItemService service;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        log.info("Get-request getItem: itemId {}", itemId);
        return service.findItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemByOwner(@RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getItemByOwner: userId {}", userId);
        return service.findItemByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemSearch(@RequestParam String text) {
        log.info("Get-request getItemSearch: text {}", text);
        return service.searchItem(text);
    }

    @PostMapping
    public ItemDto createItem(@Validated @RequestBody ItemDto itemDto, @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request create: userId {}, itemDto {}", userId, itemDto);
        return service.createItem(itemDto, userId);
    }

    @PostMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request delete: userId {}, itemId {}", userId, itemId);
        service.deleteItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                              @RequestHeader(OWNER_ID) Long userId) {
        log.info("Patch-request update: userId {}, itemId {}, itemDto {}", userId, itemId, itemDto);
        return service.saveItem(itemDto, itemId, userId);
    }
}
