package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {
    public User toUser(UserDto userDto) {
        return userDto == null ? null : User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public UserDto toUserDto(User user) {
        return user == null ? null : UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public void updateUserDto(UserDto userDto,User userToUpdate,Long userId) {
        userToUpdate.setEmail(userDto.getEmail() != null && !userDto.getEmail().isEmpty()
                && userDto.getEmail().contains("@") ? userDto.getEmail() : userToUpdate.getEmail());
        userToUpdate.setName(userDto.getName() != null
                && !userDto.getName().isEmpty() ? userDto.getName() : userToUpdate.getName());
    }
}
