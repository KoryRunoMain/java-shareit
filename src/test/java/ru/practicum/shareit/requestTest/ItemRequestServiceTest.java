package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ItemRepository itemRepository;

    private final User user = new User(1L, "User", "user@user.user");
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("description")
            .requestor(user)
            .created(LocalDateTime.now())
            .build();

    private final UserDto userDto = new UserDto(1L, "User", "user@user.user");
    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("description")
            .requestor(userDto)
            .created(LocalDateTime.now())
            .build();

    @Test
    void test_1_findAllRequestsWhenParamsIsExistAfterReturnedExpectedListRequests() {
        Mockito.when(userService.getById(anyLong()))
                .thenReturn(userDto);
        Mockito.when(itemRepository.findAllByItemRequest(any()))
                .thenReturn(new ArrayList<>());
        Mockito.when(itemRequestRepository.findAllByRequestorIdIsNot(anyLong(), any()))
                .thenReturn(List.of(itemRequest));

        assertEquals(itemRequestService.getAllItemRequests(1L, 1, 1), List.of(itemRequestDto));
    }

}
