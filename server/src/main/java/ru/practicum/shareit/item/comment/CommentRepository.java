package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /*
    findAllBy ItemId OrderBy Created Desc
     */
    List<Comment> findAllByItemIdOrderByCreated(Long itemId);

    /*
    findAllBy ItemId
     */
    List<Comment> findAllByItemId(Long itemId);

}
