package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Long REQUESTOR_ID = 1L;
    private static final Long NON_REQUESTOR_ID = 99L;
    private static final Long REQUEST_ID = 1L;
    private static final Long ITEM_ID = 1L;

    @BeforeEach
    void setUp() {
        User requestor = new User(REQUESTOR_ID, "requestor", "requestor@requestor.requestor");
        Item item = new Item(ITEM_ID, "Arduino", "Arduino", true, null, requestor, null, null, null);
        ItemRequest itemRequest = new ItemRequest(REQUEST_ID, "request", requestor, LocalDateTime.now(), List.of(item));
        userRepository.save(requestor);
        itemRepository.save(item);
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void whenFindAllByRequestorId_thenReturnsList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ItemRequest> result = itemRequestRepository.findAllByRequestorId(REQUESTOR_ID, pageable);
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(request -> request.getRequestor().getId().equals(REQUESTOR_ID));
    }

    @Test
    void whenFindAllByNonExistingRequestorId_thenReturnsEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);

        List<ItemRequest> result = itemRequestRepository.findAllByRequestorId(NON_REQUESTOR_ID, pageable);

        assertThat(result).isEmpty();
    }

    @Test
    void whenFindAllByRequestorIdIsNot_thenReturnsList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ItemRequest> result = itemRequestRepository.findAllByRequestorIdIsNot(REQUESTOR_ID, pageable);
        assertThat(result).isEmpty();
        assertThat(result).noneMatch(request -> request.getRequestor().getId().equals(REQUESTOR_ID));
    }

    @Test
    void whenFindAllByRequestorIdIsNot_withNonExistingId_thenReturnsAllRequests() {
        Pageable pageable = PageRequest.of(0, 10);

        List<ItemRequest> result = itemRequestRepository.findAllByRequestorIdIsNot(NON_REQUESTOR_ID, pageable);

        long totalRequests = itemRequestRepository.count();
        assertThat(result.size()).isEqualTo(totalRequests);
    }

}

