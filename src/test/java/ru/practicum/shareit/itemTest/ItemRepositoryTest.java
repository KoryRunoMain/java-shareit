package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Long OWNER_ID = 1L;
    private static final Long NON_OWNER_ID = 99L;
    private static final Long ITEM_ID = 1L;
    private static final String SEARCH_TEXT = "Arduino";
    private static final String NOT_FOUND_TEXT = "NotExist";

    private final User user = new User(OWNER_ID, "user", "user@user.user");
    private final Item item = new Item(ITEM_ID, "Arduino", "Arduino", true, null, user, null, null, null);
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        itemRepository.save(item);
    }

    @Test
    void searchItems_successfullyFindItems() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> itemList = itemRepository.searchItems(SEARCH_TEXT, pageable);
        assertTrue(itemList.hasContent());
    }

    @Test
    void searchItems_NotFoundRequest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> itemList = itemRepository.searchItems(NOT_FOUND_TEXT, pageable);
        assertEquals(0, itemList.getTotalElements());
    }

    @Test
    void findAllByOwnerId_successfullyFindList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> itemList = itemRepository.findAllByOwnerId(OWNER_ID, pageable);
        assertTrue(itemList.hasContent());
    }

    @Test
    void findAllByNotExistingOwnerId_ReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> itemList = itemRepository.findAllByOwnerId(NON_OWNER_ID, pageable);
        assertEquals(0, itemList.getTotalElements());
    }

    @Test
    void findAllByItemRequest_successfullyFindList() {
        List<Item> itemList = itemRepository.findAllByItemRequest(itemRequest);
        assertFalse(itemList.isEmpty());
    }

    @Test
    void findAllByNonExistingItemRequest_ReturnEmptyList() {
        Long nonExistentItemRequestId = 10L;
        ItemRequest nonExistingItemRequest = new ItemRequest();
        nonExistingItemRequest.setId(nonExistentItemRequestId);
        List<Item> itemList = itemRepository.findAllByItemRequest(nonExistingItemRequest);
        assertTrue(itemList.isEmpty());
    }

}
