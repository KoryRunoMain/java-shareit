package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.ItemController;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final Long COMMENT_ID = 1L;

    @MockBean
    private ItemService service;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private final User user = new User(USER_ID, "user", "user@user.user");
    private final ItemDto itemDto = new ItemDto(ITEM_ID, null, "item", "description", true, null, null, user, null);
    private final CommentDto commentDto = new CommentDto(COMMENT_ID, "text", itemDto, "author", LocalDateTime.now());

    @Test
    void test_1_createItem_And_ReturnStatusOk() throws Exception {
        when(service.create(any(), anyLong())).thenReturn(itemDto);
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
        verify(service).create(itemDtoCaptor.capture(), userIdCaptor.capture());

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
        assertThat(capturedUserId, equalTo(1L));
    }

    @Test
    void test_4_updateItem_And_ReturnedStatusOk() throws Exception {
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

        when(service.update(any(), eq(1L), eq(1L))).thenReturn(updatedItemDto);
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
        verify(service).update(itemDtoCaptor.capture(), itemIdCaptor.capture(), userIdCaptor.capture());

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
        assertThat(capturedItemId, equalTo(1L));
        assertThat(capturedUserId, equalTo(1L));
    }

    @Test
    void test_2_getItem_And_ReturnStatusOk() throws Exception {
        when(service.getItemById(anyLong(), anyLong())).thenReturn(itemDto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(service).getItemById(eq(1L), eq(itemDto.getId()));
    }

    @Test
    void test_3_getAllItems_And_ReturnedStatusOk() throws Exception {
        when(service.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
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
        verify(service).getAll(anyLong(), anyInt(), anyInt());
    }

    @Test
    void test_5_searchItem_And_ReturnedStatusOk() throws Exception {
        when(service.search(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search")
                        .param("text", "des")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("item")))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder("description")));
        verify(service).search(anyString(), anyInt(), anyInt());
    }

    @Test
    void test_6_createComment_And_ReturnedStatusOk() throws Exception {
        when(service.createComment(anyLong(), anyLong(), any())).thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(service).createComment(anyLong(), anyLong(), any());
    }

}
