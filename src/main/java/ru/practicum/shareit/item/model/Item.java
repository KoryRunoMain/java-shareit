package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Item {
    private Long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private Long owner;
    private Long request;
}
