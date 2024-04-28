package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/*
    TODO MB NEXT SPRINT
 */

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequest {
    private Long id;
    private String description;
    private String requestor;
    private LocalDateTime created;
}
