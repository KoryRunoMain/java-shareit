package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;
    private UserMapper userMapper;

    @Override
    public UserDto get(Long userId) {
        UserDto getUserDto = userMapper.toUserDto(userStorage.get(userId));
        log.info("get.Ok!");
        return getUserDto;
    }

    @Override
    public UserDto create(UserDto userDto) {
        validateCreateUser(userDto);
        checkUserExists(userDto.getEmail());
        UserDto createdUserDto = userMapper.toUserDto(userStorage.create(userMapper.toUser(userDto)));
        log.info("create.Ok!");
        return createdUserDto;
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User userToUpdate = userStorage.get(userId);
        if (userToUpdate == null) {
            throw new NotFoundException("update.NotFound!");
        }
        userStorage.getUsers().stream()
                .filter(u -> !u.getId().equals(userId) && u.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(user -> {
                    throw new AlreadyExistsException("update.EmailIsAlreadyTaken!");
                });
        userToUpdate.setEmail(userDto.getEmail() != null && !userDto.getEmail().isEmpty()
                && userDto.getEmail().contains("@") ? userDto.getEmail() : userToUpdate.getEmail());
        userToUpdate.setName(userDto.getName() != null && !userDto.getName().isEmpty() ?
                userDto.getName() : userToUpdate.getName());

        UserDto updatedUserDto = userMapper.toUserDto(userStorage.update(userToUpdate));
        log.info("update.Ok!");
        return updatedUserDto;
    }

    @Override
    public UserDto delete(Long userId) {
        if (!userStorage.isContains(userId)) {
            throw new NotFoundException("delete.NotFound!");
        }
        UserDto deleteUserDto = userMapper.toUserDto(userStorage.delete(userId));
        log.info("delete.Ok!");
        return deleteUserDto;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> getUsersDto = userStorage.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("getUsers.Ok!");
        return getUsersDto;
    }

    private void validateCreateUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new ValidationException("validateCreateUser.InValidField!");
        }
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("validateCreateUser.InValidField!");
        }
    }

    private void checkUserExists(String email) {
        if (userStorage.getUserByEmail(email).isPresent()) {
            throw new AlreadyExistsException("checkUserExists.EmailIsAlreadyTaken!");
        }
    }
}
