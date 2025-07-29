package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb1"})
public class ItemRepositoryTest {

    private Item item, item2;

    @Autowired
    private UserRepository userRepository;
    private User user;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("name");
        user.setEmail("name@test.ru");
        userRepository.save(user);

        item = new Item();
        item.setName("test1");
        item.setAvailable(true);
        item.setDescription("testtest");
        item.setUser(user);


        item2 = new Item();
        item2.setName("test2");
        item2.setAvailable(true);
        item2.setDescription("testtest2");
        item2.setUser(user);
    }

    @AfterEach
    public void tearDown() {

        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void saveItem() {
        itemRepository.save(item);
        Item saveItem = itemRepository.findById(1L).orElse(null);
        assertNotNull(saveItem);
        assertEquals(item.getName(), saveItem.getName());
    }

    @Test
    void updateItem() {
        itemRepository.save(item);
        System.out.println(item);
        Item saveItem = itemRepository.findById(4L).orElse(null);
        System.out.println(saveItem);
        saveItem.setName("newName");
        itemRepository.save(saveItem);
        Item saveItemNew = itemRepository.findById(4L).orElse(null);
        assertNotNull(saveItemNew);
        assertEquals(saveItem.getName(), saveItemNew.getName());
    }

    @Test
    void findById() {
        itemRepository.save(item);
        System.out.println(item);
        Item saveItem = itemRepository.findById(3L).orElse(null);
        System.out.println(saveItem);
        assertNotNull(saveItem);
        assertEquals(saveItem.getName(), item.getName());
    }

    @Test
    void findByRequest_id() {
        userRepository.save(user);
        itemRepository.save(item);
        System.out.println(item);
        System.out.println(user);
        Collection<Item> saveItem = itemRepository.findByUserId(2L);
        System.out.println(saveItem);
        assertNotNull(saveItem);
        assertEquals(saveItem.toArray().length, 1);
        assertEquals(saveItem.stream().toList().get(0).getUser().getName(), user.getName());
    }


}
