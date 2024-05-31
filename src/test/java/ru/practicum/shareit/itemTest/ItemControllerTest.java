package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final Long COMMENT_ID = 1L;
    private static final Long REQUEST_ID = 1L;
    private static final Long WRONG_ID = 100L;

    private final User user = new User(USER_ID, "user", "user@user.user");
    private final ItemDto itemDto = new ItemDto(ITEM_ID, null, "item", "description", true, null, null, user, null);
    private final ItemDto wrongItemDto = new ItemDto(ITEM_ID, REQUEST_ID, "", "description", false, null, null, user, null);
    private final CommentDto commentDto = new CommentDto(COMMENT_ID, "text", itemDto, "author", LocalDateTime.now());
    private final CommentDto wrongCommentDto = new CommentDto(COMMENT_ID, "", itemDto, "author", LocalDateTime.now());

    @Test
    void createItem_successfullyCreated() throws Exception {
        when(itemService.create(any(), anyLong())).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ArgumentCaptor<ItemDto> itemDtoCaptor = ArgumentCaptor.forClass(ItemDto.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(itemService).create(itemDtoCaptor.capture(), userIdCaptor.capture());

        ItemDto capturedItemDto = itemDtoCaptor.getValue();
        assertThat(capturedItemDto.getId(), equalTo(itemDto.getId()));
        assertThat(capturedItemDto.getName(), equalTo(itemDto.getName()));
        assertThat(capturedItemDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(capturedItemDto.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(capturedItemDto.getOwner(), equalTo(itemDto.getOwner()));
        assertThat(capturedItemDto.getRequestId(), equalTo(itemDto.getRequestId()));
        assertThat(capturedItemDto.getComments(), equalTo(itemDto.getComments()));
        assertThat(capturedItemDto.getLastBooking(), equalTo(itemDto.getLastBooking()));
        assertThat(capturedItemDto.getNextBooking(), equalTo(itemDto.getNextBooking()));
        Long capturedUserId = userIdCaptor.getValue();
        assertThat(capturedUserId, equalTo(USER_ID));
    }

    @Test
    void createItem_badRequestExistingName() throws Exception {
        when(itemService.create(any(), anyLong())).thenReturn(wrongItemDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(wrongItemDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, never()).create(wrongItemDto, USER_ID);
    }

    @Test
    void createItem_BadRequestWrongItem() throws Exception {
        when(itemService.create(any(), anyLong())).thenReturn(wrongItemDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(wrongItemDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, never()).create(wrongItemDto, USER_ID);
    }

    @Test
    void updateItem_successfullyUpdated() throws Exception {
        ItemDto updatedItemDto = ItemDto.builder()
                .id(1L)
                .name("updatedItem")
                .description("updatedDescription")
                .available(true)
                .owner(user)
                .requestId(null)
                .comments(null)
                .lastBooking(null)
                .nextBooking(null)
                .build();

        when(itemService.update(any(), eq(1L), eq(1L))).thenReturn(updatedItemDto);
        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(updatedItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updatedItem")))
                .andReturn();

        ArgumentCaptor<ItemDto> itemDtoCaptor = ArgumentCaptor.forClass(ItemDto.class);
        ArgumentCaptor<Long> itemIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(itemService).update(itemDtoCaptor.capture(), itemIdCaptor.capture(), userIdCaptor.capture());

        ItemDto capturedItemDto = itemDtoCaptor.getValue();
        Long capturedItemId = itemIdCaptor.getValue();
        Long capturedUserId = userIdCaptor.getValue();

        assertThat(capturedItemDto.getId(), equalTo(updatedItemDto.getId()));
        assertThat(capturedItemDto.getName(), equalTo(updatedItemDto.getName()));
        assertThat(capturedItemDto.getDescription(), equalTo(updatedItemDto.getDescription()));
        assertThat(capturedItemDto.getAvailable(), equalTo(updatedItemDto.getAvailable()));
        assertThat(capturedItemDto.getOwner(), equalTo(updatedItemDto.getOwner()));
        assertThat(capturedItemDto.getRequestId(), equalTo(updatedItemDto.getRequestId()));
        assertThat(capturedItemDto.getComments(), equalTo(updatedItemDto.getComments()));
        assertThat(capturedItemDto.getLastBooking(), equalTo(updatedItemDto.getLastBooking()));
        assertThat(capturedItemDto.getNextBooking(), equalTo(updatedItemDto.getNextBooking()));
        assertThat(capturedItemId, equalTo(ITEM_ID));
        assertThat(capturedUserId, equalTo(USER_ID));
    }

    @Test
    void getItem_successfullyGet() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).getItemById(eq(ITEM_ID), eq(itemDto.getId()));
    }

    @Test
    void getItem_notFoundItemId() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenThrow(new NotFoundException(""));
        mvc.perform(get("/items/{id}", WRONG_ID)
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void getAllItems_successfullyGetList() throws Exception {
        when(itemService.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].owner.id", is(itemDto.getOwner().getId().intValue())))
                .andExpect(jsonPath("$[0].owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("$[0].owner.email", is(itemDto.getOwner().getEmail())));
        verify(itemService).getAll(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getAllItems_notFoundUserExist() throws Exception {
        when(itemService.getAll(anyLong(), anyInt(), anyInt())).thenThrow(new NotFoundException(""));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", WRONG_ID))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, never()).getAll(WRONG_ID, 1, 1);
    }

    @Test
    void searchItem_successfullyFindItem() throws Exception {
        when(itemService.search(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search")
                        .param("text", "des")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("item")))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder("description")));
        verify(itemService).search(anyString(), anyInt(), anyInt());
    }

    @Test
    void createComment_successfullyCreated() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any())).thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", USER_ID)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).createComment(anyLong(), anyLong(), any());
    }

    @Test
    void createComment_badRequestEmptyText() throws Exception {
        when(itemService.createComment(any(), anyLong(), any())).thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", USER_ID)
                        .content(mapper.writeValueAsString(wrongCommentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
