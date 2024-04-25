package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {

    private String description;

    private String requestor;

    private LocalDateTime created;

}