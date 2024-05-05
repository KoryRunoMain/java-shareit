package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private UserMapper userMapper;

    @Override
    public UserDto findUserById(Long userId) {
        UserDto findUserDto = userMapper.toUserDto(repository.findById(userId)
                        .orElseThrow(() -> {
                            throw new NotFoundException("User id Not Found!");
                        }));
        log.info("method: get |Request/Response|" + "userId:{} / userId:{}",
                userId, findUserDto);
        return findUserDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
//        validateCreateUser(userDto);
//        checkUserExists(userDto.getEmail());
        UserDto createdUserDto = userMapper.toUserDto(repository.save(userMapper.toUser(userDto)));
        log.info("method: create |Request/Response|" + "userDto:{} / createdUserDto:{}",
                userDto, createdUserDto);
        return createdUserDto;
    }

    @Override
    public UserDto saveUser(UserDto userDto, Long userId) {
        User userToUpdate = userMapper.toUser(findUserById(userId));
        if (userToUpdate == null) {
            throw new NotFoundException("fail: update.getUser() User Not Found!");
        }
        findAllUsers().stream()
                .filter(u -> !u.getId().equals(userId) && u.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(user -> {
                    throw new AlreadyExistsException("fail: update.getEmail() Email Is Already Taken!");
                });
        userMapper.updateUserDto(userDto, userToUpdate);
        UserDto updatedUserDto = userMapper.toUserDto(repository.save(userToUpdate));
        log.info("method: update |Request/Response|" + "userDto:{}, userId:{} / updatedUserDto:{}",
                userDto, userId, updatedUserDto);
        return updatedUserDto;
    }

    @Override
    public void deleteUserById(Long userId) {
//        if (!repository.isContains(userId)) {
//            throw new NotFoundException("fail: delete.isContains() User Not Found!");
//        }
        repository.deleteById(userId);
        log.info("method: delete |Request|" + "userId:{}", userId);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserDto> getUsersDto = repository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("method: getUsers |Response|" + "ListUsers:{}", getUsersDto);
        return getUsersDto;
    }

//    private void validateCreateUser(UserDto userDto) {
//        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
//            throw new ValidationException("fail: validateCreateUser.getEmail() is Null or isBlank!");
//        }
//        if (userDto.getName() == null || userDto.getName().isBlank()) {
//            throw new ValidationException("fail: validateCreateUser.getName() is Null or isBlank!");
//        }
//    }
//
//    private void checkUserExists(String email) {
//        if (repository.getUserByEmail(email).isPresent()) {
//            throw new AlreadyExistsException("fail: checkUserExists.getUserByEmail() Email Is Already Taken!");
//        }
//    }
}
