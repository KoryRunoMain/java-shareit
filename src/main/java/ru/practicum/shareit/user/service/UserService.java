package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.tokens.ScalarToken;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.IUserStorage;
import ru.practicum.shareit.user.UserMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private IUserStorage userStorage;
    private UserMapper userMapper;


    public UserDto create(UserDto userDto) {
        User toUserDto = userMapper.toUser(userDto);
        return userMapper.toUserDto(userStorage.create(toUserDto));
    }

    @Transactional
    public UserDto update(UserDto userDto, Long id) {
        userDto.setId(id);
        User toUserDto = userMapper.toUser(userDto);
        if (!userStorage.isContains(toUserDto)) {
            throw new NotFoundException("Not Found!");
        }
        User updatedUser = userStorage.update(toUserDto);
        return userMapper.toUserDto(updatedUser);
    }

    @Transactional
    public UserDto delete(Long userId) {
        return userMapper.toUserDto(userStorage.delete(userId));
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        User user = userStorage.get(userId);
        try {
            return userMapper.toUserDto(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("Not Found!");
        }
    }
}
