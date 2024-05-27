package ru.practicum.shareit.requestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {

    @Mock
    ItemRequestService service;

    @InjectMocks
    ItemRequestController controller;

    private static final Long REQUEST_ID = 1L;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(REQUEST_ID).description("description").build();
    private final ItemRequestDto itemRequestDto2 = ItemRequestDto.builder().id(REQUEST_ID).description("description").build();
    private final List<ItemRequestDto> itemRequestDtoList = Arrays.asList(itemRequestDto, itemRequestDto2);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void test_1_createItemRequest_And_ReturnStatusOk() throws Exception {
        when(service.create(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestor", is(itemRequestDto.getRequestor())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(itemRequestDto.getItems())));

        ArgumentCaptor<ItemRequestDto> itemDtoCaptor = ArgumentCaptor.forClass(ItemRequestDto.class);
        ArgumentCaptor<Long> itemIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(service).create(itemIdCaptor.capture(), itemDtoCaptor.capture());

        ItemRequestDto capturedItemRequestDto = itemDtoCaptor.getValue();
        assertThat(itemRequestDto.getId(), equalTo(capturedItemRequestDto.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(capturedItemRequestDto.getDescription()));
        assertThat(itemRequestDto.getRequestor(), equalTo(capturedItemRequestDto.getRequestor()));
        assertThat(itemRequestDto.getCreated(), equalTo(capturedItemRequestDto.getCreated()));
        assertThat(itemRequestDto.getItems(), equalTo(capturedItemRequestDto.getItems()));
    }

    @Test
    void test_2_getItemRequestById_And_ReturnStatusOk() throws Exception {
        when(service.getById(anyLong(), anyLong())).thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestor", is(itemRequestDto.getRequestor())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(itemRequestDto.getItems())));
        verify(service).getById(eq(1L), eq(1L));
    }

    @Test
    void test_3_getAllItemRequests_And_ReturnStatusOk() throws Exception {
        when(service.getAllItemRequests(anyLong(), anyInt(), anyInt())).thenReturn(itemRequestDtoList);
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(itemRequestDtoList.size())))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[1].id", is(itemRequestDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(itemRequestDto2.getDescription())));
        verify(service).getAllItemRequests(1L, 0, 10);
    }

    @Test
    void test_4_getOwnItemRequests_And_ReturnStatusOk() throws Exception {
        when(service.getOwnItemRequests(anyLong(), anyInt(), anyInt())).thenReturn(itemRequestDtoList);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(itemRequestDtoList.size())))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[1].id", is(itemRequestDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(itemRequestDto2.getDescription())));
        verify(service).getOwnItemRequests(1L, 0, 10);
    }

}
