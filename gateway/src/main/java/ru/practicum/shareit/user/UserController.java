package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Get-request getUserById: userId {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get-request getUsers");
        return userClient.getUsers();
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated @RequestBody UserDto userDto) {
        log.info("Post-request createUser: userDto {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                                             @PathVariable long userId) {
        log.info("Patch-request updateUser: userID {}, userDto {}", userId, userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        log.info("Delete-request deleteUser: userId {}", userId);
        userClient.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
