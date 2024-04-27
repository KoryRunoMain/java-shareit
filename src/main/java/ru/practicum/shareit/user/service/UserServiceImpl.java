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

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;
    private UserMapper userMapper;

    @Override
    public UserDto get(Long userId) {
        UserDto getUserDto = userMapper.toUserDto(userStorage.get(userId));
        log.info("method: get |Request/Response|" + "userId:{} / userId:{}",
                userId, getUserDto);
        return getUserDto;
    }

    @Override
    public UserDto create(UserDto userDto) {
        validateCreateUser(userDto);
        checkUserExists(userDto.getEmail());
        UserDto createdUserDto = userMapper.toUserDto(userStorage.create(userMapper.toUser(userDto)));
        log.info("method: create |Request/Response|" + "userDto:{} / createdUserDto:{}",
                userDto, createdUserDto);
        return createdUserDto;
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User userToUpdate = userStorage.get(userId);
        if (userToUpdate == null) {
            throw new NotFoundException("fail: update.getUser() User Not Found!");
        }
        userStorage.getUsers().stream()
                .filter(u -> !u.getId().equals(userId) && u.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(user -> {
                    throw new AlreadyExistsException("fail: update.getEmail() Email Is Already Taken!");
                });
        userMapper.updateUserDto(userDto, userToUpdate, userId);
        UserDto updatedUserDto = userMapper.toUserDto(userStorage.update(userToUpdate));
        log.info("method: update |Request/Response|" + "userDto:{}, userId:{} / updatedUserDto:{}",
                userDto, userId, updatedUserDto);
        return updatedUserDto;
    }

    @Override
    public UserDto delete(Long userId) {
        if (!userStorage.isContains(userId)) {
            throw new NotFoundException("fail: delete.isContains() User Not Found!");
        }
        UserDto deleteUserDto = userMapper.toUserDto(userStorage.delete(userId));
        log.info("method: delete |Request/Response|" + "userId:{} / deleteUserDto:{}",
                userId, deleteUserDto);
        return deleteUserDto;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> getUsersDto = userStorage.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("method: getUsers |Response|" + "ListUsers:{}",
                getUsersDto);
        return getUsersDto;
    }

    private void validateCreateUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new ValidationException("fail: validateCreateUser.getEmail() is Null or isBlank!");
        }
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("fail: validateCreateUser.getName() is Null or isBlank!");
        }
    }

    private void checkUserExists(String email) {
        if (userStorage.getUserByEmail(email).isPresent()) {
            throw new AlreadyExistsException("fail: checkUserExists.getUserByEmail() Email Is Already Taken!");
        }
    }
}
