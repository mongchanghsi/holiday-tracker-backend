package kotlinholidaytracker.kotlinholidaytracker.service

import kotlinholidaytracker.kotlinholidaytracker.entity.User
import kotlinholidaytracker.kotlinholidaytracker.exception.BadRequestException
import kotlinholidaytracker.kotlinholidaytracker.exception.NotFoundException
import kotlinholidaytracker.kotlinholidaytracker.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class UserServiceTest {
  @Mock
  private lateinit var mockUserRepository: UserRepository;

  private lateinit var userServiceTest: UserService;

  // Basic mock value
  private val mockUserId = 1L
  private val mockName = "mockName"
  private val mockEmail = "mockEmail"
  private val mockPassword = "mockPassword"

  @BeforeEach
  fun setUp() {
    userServiceTest = UserService(mockUserRepository)
  }

  @Test
  fun canGetAllUsers() {
    val result: List<User> = userServiceTest.getAllUsers()
    val expected: List<User> = ArrayList()
    assertThat(result === expected)
  }

  @Test
  fun canGetUserById() {
    val user = User(mockUserId, mockName, mockEmail, mockPassword)
    val userOptional = Optional.of(user)

    // To allow .findById to return an Optional object
    BDDMockito.given(mockUserRepository.findById(mockUserId))
      .willReturn(userOptional)
    val result: User = userServiceTest.getUser(mockUserId)
    assertThat(result === user)
  }

  @Test
  fun shouldThrowMessageIfUserDoesNotExist() {
    val userOptional = Optional.empty<User>()

    // To allow .findById to return an empty Optional object
    given(mockUserRepository.findById(mockUserId))
      .willReturn(userOptional)
    assertThatThrownBy { userServiceTest.getUser(mockUserId) }
      .isInstanceOf(NotFoundException::class.java)
      .hasMessageContaining("User not found")
  }


  // Missing success POST Method, need to figure out how to capture value

  @Test
  fun shouldThrowMessageIfUnableToAddUserDueToExistingEmail() {
    val user = User(mockUserId, mockName, mockEmail, mockPassword)
    val userOptional = Optional.of(user)
    given(mockUserRepository.findUserByEmail(user.email))
      .willReturn(userOptional)
    assertThatThrownBy { userServiceTest.addNewUser(user) }
      .isInstanceOf(BadRequestException::class.java)
      .hasMessageContaining("User already exist")
  }

  @Test
  fun userCanSignIn() {
    val user = User(mockUserId, mockName, mockEmail, mockPassword)
    val userOptional = Optional.of(user)
    given(mockUserRepository.findUserByEmail(mockEmail))
      .willReturn(userOptional)
    userServiceTest.userSignIn(mockEmail, mockPassword)
  }

  @Test
  fun userCannotSignInWithWrongEmail() {
    val userOptional = Optional.empty<User>()
    given(mockUserRepository.findUserByEmail(mockEmail))
      .willReturn(userOptional)
    assertThatThrownBy { userServiceTest.userSignIn(mockEmail, mockPassword) }
      .isInstanceOf(BadRequestException::class.java)
      .hasMessageContaining("Please check your email and password")
  }

  @Test
  fun userCannotSignInWithWrongPassword() {
    val user = User(mockUserId, mockName, mockEmail, mockPassword)
    val userOptional = Optional.of(user)
    given(mockUserRepository.findUserByEmail(mockEmail))
      .willReturn(userOptional)
    assertThatThrownBy {
      userServiceTest.userSignIn(
        mockEmail,
        "mockWrongPassword"
      )
    }
      .isInstanceOf(BadRequestException::class.java)
      .hasMessageContaining("Please check your email and password")
  }

  @Test
  fun userCanChangePassword() {
    val user = User(mockUserId, mockName, mockEmail, mockPassword)
    val userOptional = Optional.of(user)
    given(mockUserRepository.findById(mockUserId))
      .willReturn(userOptional)
    val returnUser = userServiceTest.userChangePassword(mockUserId, mockPassword, "mockNewPassword")
    assertThat(returnUser.password).isEqualTo("mockNewPassword")
  }

  @Test
  fun userCannotChangePasswordDueToWrongId() {
    val userOptional = Optional.empty<User>()
    given(mockUserRepository.findById(mockUserId))
      .willReturn(userOptional)
    assertThatThrownBy {
      userServiceTest.userChangePassword(
        mockUserId,
        mockPassword,
        "mockNewPassword"
      )
    }
      .isInstanceOf(BadRequestException::class.java)
      .hasMessageContaining("User does not exist")
  }

  @Test
  fun userCannotChangePasswordDueRToWrongOldPassword() {
    val user = User(mockUserId, mockName, mockEmail, mockPassword)
    val userOptional = Optional.of(user)
    given(mockUserRepository.findById(mockUserId))
      .willReturn(userOptional)
    assertThatThrownBy {
      userServiceTest.userChangePassword(
        mockUserId,
        "mockOldPassword",
        "mockWrongPassword"
      )
    }
      .isInstanceOf(BadRequestException::class.java)
      .hasMessageContaining("Old password does not match")
  }
}