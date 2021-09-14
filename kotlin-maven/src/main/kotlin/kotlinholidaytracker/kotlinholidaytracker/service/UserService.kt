package kotlinholidaytracker.kotlinholidaytracker.service

import kotlinholidaytracker.kotlinholidaytracker.entity.User
import kotlinholidaytracker.kotlinholidaytracker.exception.BadRequestException
import kotlinholidaytracker.kotlinholidaytracker.exception.NotFoundException
import kotlinholidaytracker.kotlinholidaytracker.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService (private val userRepository: UserRepository) {
  fun getAllUsers(): List<User> {
    return userRepository.findAll()
  }

  fun getUser(id: Long): User {
    val userEntry: Optional<User> = userRepository.findById(id)
    if (userEntry.isEmpty) throw NotFoundException("User not found")
    return userEntry.get()
  }

  fun addNewUser(user: User): User {
    val userByEmail: Optional<User> = userRepository.findUserByEmail(user.email)
    if (userByEmail.isPresent) throw BadRequestException("User already exist")
    return userRepository.save(user)
  }

  fun userSignIn(email: String, password: String) {
    val userEntry: Optional<User> = userRepository.findUserByEmail(email)
    if (userEntry.isEmpty) throw BadRequestException("Please check your email and password")
    if (userEntry.get().password != password) throw BadRequestException("Please check your email and password")
  }

  fun userChangePassword(id: Long, oldPassword: String, newPassword: String): User {
    val userById: Optional<User> = userRepository.findById(id)
    if (userById.isEmpty) throw BadRequestException("User does not exist")
    if (userById.get().password != oldPassword) throw BadRequestException("Old password does not match")
    userById.get().password = newPassword
    userRepository.save(userById.get())
    return userById.get()
  }
}