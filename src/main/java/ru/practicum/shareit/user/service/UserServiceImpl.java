package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private UserMapper userMapper;

    @Override
    public UserDto getById(Long userId) {
        UserDto userDto = userMapper.toUserDto(repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("fail: user/owner ID Not Found!")));
        log.info("method: getById |Request/Response|" + "userId:{} / userId:{}", userId, userDto);
        return userDto;
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = repository.save(userMapper.toUser(userDto));
        UserDto createdUserDto = userMapper.toUserDto(user);
        log.info("method: create |Request/Response|" + "userDto:{} / createdUser:{}", userDto, createdUserDto);
        return createdUserDto;
    }

    @Override
    public UserDto save(UserDto userDto, Long userId) {
        User user = userMapper.toUser(getById(userId));
        if (getById(userId) == null) {
            throw new NotFoundException("fail: user Not Found!");
        }
        Optional<User> existingUser = repository.findByIdNotAndEmail(userId, user.getEmail());
        if (existingUser.isPresent()) {
            throw new AlreadyExistsException("fail: email Is Already Taken!");
        }
        userMapper.updateUserDto(userDto, user);
        repository.save(user);
        UserDto updatedUserDto = userMapper.toUserDto(user);
        log.info("method: save |Request/Response|" + "userDto:{}, userId:{} / updatedUserDto:{}",
                userDto, userId, updatedUserDto);
        return updatedUserDto;
    }

    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
        log.info("method: delete |Request|" + "userId:{}", userId);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> usersDto = repository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("method: getAll |Response|" + "list of users:{}", usersDto);
        return usersDto;
    }

}
