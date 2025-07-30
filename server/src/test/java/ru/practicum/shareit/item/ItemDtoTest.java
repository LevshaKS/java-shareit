package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {


    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    public void itemDtoSerialization() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("testDescription");
        itemDto.setAvailable(true);
        itemDto.setOwner(1);
        itemDto.setRequestId(1L);

        assertThat(this.json.write(itemDto)).extractingJsonPathStringValue("name").isEqualTo("test");
        assertThat(this.json.write(itemDto)).extractingJsonPathStringValue("description").isEqualTo("testDescription");
        assertThat(this.json.write(itemDto)).extractingJsonPathBooleanValue("available").isEqualTo(true);
        assertThat(this.json.write(itemDto)).extractingJsonPathNumberValue("owner").isEqualTo(1);
        assertThat(this.json.write(itemDto)).extractingJsonPathNumberValue("requestId").isEqualTo(1);
    }

    @Test
    public void itemDtoDeserialization() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("testDescription");
        itemDto.setAvailable(true);
        itemDto.setOwner(1);
        itemDto.setRequestId(1L);

        String content = "{\"name\":\"test\",\"description\":\"testDescription\"," +
                "\"available\":\"true\",\"owner\":\"1\",\"requestId\":\"1\"}";

        assertThat(this.json.parse(content)).isEqualTo(itemDto);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("test");
        assertThat(this.json.parseObject(content).getDescription()).isEqualTo("testDescription");
        assertThat(this.json.parseObject(content).getAvailable()).isEqualTo(true);
        assertThat(this.json.parseObject(content).getOwner()).isEqualTo(1);
        assertThat(this.json.parseObject(content).getRequestId()).isEqualTo(1);
    }

}

