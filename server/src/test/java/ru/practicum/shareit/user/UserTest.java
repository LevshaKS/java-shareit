package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    @Test
    void testStructure() {
        User user = new User();

        user.setId(1L);
        user.setName("name");
        user.setEmail("test@email.ru");

        assertEquals(1L, user.getId());
        assertEquals("name", user.getName());
        assertEquals("test@email.ru", user.getEmail());
    }


    @Test
    void testNoArgs() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void testEqualsNull() {
        User user1 = new User();
        User user2 = new User();

        assertNotEquals(user1, user2);
    }


}
