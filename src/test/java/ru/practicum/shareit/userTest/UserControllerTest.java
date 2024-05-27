package ru.practicum.shareit.userTest;

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
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private static final Long USER_ID = 1L;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");
    private final UserDto updatedUserDto = new UserDto(USER_ID, "user", "user@user.user");

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void test_1_createUser_And_ReturnStatusOk() throws Exception {
        when(service.create(any(UserDto.class))).thenReturn(userDto);
        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(userDto.getName())))
            .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        ArgumentCaptor<UserDto> userDtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(service).create(userDtoCaptor.capture());

        UserDto capturedUserDto = userDtoCaptor.getValue();
        assertThat(userDto.getId(), equalTo(capturedUserDto.getId()));
        assertThat(userDto.getName(), equalTo(capturedUserDto.getName()));
        assertThat(userDto.getEmail(), equalTo(capturedUserDto.getEmail()));
    }

    @Test
    void test_2_updateUser_And_ReturnStatusOk() throws Exception {
        when(service.update(any(UserDto.class), anyLong())).thenReturn(updatedUserDto);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(updatedUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updatedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(updatedUserDto.getEmail())));

        ArgumentCaptor<UserDto> userDtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(service).update(userDtoCaptor.capture(), userIdCaptor.capture());

        UserDto capturedUserDto = userDtoCaptor.getValue();
        assertThat(updatedUserDto.getId(), equalTo(capturedUserDto.getId()));
        assertThat(updatedUserDto.getName(), equalTo(capturedUserDto.getName()));
        assertThat(updatedUserDto.getEmail(), equalTo(capturedUserDto.getEmail()));
    }

    @Test
    void test_3_getUser_And_ReturnStatusOk() throws Exception {
        when(service.getById(eq(1L))).thenReturn(userDto);
        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(service).getById(eq(1L));
    }

    @Test
    void test_4_getAllUsers_And_ReturnStatusOk() throws Exception {
        when(service.getAll()).thenReturn(List.of(userDto));
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
        verify(service).getAll();
    }

    @Test
    void test_5_deleteUser_And_ReturnStatusOk() throws Exception {
        doNothing().when(service).delete(eq(1L));
        mvc.perform(delete("/users/1")).andExpect(status().isOk());
        verify(service).delete(eq(1L));
    }

}
