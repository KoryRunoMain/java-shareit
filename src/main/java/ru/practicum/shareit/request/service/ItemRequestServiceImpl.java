package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;


    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        User user = userMapper.toUser(userService.getById(userId));
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setItems(createItemList(itemRequestDto.getItems()));
        itemRequestRepository.save(itemRequest);

        ItemRequestDto createdItemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        log.info("method: create |Request/Response|" + " userId={}, itemRequestDto={} / createdItemRequestDto={}",
                userId, itemRequestDto, createdItemRequestDto);
        return createdItemRequestDto;
    }

    @Override
    public ItemRequestDto getById(Long requestId, Long userId) {
        userService.getById(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("fail: requestId Not Found!"));

        List<Item> items = itemRepository.findAllByItemRequest(itemRequest);
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(createItemDtoList(items));
        log.info("method: getById |Request/Response|" + " userId={}, requestId={} / itemRequestDto={}",
                userId, itemRequestDto, itemRequestDto);
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getOwnItemRequests(Long userId, int from, int size) {
        userService.getById(userId);
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"created"));
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorId(userId, pageRequest);
        if (itemRequestList.isEmpty()) {
            log.info("fail: itemRequestList is Empty!");
        }

        List<ItemRequestDto> itemRequestDtoList = createItemRequestList(itemRequestList);
        log.info("method: getAllItemRequests |Request/Response|" + " userId={}, page={}, size={} " +
                "/ itemRequestDtoList={}", userId, page, size, itemRequestDtoList);
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size) {
        userService.getById(userId);
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"created"));
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorIdIsNot(userId, pageRequest);
        if (itemRequestList.isEmpty()) {
            log.info("fail: itemRequestList is Empty!");
        }

        List<ItemRequestDto> itemRequestDtoList = createItemRequestList(itemRequestList);
        log.info("method: getAllItemRequests |Request/Response|" + " userId={}, page={}, size={} " +
                "/ itemRequestDtoList={}", userId, page, size, itemRequestDtoList);
        return itemRequestDtoList;
    }

    private List<ItemDto> createItemDtoList(List<Item> items) {
        return Optional.ofNullable(items)
                .map(item -> item.stream().map(itemMapper::toItemDto).collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    private List<Item> createItemList(List<ItemDto> items) {
        return Optional.ofNullable(items)
                .map(itemDto -> itemDto.stream().map(itemMapper::toItem).collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    private List<ItemRequestDto> createItemRequestList(List<ItemRequest> requestList) {
        return requestList.stream()
                .peek(itemRequest -> itemRequest.setItems(itemRepository.findAllByItemRequest(itemRequest)))
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

}