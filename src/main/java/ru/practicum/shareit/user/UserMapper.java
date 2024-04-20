package ru.practicum.shareit.user;

public class UserMapper {
    public User toUser(User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
