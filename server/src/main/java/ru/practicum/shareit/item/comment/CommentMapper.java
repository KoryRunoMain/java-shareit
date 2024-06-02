package ru.practicum.shareit.item.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;

@Component
@AllArgsConstructor
public class CommentMapper {
    private ItemMapper itemMapper;

    public CommentDto toCommentDto(Comment comment) {
        return comment == null ? null : CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(itemMapper.toItemDto(comment.getItem()))
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public Comment toComment(CommentDto commentDto) {
        return commentDto == null ? null : Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

}
