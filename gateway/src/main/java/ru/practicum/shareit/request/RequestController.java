package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class RequestController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(OWNER_ID) Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Get-request getItemRequest: requestId={}, userId={}", requestId, userId);
        return requestClient.getById(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByOwner(@RequestHeader(OWNER_ID) Long userId) {
        log.info("Get-request getItemRequestsByOwner: userId={}", userId);
        return requestClient.getByOwner(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader(OWNER_ID) Long userId,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get-request getItemRequestsByUser: userId={}, from={}, size={}", userId, from, size);
        return requestClient.getByUser(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(OWNER_ID) Long userId,
                                                    @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Post-request createItemRequest: userId={}, description={}", userId, itemRequestDto);
        return requestClient.create(userId, itemRequestDto);
    }

}
