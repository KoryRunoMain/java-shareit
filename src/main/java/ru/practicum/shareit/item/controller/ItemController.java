package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final ItemService service;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getItem: itemId {}", itemId);
        return service.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(OWNER_ID) Long userId,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get-request getAllItems: userId={}, from={}, size={}", userId, from, size);
        return service.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text,
                                    @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                    @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get-request getItemSearch: text={}, from={}, size={}", text, from, size);
        return service.search(text, from, size);
    }

    @PostMapping
    public ItemDto createItem(@Validated @RequestBody ItemDto itemDto,
                              @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request create: userId {}, itemDto {}", userId, itemDto);
        return service.create(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId,
                                    @Validated @RequestBody CommentDto commentDto,
                                    @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request comment: itemId {}, userId {}, text {}", itemId, userId, commentDto);
        return service.createComment(itemId, userId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader(OWNER_ID) Long userId) {
        log.info("Patch-request update: userId {}, itemId {}, itemDto {}", userId, itemId, itemDto);
        return service.save(itemDto, itemId, userId);
    }

}
