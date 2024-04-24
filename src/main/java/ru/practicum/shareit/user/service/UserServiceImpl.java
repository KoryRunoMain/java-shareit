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

    private UserStorage storage;
    private UserMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        User newUser = mapper.toUser(userDto);
        validateCreateUser(userDto);
        checkUserExists(userDto.getEmail());
        UserDto createdUser = mapper.toUserDto(storage.create(newUser));
        log.info("create.Ok!");
        return createdUser;
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User updateUser = storage.get(userId);
        if (updateUser == null) {
            throw new NotFoundException("Not Found!");
        }
        storage.getUsers().stream()
                .filter(u -> !u.getId().equals(userId) && u.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(user -> {
                    throw new AlreadyExistsException("Email уже занят");
                });
        if (userDto.getEmail() != null
                && !userDto.getEmail().isEmpty()
                && userDto.getEmail().contains("@")) {
            updateUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null
                && !userDto.getName().isEmpty()) {
            updateUser.setName(userDto.getName());
        }
        return mapper.toUserDto(storage.update(updateUser));
    }

    @Override
    public UserDto delete(Long userId) {
        if (!storage.isContains(userId)) {
            throw new NotFoundException("Not found");
        }
        log.info("delete.Ok!");
        return mapper.toUserDto(storage.delete(userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = storage.get(userId);
        try {
            log.info("getUserById.Ok!");
            return mapper.toUserDto(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("Not Found!");
        }
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("getUsers.Ok!");
        return storage.getUsers().stream()
                .map(mapper::toUserDto)
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
        if (storage.getUserByEmail(email).isPresent()) {
            throw new AlreadyExistsException("Пользователь уже существует");
        }
    }
}
