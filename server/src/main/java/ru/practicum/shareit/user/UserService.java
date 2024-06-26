package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    UserDto getById(Long userId);

    List<UserDto> getAll();

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Long id);

    void delete(Long userId);

}