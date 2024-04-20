package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    private Long id;

    @NotBlank(message = "поле не может быть пустым")
    private String name;
    @NotBlank(message = "поле не может быть пустым")
    private String description;
    @NotNull(message = "поле не может быть пустым")
    private boolean isAvailable;

    private Long request;
}
