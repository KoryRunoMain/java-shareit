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
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public UserDto getById(Long userId) {
        UserDto findUserDto = userMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException("User id Not Found!");
                }));
        log.info("method: getById |Request/Response|" + "userId:{} / userId:{}",
                userId, findUserDto);
        return findUserDto;
    }

    @Override
    public UserDto create(UserDto userDto) {
//        validateCreateUser(userDto);
//        checkUserExists(userDto.getEmail());
        UserDto createdUser = userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        log.info("method: create |Request/Response|" + "userDto:{} / createdUser:{}",
                userDto, createdUser);
        return createdUser;
    }

    @Override
    public UserDto save(UserDto userDto, Long userId) {
        User user = userMapper.toUser(getById(userId));
        if (user == null) {
            throw new NotFoundException("fail: update.getUser() User Not Found!");
        }
        getAll().stream()
                .filter(u -> !u.getId().equals(userId) && u.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(u -> {
                    throw new AlreadyExistsException("fail: update.getEmail() Email Is Already Taken!");
                });
        userMapper.updateUserDto(userDto, user);
        UserDto updatedUser = userMapper.toUserDto(userRepository.save(user));
        log.info("method: save |Request/Response|" + "userDto:{}, userId:{} / updatedUserDto:{}",
                userDto, userId, updatedUser);
        return updatedUser;
    }

    @Override
    public void delete(Long userId) {
//        if (!repository.isContains(userId)) {
//            throw new NotFoundException("fail: delete.isContains() User Not Found!");
//        }
        userRepository.deleteById(userId);
        log.info("method: delete |Request|" + "userId:{}", userId);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("method: getAll |Response|" + "list of users:{}", users);
        return users;
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
