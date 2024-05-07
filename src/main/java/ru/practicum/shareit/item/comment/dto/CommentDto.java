package ru.practicum.shareit.item.comment.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {

    private Long id;

    @NotBlank
    private String text;

    private ItemDto item;

    private String authorName;

    private LocalDateTime created;

}
