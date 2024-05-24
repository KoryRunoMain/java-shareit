package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private CommentService commentService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final Long REQUEST_ID = 1L;

    //Users
    private final User user = new User(USER_ID, "user", "user@user.user");
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");
    //Items
    private final Item item = new Item(ITEM_ID, "item", "descriptionItem", true, null, user, null, null, null);
    private final ItemDto itemDto = new ItemDto(ITEM_ID, REQUEST_ID, "item", "descriptionItem", item.getAvailable(), null, null, user, null);
    private final Item updatedItem = new Item(ITEM_ID, "name", "description", true, null, user, null, null, null);
    private final ItemDto updatedItemDto = new ItemDto(ITEM_ID, REQUEST_ID, "name", "description", null, null, null, user, null);
    //Requests
    private final ItemRequest itemRequest = new ItemRequest(REQUEST_ID, "description", user, LocalDateTime.now(), List.of(item));
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(REQUEST_ID, "description", userDto, LocalDateTime.now(), List.of(itemDto));

    @BeforeEach
    void setUp() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(userService.getById(anyLong())).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(itemRequestMapper.toItemRequest(any(ItemRequestDto.class))).thenReturn(itemRequest);
        when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class))).thenReturn(itemRequestDto);
        lenient().when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        lenient().when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        lenient().when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
    }

    @Test
    void test_1_create_And_ReturnItem() {
        when(userService.getById(anyLong())).thenReturn(userDto);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        assertEquals(itemDto, itemService.create(itemDto, user.getId()));
        verify(itemRepository).save(item);
    }

    @Test
    void test_2_update_And_ReturnItem() {
        when(userService.getById(user.getId())).thenReturn(userDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(updatedItem));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(updatedItemDto);
        assertEquals(updatedItemDto, itemService.update(updatedItemDto, user.getId(), item.getId()));
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void test_3_getById_And_ReturnItem() {
        when(userService.getById(user.getId())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        assertEquals(itemDto, itemService.getById(item.getId()));
    }

    @Test
    void test_4_getAll_And_ReturnItem() {
        Page<Item> itemsPage = new PageImpl<>(List.of(item));
        Pageable pageable = PageRequest.of(0, 10);
        when(itemRepository.findAllByOwnerId(eq(user.getId()), eq(pageable))).thenReturn(itemsPage);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        List<ItemDto> responseList = itemService.getAll(user.getId(), 0, 10);
        List<ItemDto> expectedList = List.of(itemDto);
        assertEquals(expectedList, responseList);
    }

    @Test
    void test_5_search_And_ReturnItem() {
        String searchText = "descrip";
        Page<Item> itemsPage = new PageImpl<>(List.of(item));
        Pageable pageable = PageRequest.of(0, 10);
        when(itemRepository.searchItems(eq(searchText), eq(pageable))).thenReturn(itemsPage);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        List<ItemDto> responseList = itemService.search(searchText, 0, 10);
        List<ItemDto> expectedList = List.of(itemDto);
        assertEquals(expectedList, responseList);
    }

}
