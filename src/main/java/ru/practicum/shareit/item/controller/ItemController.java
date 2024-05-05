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
    public ItemDto getItem(@PathVariable Long itemId,
                       @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getItem: itemId {}", itemId);
        return service.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getAllItems: userId {}", userId);
        return service.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("Get-request getItemSearch: text {}", text);
        return service.search(text);
    }

    @PostMapping
    public ItemDto createItem(@Validated @RequestBody ItemDto itemDto,
                              @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request create: userId {}, itemDto {}", userId, itemDto);
        return service.create(itemDto, userId);
    }

    @PostMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId,
                           @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request delete: userId {}, itemId {}", userId, itemId);
        service.delete(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto saveItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader(OWNER_ID) Long userId) {
        log.info("Patch-request update: userId {}, itemId {}, itemDto {}", userId, itemId, itemDto);
        return service.save(itemDto, itemId, userId);
    }

    // POST /items/{itemId}/comment

}
