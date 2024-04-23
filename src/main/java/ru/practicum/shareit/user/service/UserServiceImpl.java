package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.IUserStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private IUserStorage userStorage;
    private UserMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        User newUser = mapper.toUser(userDto);
        validateUser(userDto);
        checkUserExists(userDto.getEmail());
        UserDto createdUser = mapper.toUserDto(userStorage.create(newUser));
        log.info("create.Ok!");
        return createdUser;
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        userDto.setId(id);
        User toUserDto = mapper.toUser(userDto);
        if (!userStorage.isContains(toUserDto)) {
            throw new NotFoundException("Not Found!");
        }
        User updatedUser = userStorage.update(toUserDto);
        log.info("update.Ok!");
        return mapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto delete(Long userId) {
        log.info("delete.Ok!");
        return mapper.toUserDto(userStorage.delete(userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userStorage.get(userId);
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
        return userStorage.getUsers().stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void validateUser(UserDto userDto) {
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
