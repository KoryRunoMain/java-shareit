package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    @NotBlank(message = "поле не может быть пустым")
    private String name;

    @NotBlank(message = "поле не может быть пустым")
    @Email
    private String email;
}
