package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.IUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private IUserStorage userStorage;
    private UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        User toUserDto = userMapper.toUser(userDto);
        log.info("Ok!");
        return userMapper.toUserDto(userStorage.create(toUserDto));
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        userDto.setId(id);
        User toUserDto = userMapper.toUser(userDto);
        if (!userStorage.isContains(toUserDto)) {
            throw new NotFoundException("Not Found!");
        }
        User updatedUser = userStorage.update(toUserDto);
        log.info("Ok!");
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto delete(Long userId) {
        log.info("Ok!");
        return userMapper.toUserDto(userStorage.delete(userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userStorage.get(userId);
        try {
            log.info("Ok!");
            return userMapper.toUserDto(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("Not Found!");
        }
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Ok!");
        return userStorage.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
