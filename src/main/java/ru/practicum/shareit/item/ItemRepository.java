package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /*
    search Items
     */
    @Query("SELECT i FROM Item i " +
            "WHERE (upper(i.name) LIKE upper(concat('%', ?1, '%')) " +
            "OR upper(i.description) LIKE upper(concat('%', ?1, '%'))) " +
            "AND i.available = true")
    Page<Item> searchItems(String text, Pageable pageRequest);

    /*
    findAllBy OwnerId
     */
    Page<Item> findAllByOwnerId(Long ownerId, Pageable pageRequest);

    /*
    findAllBy ItemRequest
     */
    List<Item> findAllByItemRequest(ItemRequest itemRequest);

}
