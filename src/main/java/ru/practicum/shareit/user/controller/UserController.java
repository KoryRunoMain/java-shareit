package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private UserService userService;
    private ItemService itemService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Get-request getUser: userId {}", userId);
        return userService.get(userId);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Get-request getUsers");
        return userService.getUsers();
    }

    @ResponseBody
    @PostMapping
    public UserDto createItem(@Validated @RequestBody UserDto userDto) {
        log.info("Post-request createItem: userDto {}", userDto);
        return userService.create(userDto);
    }

    @ResponseBody
    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Patch-request update: userID {}, userDto {}", userId, userDto);
        return userService.update(userDto, userId);
    }

    @Transactional
    @DeleteMapping("/{userId}")
    public UserDto delete(@PathVariable Long userId) {
        log.info("Delete-request delete: userId {}", userId);
        itemService.deleteItemsByOwner(userId);
        return userService.delete(userId);
    }
}
