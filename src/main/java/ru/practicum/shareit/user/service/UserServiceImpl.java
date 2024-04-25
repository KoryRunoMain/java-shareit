package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private UserStorage userStorage;
    private UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        User newUser = userMapper.toUser(userDto);
        validateCreateUser(userDto);
        checkUserExists(userDto.getEmail());
        UserDto createdUserDto = userMapper.toUserDto(userStorage.create(newUser));
        log.info("create.Ok!");
        return createdUserDto;
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User updateUser = userStorage.get(userId);
        if (updateUser == null) {
            throw new NotFoundException("Not Found!");
        }
        userStorage.getUsers().stream()
                .filter(u -> !u.getId().equals(userId) && u.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(user -> {
                    throw new AlreadyExistsException("Email уже занят");
                });
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty() && userDto.getEmail().contains("@")) {
            updateUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            updateUser.setName(userDto.getName());
        }
        return userMapper.toUserDto(userStorage.update(updateUser));
    }

    @Override
    public UserDto delete(Long userId) {
        if (!userStorage.isContains(userId)) {
            throw new NotFoundException("Not found");
        }
        log.info("delete.Ok!");
        return userMapper.toUserDto(userStorage.delete(userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userStorage.get(userId);
        try {
            log.info("getUserById.Ok!");
            return userMapper.toUserDto(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("Not Found!");
        }
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("getUsers.Ok!");
        return userStorage.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void validateCreateUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new ValidationException("Email не может быть пустым");
        }
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("Name не может быть пустым");
        }
    }

    private void checkUserExists(String email) {
        if (userStorage.getUserByEmail(email).isPresent()) {
            throw new AlreadyExistsException("Пользователь уже существует");
        }
    }
}
