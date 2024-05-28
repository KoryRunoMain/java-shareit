package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
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
    private CommentRepository commentRepository;
    @Mock
    private CommentService commentService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private CommentMapper commentMapper;

    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final Long ITEM_ID_2 = 2L;
    private static final Long REQUEST_ID = 1L;
    private static final Long REQUEST_ID_2 = 2L;
    private static final Long BOOKING_ID = 1L;
    private static final Long COMMENT_ID = 1L;
    private static final Long WRONG_USER_ID = 10L;
    private static final Long WRONG_ITEM_ID = 10L;

    //Users
    private final User user = new User(USER_ID, "user", "user@user.user");
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");
    //Items
    private final Item item = new Item(ITEM_ID, "item", "descriptionItem", true, null, user, null, null, null);
    private final Item item2 = new Item(ITEM_ID_2, "item", "descriptionItem", true, null, user, null, null, null);
    private final ItemDto itemDto = new ItemDto(ITEM_ID, REQUEST_ID_2, "item", "descriptionItem", true, null, null, user, null);
    private final ItemDto itemDto2 = new ItemDto(ITEM_ID_2, REQUEST_ID, "item", "descriptionItem", true, null, null, user, null);
    private final Item updatedItem = new Item(ITEM_ID, "name", "description", true, null, user, null, null, null);
    private final ItemDto updatedItemDto = new ItemDto(ITEM_ID, REQUEST_ID, "name", "description", null, null, null, user, null);
    //Requests
    private final ItemRequest itemRequest = new ItemRequest(REQUEST_ID, "description", user, LocalDateTime.now(), List.of(item));
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(REQUEST_ID, "description", userDto, LocalDateTime.now(), List.of(itemDto));
    //Bookings
    private final Booking booking = new Booking(BOOKING_ID, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
    private final List<Booking> bookingList = List.of(booking);
    //Comments
    private final Comment comment = new Comment(COMMENT_ID, "comment", item, user, LocalDateTime.now().minusMinutes(60));
    private final CommentDto commentDto = CommentDto.builder().id(COMMENT_ID).text("comment").item(itemDto).authorName("user").build();

    @Test
    void test_1_create_And_ReturnItem() {
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        assertEquals(itemDto, itemService.create(itemDto, user.getId()));
        verify(itemRepository).save(item);
    }

    @Test
    void test_2_createWithUserNotExist_And_ReturnException() {
        when(userService.getById(anyLong())).thenThrow(new NotFoundException("fail: user/owner ID Not Found!"));
        Exception exception = assertThrows(NotFoundException.class, () -> itemService.create(itemDto, WRONG_USER_ID));
        assertEquals(exception.getMessage(), "fail: user/owner ID Not Found!");
    }

    @Test
    void test_3_createWithNotFoundItemRequest_And_ReturnException() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemService.getItemRequest(itemDto2)).thenThrow(new NotFoundException("fail: itemRequestId Not Found!"));
        Exception exception = assertThrows(NotFoundException.class, () -> itemService.getItemRequest(itemDto2));
        assertEquals(exception.getMessage(), "fail: itemRequestId Not Found!");
    }

    @Test
    void test_4_update_And_ReturnItem() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(updatedItem));
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(updatedItemDto);
        assertEquals(updatedItemDto, itemService.update(updatedItemDto, user.getId(), item.getId()));
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void test_5_updateWithNotFoundItem_And_ReturnException() {
        when(itemRepository.findById(anyLong())).thenThrow(new NotFoundException("itemId not Found!"));
        Exception exception = assertThrows(NotFoundException.class, () -> itemService.update(itemDto, WRONG_ITEM_ID, USER_ID));
        assertEquals(exception.getMessage(), "itemId not Found!");
    }

    @Test
    void test_6_updateUserNotEqualOwner_And_ReturnException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Exception exception = assertThrows(ValidationException.class, () -> itemService.update(itemDto, ITEM_ID, WRONG_USER_ID));
        assertEquals(exception.getMessage(), "fail: ownerId and userId is not equals!");
    }

    @Test
    void test_7_getById_And_ReturnItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        assertEquals(itemDto, itemService.getById(item.getId()));
    }

    @Test
    void test_8_getByIdWithNotFoundItem_And_ReturnException() {
        when(itemRepository.findById(anyLong())).thenThrow(new NotFoundException("itemId not Found!"));
        Exception exception = assertThrows(NotFoundException.class, () -> itemService.getItemById(WRONG_ITEM_ID, USER_ID));
        assertEquals(exception.getMessage(), "itemId not Found!");
    }

    @Test
    void test_9_getAll_And_ReturnItem() {
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        Page<Item> itemsPage = new PageImpl<>(List.of(item));
        Pageable pageable = PageRequest.of(0, 10);
        when(itemRepository.findAllByOwnerId(eq(user.getId()), eq(pageable))).thenReturn(itemsPage);
        List<ItemDto> responseList = itemService.getAll(user.getId(), 0, 10);
        List<ItemDto> expectedList = List.of(itemDto);
        assertEquals(expectedList, responseList);
    }

    @Test
    void test_10_search_And_ReturnItem() {
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        String searchText = "descrip";
        Page<Item> itemsPage = new PageImpl<>(List.of(item));
        Pageable pageable = PageRequest.of(0, 10);
        when(itemRepository.searchItems(eq(searchText), eq(pageable))).thenReturn(itemsPage);
        List<ItemDto> responseList = itemService.search(searchText, 0, 10);
        List<ItemDto> expectedList = List.of(itemDto);
        assertEquals(expectedList, responseList);
    }

    @Test
    void test_11_searchWithEmptyText_And_ReturnEmptyList() {
        String searchText = "";
        assertThat(itemService.search(searchText, 0, 10), hasSize(0));
        assertThat(itemService.search(null, 0, 10), hasSize(0));
        when(itemRepository.searchItems(anyString(),any())).thenReturn(Page.empty());
        assertEquals(itemService.search("", 0, 10), Collections.EMPTY_LIST);
    }

    @Test
    void test_12_createComment() {
        when(commentMapper.toCommentDto(any())).thenReturn(commentDto);
        when(commentMapper.toComment(any())).thenReturn(comment);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userService.getById(anyLong())).thenReturn(userDto);
        when(bookingRepository.findByItemIdAndBookerIdAndStatusAndEndIsBefore(anyLong(), anyLong(), any(), any())).thenReturn(bookingList);
        when(commentRepository.save(any())).thenReturn(comment);
        when(commentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        CommentDto testComment = itemService.createComment(ITEM_ID, USER_ID, commentDto);
        assertEquals(testComment.getId(), commentDto.getId());
        assertEquals(testComment.getItem(), commentDto.getItem());
        assertEquals(testComment.getText(), commentDto.getText());
        assertEquals(testComment.getAuthorName(), commentDto.getAuthorName());
    }

    @Test
    void test_13_createEmptyComment_And_ReturnException() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("");
        assertThrows(ValidationException.class, () -> {
            itemService.createComment(ITEM_ID, USER_ID, commentDto);
        });
    }

}
