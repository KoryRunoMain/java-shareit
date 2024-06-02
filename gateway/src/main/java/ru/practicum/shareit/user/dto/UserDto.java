package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class UserDto {

    public Long id;

    @NotBlank
    public String name;

    @NotBlank
    @Email
    public String email;

}
