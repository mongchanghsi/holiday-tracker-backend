package kotlinholidaytracker.kotlinholidaytracker.controller

import kotlinholidaytracker.kotlinholidaytracker.entity.User
import kotlinholidaytracker.kotlinholidaytracker.exception.BadRequestException
import kotlinholidaytracker.kotlinholidaytracker.service.UserService
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

class ChangePasswordForm (val oldPassword: String, val newPassword: String);
class SignInForm(val email: String, val password: String);

@RestController
@RequestMapping("api/user")
class UserController (private val userService: UserService){
    @GetMapping
    fun getUsers(): List<User> {
        return userService.getAllUsers();
    }

    // Alternative way to represent
    // fun getUsers(): List<User> = userService.getAllUsers();

    @GetMapping("/search")
    fun getUser(@RequestParam(required = false) id: Long?): User {
        if (id == null) throw BadRequestException("Please enter a id as a query parameter")
        return userService.getUser(id);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun userSignUp(@RequestBody user: User): User {
        if (user.name.isEmpty()) throw BadRequestException("Missing name");
        if (user.email.isEmpty()) throw BadRequestException("Missing email");
        if (user.password.isEmpty()) throw BadRequestException("Missing password");
        return userService.addNewUser(user);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun userSignIn(@RequestBody signInForm: SignInForm) {
        if (signInForm.email.isEmpty()) throw BadRequestException("Missing email");
        if (signInForm.password.isEmpty()) throw BadRequestException("Missing password");
        userService.userSignIn(signInForm.email, signInForm.password);
    }

    @PutMapping("/changepassword/{id}")
    fun changePassword(@PathVariable id: Long, @RequestBody passwordForm: ChangePasswordForm): User {
        return userService.userChangePassword(id, passwordForm.oldPassword, passwordForm.newPassword);
    }
}