package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getUserItemRequests: userId={}, from={}, size={}", userId, from, size);
        return requestClient.getRequests(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getAllItemRequests: userId={}, from={}, size={}", userId, from, size);
        return requestClient.getRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getItemRequest: requestId={}, userId={}", requestId, userId);
        return requestClient.getRequest(requestId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@Validated @RequestBody ItemRequestDto itemRequestDto,
                                                    @RequestHeader(OWNER_ID) Long userId) {
        log.info("Post-request createItemRequest: userId={}, description={}", userId, itemRequestDto);
        return requestClient.createRequest(userId, itemRequestDto);
    }

}
