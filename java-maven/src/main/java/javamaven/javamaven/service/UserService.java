package javamaven.javamaven.service;

import javamaven.javamaven.entity.User;
import javamaven.javamaven.exception.BadRequestException;
import javamaven.javamaven.exception.NotFoundException;
import javamaven.javamaven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public List<User> getAllUsers() { return (List<User>) userRepository.findAll(); }

    public User getUser(Long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) throw new NotFoundException("User not found");
        return userById.get();
    }

    public User addNewUser(User user) {
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            throw new BadRequestException("User already exist");
        }
        return userRepository.save(user);
    }

    public void userSignIn(String email, String password) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isEmpty()){
            throw new BadRequestException("Please check your email and password");
        }
        if (!userByEmail.get().getPassword().equals(password)) {
            throw new BadRequestException("Please check your email and password");
        }
    }

    public User userChangePassword(Long id, String oldPassword, String newPassword) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) throw new BadRequestException("User does not exist");
        if (userById.get().getPassword() != oldPassword) throw new BadRequestException("Old password does not match");
        userById.get().setPassword(newPassword);
        userRepository.save(userById.get());
        return userById.get();
    }
}
