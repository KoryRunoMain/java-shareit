package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

/*
    TODO MB NEXT SPRINT
*/

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {
    private String description;
    private String requestor;
    private LocalDateTime created;
}