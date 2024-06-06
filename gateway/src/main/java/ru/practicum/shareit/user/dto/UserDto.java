package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    public Long id;

    @NotBlank
    public String name;

    @NotBlank
    @Email
    public String email;

}
