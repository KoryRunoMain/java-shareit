package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getItem: itemId {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size,
                                              @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getAllItems: userId={}, from={}, size={}", userId, from, size);
        return itemClient.getItems(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@Validated @RequestBody ItemDto itemDto,
                                             @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request create: userId {}, itemDto {}", userId, itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable Long itemId,
                                             @RequestHeader(OWNER_ID) Long userId) {
        log.info("Patch-request update: userId {}, itemId {}, itemDto {}", userId, itemId, itemDto);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size,
                                             @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getItemSearch: userId {}, text={}, from={}, size={}", userId, text, from, size);
        return itemClient.searchItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
                                                @Validated @RequestBody CommentDto commentDto,
                                                @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request comment: itemId {}, userId {}, text {}", itemId, userId, commentDto);
        return itemClient.createComment(itemId, userId, commentDto);
    }

}
