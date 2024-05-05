package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private UserService service;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Get-request getUser: userId {}", userId);
        return service.getById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Get-request getUsers");
        return service.getAll();
    }

    @ResponseBody
    @PostMapping
    public UserDto createUser(@Validated @RequestBody UserDto userDto) {
        log.info("Post-request createItem: userDto {}", userDto);
        return service.create(userDto);
    }

    @ResponseBody
    @PatchMapping("/{userId}")
    public UserDto saveUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Patch-request update: userID {}, userDto {}", userId, userDto);
        return service.save(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Delete-request delete: userId {}", userId);
        service.delete(userId);
    }
}
