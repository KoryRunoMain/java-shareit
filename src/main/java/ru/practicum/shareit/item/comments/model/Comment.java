package ru.practicum.shareit.item.comments.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne
    private Item item;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne
    private User author;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

}
