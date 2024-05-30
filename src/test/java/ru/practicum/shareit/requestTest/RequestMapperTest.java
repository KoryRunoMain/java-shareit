package ru.practicum.shareit.requestTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestMapperTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemRequestMapper itemRequestMapper;


    private static final Long REQUEST_ID = 1L;
    private static final Long USER_ID = 1L;

    private final User user = new User(USER_ID, "user", "user@user.user");
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");

    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(REQUEST_ID)
            .description("description")
            .requestor(user)
            .created(LocalDateTime.now().minusMinutes(60))
            .items(null)
            .build();
    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(REQUEST_ID)
            .description("description")
            .requestor(userDto)
            .created(LocalDateTime.now().minusMinutes(60))
            .items(null)
            .build();

    @Test
    void toItemRequestDto_successfully() {
        ItemRequestDto actRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        assertEquals(actRequestDto.getId(), itemRequestDto.getId());
        assertEquals(actRequestDto.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    void toItemRequest_successfully() {
        ItemRequest actRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        assertEquals(actRequest.getId(), itemRequest.getId());
        assertEquals(actRequest.getDescription(), itemRequest.getDescription());
    }

}
