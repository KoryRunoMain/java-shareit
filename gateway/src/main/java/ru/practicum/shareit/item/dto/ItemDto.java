package ru.practicum.shareit.item.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class ItemDto {

    public Long id;

    public Long requestId;

    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotNull
    public Boolean available;

}
