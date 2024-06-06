package ru.practicum.shareit.item.comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCreatedComments(Long itemId);

    List<Comment> getAllComments(Long itemId);

}
