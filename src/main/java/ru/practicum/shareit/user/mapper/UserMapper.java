package ru.practicum.shareit.user.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

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

    public void updateUserDto(UserDto userDto, User userToUpdate) {
        String updatedEmail = Optional.ofNullable(userDto.getEmail())
                .filter(email -> email.contains("@"))
                .orElse(userToUpdate.getEmail());
        userToUpdate.setEmail(updatedEmail);

        String updatedName = Optional.ofNullable(userDto.getName())
                .filter(name -> !name.isEmpty())
                .orElse(userToUpdate.getName());
        userToUpdate.setName(updatedName);
    }

}
