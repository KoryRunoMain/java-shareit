package ru.practicum.shareit.item.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {

    private Long id;
    private String text;
    private ItemDto item;
    private String authorName;
    private LocalDateTime created;

}
