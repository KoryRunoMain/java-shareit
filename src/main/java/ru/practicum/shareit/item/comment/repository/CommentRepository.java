package ru.practicum.shareit.item.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /*
    findAllBy ItemId OrderBy Created Desc
     */
    List<Comment> findAllByItemIdOrderByCreatedDesc(Long commentId);

    /*
    findAllBy ItemId
     */
    List<Comment> findAllByItemId(Long itemId);

}
