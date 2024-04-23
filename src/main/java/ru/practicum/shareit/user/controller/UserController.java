package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@CrossOrigin({
        "http://localhost:5173/",
        "http://127.0.0.1:5173/"})
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private UserService userService;
    private ItemService itemService;

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Получен Get-запрос к /users");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Получен Get-запрос к /users/{}", userId);
        return userService.getUserById(userId);
    }

    @ResponseBody
    @PostMapping
    public UserDto createUser(@Validated @RequestBody UserDto userDto) {
        log.info("Получен Post-запрос к /users, userDto: {}", userDto);
        return userService.create(userDto);
    }

    @ResponseBody
    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long userId) {
        log.info("Получен Patch-запрос к /users/{}", userId);
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable Long userId) {
        log.info("Получен Delete-запрос к /users/{}", userId);
        itemService.deleteItemsByOwner(userId);
        return userService.delete(userId);
    }
}
