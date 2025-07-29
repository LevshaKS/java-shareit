//package ru.practicum.shareit.user;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import ru.practicum.shareit.user.client.UserClient;
//import ru.practicum.shareit.user.dto.UserDto;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//
////@ExtendWith(MockitoExtension.class)
// class UserClientTest {
//    @Mock
//    RestTemplate restTemplate  ;
//
//@Mock
//    RestTemplateBuilder builder;
//    @InjectMocks
//     UserClient userClient;
//    UserDto userDto, userDtoRequest;
//    long id;
//    @BeforeEach
//     void setUp () {
//        MockitoAnnotations.openMocks(this);
//      userClient=  new UserClient("", builder);
//       // when(builder.build()).thenReturn(restTemplate);
//      //  when(builder.uriTemplateHandler(any())).thenReturn(builder);
//      //  when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
//           id = 1;
//        userDto = new UserDto(null, "name", "test@email.ru");
//        userDtoRequest = new UserDto(id, "name", "test@email.ru");
//    }
//    @Test
//    void saveUser  ()  {
//
//            when(restTemplate.exchange(anyString(),  eq(HttpMethod.POST),  any(HttpEntity.class), eq(Object.class)))
//                    .thenReturn(ResponseEntity.ok().body(userDtoRequest));
//            ResponseEntity<Object> result = userClient.createUser(userDto);
//            assertThat(result.getBody()).isEqualTo(userDtoRequest);
//            assertNotNull(result,"не должен быть пустым");
//            verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(UserDto.class));
//        }
//}
