package javamaven.javamaven.service;

import javamaven.javamaven.entity.User;
import javamaven.javamaven.exception.BadRequestException;
import javamaven.javamaven.exception.NotFoundException;
import javamaven.javamaven.repository.UserRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private UserService userServiceTest;

    // Basic mock value
    private Long mockUserId = 1L;
    private String mockName = "mockName";
    private String mockEmail = "mockEmail";
    private String mockPassword = "mockPassword";

    @BeforeEach
    void setUp() {
        userServiceTest = new UserService(mockUserRepository);
    }

    @Test
    void canGetAllUsers() {
        userServiceTest.getAllUsers();

        verify(mockUserRepository).findAll();
    }

    @Test
    void canGetUserById() {
        User user = new User(mockName, mockEmail, mockPassword);

        Optional<User> userOptional = Optional.of(user);

        // To allow .findById to return an Optional object
        given(mockUserRepository.findById(mockUserId))
                .willReturn(userOptional);

        userServiceTest.getUser(mockUserId);

        verify(mockUserRepository).findById(mockUserId);
    }

    @Test
    void shouldThrowMessageIfUserDoesNotExist() {
        Optional<User> userOptional = Optional.empty();

        // To allow .findById to return an empty Optional object
        given(mockUserRepository.findById(mockUserId))
                .willReturn(userOptional);

        assertThatThrownBy(() -> userServiceTest.getUser(mockUserId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found");

        verify(mockUserRepository).findById(mockUserId);
    }

    @Test
    void canAddNewUser() {
        User user = new User(mockName, mockEmail, mockPassword);

        userServiceTest.addNewUser(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(mockUserRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);

        verify(mockUserRepository).save(user);
    }

    @Test
    void shouldThrowMessageIfUnableToAddUserDueToExistingEmail() {
        User user = new User(mockName, mockEmail, mockPassword);

        Optional<User> userOptional = Optional.of(user);

        given(mockUserRepository.findUserByEmail(user.getEmail()))
                .willReturn(userOptional);

        assertThatThrownBy(() -> userServiceTest.addNewUser(user))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("User already exist");

        verify(mockUserRepository, never()).save(any());
    }

    @Test
    void userCanSignIn() {
        User user = new User(mockName, mockEmail, mockPassword);

        Optional<User> userOptional = Optional.of(user);

        given(mockUserRepository.findUserByEmail(mockEmail))
                .willReturn(userOptional);

        userServiceTest.userSignIn(mockEmail, mockPassword);

        verify(mockUserRepository).findUserByEmail(mockEmail);
    }

    @Test
    void userCannotSignInWithWrongEmail() {
        Optional<User> userOptional = Optional.empty();

        given(mockUserRepository.findUserByEmail(mockEmail))
                .willReturn(userOptional);

        assertThatThrownBy(() -> userServiceTest.userSignIn(mockEmail, mockPassword))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Please check your email and password");

        verify(mockUserRepository).findUserByEmail(mockEmail);
    }

    @Test
    void userCannotSignInWithWrongPassword() {
        User user = new User(mockName, mockEmail, mockPassword);

        Optional<User> userOptional = Optional.of(user);

        given(mockUserRepository.findUserByEmail(mockEmail))
                .willReturn(userOptional);

        assertThatThrownBy(() -> userServiceTest.userSignIn(mockEmail, "mockWrongPassword"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Please check your email and password");

        verify(mockUserRepository).findUserByEmail(mockEmail);
    }

    @Test
    void userCanChangePassword() {
        User user = new User(mockName, mockEmail, mockPassword);

        Optional<User> userOptional = Optional.of(user);

        given(mockUserRepository.findById(mockUserId))
                .willReturn(userOptional);

        User returnUser = userServiceTest.userChangePassword(mockUserId, mockPassword, "mockNewPassword");

        verify(mockUserRepository).findById(mockUserId);

        assertThat(returnUser.getPassword()).isEqualTo("mockNewPassword");
    }

    @Test
    void userCannotChangePasswordDueToWrongId() {
        Optional<User> userOptional = Optional.empty();

        given(mockUserRepository.findById(mockUserId))
                .willReturn(userOptional);

        assertThatThrownBy(() -> userServiceTest.userChangePassword(mockUserId, mockPassword, "mockNewPassword"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("User does not exist");

        verify(mockUserRepository).findById(mockUserId);
    }

    @Test
    void userCannotChangePasswordDueRToWrongOldPassword() {
        User user = new User(mockName, mockEmail, mockPassword);

        Optional<User> userOptional = Optional.of(user);

        given(mockUserRepository.findById(mockUserId))
                .willReturn(userOptional);

        assertThatThrownBy(() -> userServiceTest.userChangePassword(mockUserId, "mockOldPassword", "mockWrongPassword"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Old password does not match");

        verify(mockUserRepository).findById(mockUserId);
    }
}