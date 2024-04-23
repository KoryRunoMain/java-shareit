package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Item {
    private Long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private Long owner;
    private Long request;
}
