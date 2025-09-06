package in.gov.egramswaraj.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.request.UserRequest;
import in.gov.egramswaraj.response.UserDeatilsResponse;
import in.gov.egramswaraj.response.UserResponse;
import in.gov.egramswaraj.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

	@InjectMocks
    private UserRestController userRestController;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    private UserRequest userRequest;
    private UserEntity userEntity;
    private UserResponse userResponse;

    @BeforeEach
    void setup() {
        userRequest = new UserRequest();

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("john.doe");
        userEntity.setDescription("ADMIN");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("john.doe");
        userResponse.setDescription("ADMIN");
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(userRequest)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);

        UserResponse response = userRestController.createUser(userRequest);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("john.doe");
        verify(userService, times(1)).createUser(userRequest);
    }

    @Test
    void testUpdateUserStatus() {
        when(userService.updateUserStatus(1L, true)).thenReturn(AppConstant.USER_STATUS);

        ResponseEntity<String> response = userRestController.updateUserStatus(1L, true);

        assertThat(response.getBody()).isEqualTo(AppConstant.USER_STATUS);
        verify(userService, times(1)).updateUserStatus(1L, true);
    }

    @Test
    void testDeleteUser() {
        when(userService.deleteUser(1L)).thenReturn(AppConstant.USER_DELETION);

        ResponseEntity<String> response = userRestController.deleteUser(1L);

        assertThat(response.getBody()).isEqualTo(AppConstant.USER_DELETION);
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testGetAllUsersPaged() {
        Page<UserEntity> page = new PageImpl<>(Collections.singletonList(userEntity));
        when(userService.getAllUsers(0, 100)).thenReturn(page);
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);

        ResponseEntity<Page<UserResponse>> response = userRestController.getAllUsersPaged(0, 100);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).getUsername()).isEqualTo("john.doe");
        verify(userService, times(1)).getAllUsers(0, 100);
    }

    @Test
    void testGetUserById() {
        UserDeatilsResponse userDetailsResponse = new UserDeatilsResponse();
        userDetailsResponse.setId(1L);
        userDetailsResponse.setUsername("john.doe");

        when(userService.getUserById(1L)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDeatilsResponse.class)).thenReturn(userDetailsResponse);

        ResponseEntity<UserDeatilsResponse> response = userRestController.getUserById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("john.doe");
        verify(userService, times(1)).getUserById(1L);
    }

}
