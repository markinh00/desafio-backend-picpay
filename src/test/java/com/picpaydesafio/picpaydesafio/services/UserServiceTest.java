package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.domain.user.UserType;
import com.picpaydesafio.picpaydesafio.dtos.user.UserCreateDTO;
import com.picpaydesafio.picpaydesafio.dtos.user.UserUpdateDTO;
import com.picpaydesafio.picpaydesafio.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a new user successfully")
    void createUser() {
        UserCreateDTO dto = new UserCreateDTO(
                "John",
                "john@example.com",
                "12345678900",
                "99911199910",
                new BigDecimal(100),
                UserType.COMMON
        );
        User user = new User(dto);

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.findByDocument(dto.document())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User created = userService.createUser(dto);

        assertNotNull(created);
        assertEquals("John", created.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void findUserByIdSuccess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John");

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));

        User found = userService.findUserById(1L);

        assertNotNull(found);
        assertEquals("John", found.getName());
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void findUserByIdNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userService.findUserById(1L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should update user successfully")
    void updateUserSuccess() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setDocument("12345678900");

        UserUpdateDTO updateDTO = new UserUpdateDTO("New Name", null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByDocument(existingUser.getDocument())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updated = userService.updateUser(1L, updateDTO);

        assertEquals("New Name", updated.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void updateUserNotFound() {
        UserUpdateDTO updateDTO = new UserUpdateDTO("New Name", null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userService.updateUser(1L, updateDTO));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUserSuccess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("To Delete");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        User deleted = userService.deleteUser(1L);

        assertNotNull(deleted);
        assertEquals("To Delete", deleted.getName());
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void deleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userService.deleteUser(1L));

        assertEquals("User not found", exception.getMessage());
    }
}
