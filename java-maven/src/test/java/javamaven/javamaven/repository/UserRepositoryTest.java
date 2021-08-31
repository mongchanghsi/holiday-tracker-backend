package javamaven.javamaven.repository;

import javamaven.javamaven.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepositoryTest;

    // Basic mock values
    private String mockName = "mockName";
    private String mockEmail = "mockEmail";
    private String mockPassword = "mockPassword";

    @AfterEach
    void tearDown() {
        userRepositoryTest.deleteAll();
    }

    @Test
    void itShouldFindUserByEmailIfExist() {
        User user = new User(mockName, mockEmail, mockPassword);

        userRepositoryTest.save(user);

        Optional<User> doesUserExist = userRepositoryTest.findUserByEmail(mockEmail);

        assertThat(doesUserExist.isPresent()).isTrue();
    }

    @Test
    void itShouldFindUserByEmailIfDoesNotExist() {
        Optional<User> doesUserExist = userRepositoryTest.findUserByEmail(mockEmail);

        assertThat(doesUserExist.isEmpty()).isTrue();
    }
}