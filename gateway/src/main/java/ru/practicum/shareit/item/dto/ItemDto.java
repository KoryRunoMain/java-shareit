package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
