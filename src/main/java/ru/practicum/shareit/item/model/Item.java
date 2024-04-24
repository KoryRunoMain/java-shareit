package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Item {
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private boolean isAvailable;

    private Long request;
    private Long owner;
}
