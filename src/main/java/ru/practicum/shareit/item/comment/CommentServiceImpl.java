package ru.practicum.shareit.item.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getAllCreatedComments(Long itemId) {
        if (itemId == null) {
            throw new NotFoundException("fail: ItemId Not Found");
        }
        List<Comment> comments = commentRepository.findAllByItemIdOrderByCreatedDesc(itemId);
        log.info("method: getAllCreatedComments |Request/Response|" + "itemDtoId:{} / comments:{}", itemId, comments);
        return comments;
    }

    @Override
    public List<Comment> getAllComments(Long itemId) {
        if (itemId == null) {
            throw new NotFoundException("fail: ItemId Not Found");
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        log.info("method: getAllComments |Request/Response|" + "itemDtoId:{} / comments:{}", itemId, comments);
        return comments;
    }

}
