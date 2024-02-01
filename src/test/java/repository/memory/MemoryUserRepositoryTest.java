package repository.memory;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestData.USER1;
import static util.TestData.USER2;

class MemoryUserRepositoryTest {
    private UserRepository userRepository;

    @BeforeEach
    void initEach() {
        this.userRepository = MemoryUserRepository.getInstance();
    }

    @Test
    void saveAndGetByUsernameTest() {
        User saveUser = userRepository.save(USER1);
        Optional<User> user = userRepository.findByUsername(USER1.getUsername());

        assertThat(user).isNotNull();
        user.ifPresent(u -> assertThat(u).isEqualTo(saveUser));
    }

    @Test
    void findByUsernameNonExistentTest() {
        Optional<User> retrievedUser = userRepository.findByUsername("n");
        assertThat(retrievedUser).isEmpty();
    }

    @Test
    void updateTest() {
        User user = userRepository.save(USER1);
        User newUser = new User(USER2.getUsername(), USER2.getPassword());
        newUser.setId(user.getId());

        User updatedUser = userRepository.save(newUser);
        Optional<User> retrievedUser = userRepository.findByUsername(USER2.getUsername());

        retrievedUser.ifPresent(u -> {
            assertThat(u).isEqualTo(updatedUser);
            assertThat(u.getId()).isEqualTo(updatedUser.getId());
        });
    }

    @Test
    void findByIdTest() {
        User user = userRepository.save(USER1);
        Optional<User> retrievedUser = userRepository.findById(user.getId());

        retrievedUser.ifPresent(u -> assertThat(u).isEqualTo(user));
    }

    @Test
    void findByIdNonExistentTest() {
        Optional<User> retrievedUser = userRepository.findById(123L);

        assertThat(retrievedUser).isEmpty();
    }
}