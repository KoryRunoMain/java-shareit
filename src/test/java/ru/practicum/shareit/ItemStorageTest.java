package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.storage.ItemStorageImpl;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemStorageTest {

    private ItemStorage itemStorage;


    @BeforeEach
    void init() {
        itemStorage = new ItemStorageImpl();
    }

    @Test
    public void create() {
        assertThatCode(() -> {
            Item newItem = Item.builder()
                    .id(1L)
                    .isAvailable(true)
                    .name("Name")
                    .owner(1L)
                    .request(1L)
                    .build();
            itemStorage.create(newItem);
        }).doesNotThrowAnyException();
    }

}

