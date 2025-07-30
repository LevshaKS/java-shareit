package ru.practicum.shareit.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    public void userDtoSerialization() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("testName");
        userDto.setEmail("test@email.com");
        assertThat(this.json.write(userDto)).extractingJsonPathStringValue("name").isEqualTo("testName");
        assertThat(this.json.write(userDto)).extractingJsonPathStringValue("email").isEqualTo("test@email.com");
    }


    @Test
    public void userDtoDeserialization() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("testName");
        userDto.setEmail("test@email.com");

        String content = "{\"name\":\"testName\",\"email\":\"test@email.com\"}";

        assertThat(this.json.parse(content)).isEqualTo(userDto);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("testName");
        assertThat(this.json.parseObject(content).getEmail()).isEqualTo("test@email.com");
    }

}
