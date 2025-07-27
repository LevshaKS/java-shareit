//package ru.practicum.shareit.user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import ru.practicum.shareit.user.client.UserClient;
//import ru.practicum.shareit.user.dto.UserDto;
//
//import static org.mockito.Mockito.eq;
//import static org.mockito.Mockito.when;
//
//
//public class UserClientTest {
//        @Mock
//      private RestTemplate restTemplate;
//
//    @InjectMocks
//    private UserClient userClient;
//
//
//    UserDto userDto, userDtoRequest;
//    long id;
//
//    @BeforeEach
//    void setUp () {
//       this.restTemplate = restTemplate;
//
//        id = 1;
//        userDto = new UserDto(null, "name", "test@email.ru");
//        userDtoRequest = new UserDto(id, "name", "test@email.ru");
//    }
//
//    @Test
//    void saveUser  () {
//
//        System.out.println(restTemplate);
//        when(restTemplate.exchange(ArgumentMatchers.anyString(),  eq(HttpMethod.POST),  ArgumentMatchers.any(), eq(UserDto.class)))
//                .thenReturn(ResponseEntity.ok(userDtoRequest));
//
//      //  ResponseEntity<Object> result = userClient.createUser(userDto);
//      //  assertThat(result.getBody()).isEqualTo(userDtoRequest);
//     //   assertNotNull(result,"не должен быть пустым");
//    //   verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(UserDto.class));
//    }
//
//}
