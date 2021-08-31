package javamaven.javamaven.controller;

import javamaven.javamaven.entity.User;
import javamaven.javamaven.exception.BadRequestException;
import javamaven.javamaven.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

class SignInForm {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

class ChangePasswordForm {
    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}

@RestController
@CrossOrigin(origins = "https://localhost:3000")
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) { this.userService = userService; };

    @GetMapping
    public List<User> getUsers() { return userService.getAllUsers(); }

    @GetMapping("/search")
    public User getUser(@RequestParam(required = false) Long id) {
        if (id == null) throw new BadRequestException("Please enter a id as a query parameter");
        return userService.getUser(id);
    }

    @PostMapping("/signup")
    public User userSignUp(@RequestBody User user) {
        if (user.getName().isEmpty()) throw new BadRequestException("Missing name");
        if (user.getEmail().isEmpty()) throw new BadRequestException("Missing email");
        if (user.getPassword().isEmpty()) throw new BadRequestException("Missing password");
        return userService.addNewUser(user);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userSignIn(@RequestBody SignInForm signInForm) {
        if (signInForm.getEmail().isEmpty()) throw new BadRequestException("Missing email");
        if (signInForm.getPassword().isEmpty()) throw new BadRequestException("Missing password");
        userService.userSignIn(signInForm.getEmail(), signInForm.getPassword());
    }

    @PutMapping("/changepassword/{id}")
    public User changePassword(@PathVariable Long id, @RequestBody ChangePasswordForm passwordForm) {
        return userService.userChangePassword(id, passwordForm.getOldPassword(), passwordForm.getNewPassword());
    }
}