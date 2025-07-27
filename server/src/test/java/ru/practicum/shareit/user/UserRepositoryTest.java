package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb", "spring.jpa.hibernate.ddl-auto=create-drop"})
public class UserRepositoryTest {
    private User user, user2;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("test");
        user.setEmail("test@email.ru");

        user2 = new User();
        user2.setName("test2");
        user2.setEmail("test2@email.ru");
    }

    @AfterEach
    public void tearDown() {

        userRepository.delete(user);
        userRepository.delete(user2);
    }

    @Test
    public void saveUser() {
        userRepository.save(user);
        User userSave = userRepository.findById(1L).orElse(null);
        assertNotNull(userSave);
        assertEquals(user.getName(), userSave.getName());

    }

    @Test
    public void updateUser() {
        userRepository.save(user);
        User userSave = userRepository.findById(2L).orElse(null);
        userSave.setName("newName");
        userRepository.save(userSave);
        User userSaveNew = userRepository.findById(2L).orElse(null);
        assertNotNull(userSaveNew);
        assertEquals(userSave.getName(), userSaveNew.getName());
    }

    @Test
    public void findByIdUser() {
        userRepository.save(user);
        User userSave = userRepository.findById(5L).orElse(null);
        assertNotNull(userSave);
        assertEquals(userSave.getName(), user.getName());
    }

    @Test
    public void findByAllUser() {
        userRepository.save(user);
        userRepository.save(user2);
        List<User> userAll = userRepository.findAll();
        assertNotNull(userAll);
        assertEquals(userAll.size(), 2);
        assertEquals(userAll.get(1).getName(), user2.getName());
    }

    @Test
    public void deleteUser() {
        userRepository.save(user);
        userRepository.save(user2);
        List<User> userAll = userRepository.findAll();
        assertNotNull(userAll);
        assertEquals(userAll.size(), 2);
        System.out.println(userAll);
        userRepository.deleteById(7);
        userAll = userRepository.findAll();
        assertEquals(userAll.size(), 1);
    }
}
