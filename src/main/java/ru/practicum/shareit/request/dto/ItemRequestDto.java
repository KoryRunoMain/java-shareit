package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {

    private Long id;

    private String description;

    private UserDto requestor;

    private LocalDateTime created;

}