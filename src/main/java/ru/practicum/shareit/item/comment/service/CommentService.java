package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCreatedComments(Long itemId);

    List<Comment> getAllComments(Long itemId);

    void saveComment(Comment comment);

}
