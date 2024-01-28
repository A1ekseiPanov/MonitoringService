package repository.memory;

import entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;

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
        var user = userRepository.findByUsername(USER1.getUsername());

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user).isEqualTo(saveUser);
    }

    @Test
    void findByUsernameNonExistentTest() {
        User retrievedUser = userRepository.findByUsername("n");

        Assertions.assertThat(retrievedUser).isNull();
    }

    @Test
    void updateTest() {
        User user = userRepository.save(USER1);
        User newUser = new User(USER2.getUsername(), USER2.getPassword());
        newUser.setId(user.getId());

        User updatedUser = userRepository.save(newUser);
        User retrievedUser = userRepository.findByUsername(USER2.getUsername());

        Assertions.assertThat(retrievedUser).isEqualTo(updatedUser);
        Assertions.assertThat(retrievedUser.getId()).isEqualTo(updatedUser.getId());
    }

    @Test
    void findByIdTest() {
        User user = userRepository.save(USER1);
        User retrievedUser = userRepository.findById(user.getId());

        Assertions.assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    void findByIdNonExistentTest() {
        User retrievedUser = userRepository.findById(123L);

        Assertions.assertThat(retrievedUser).isNull();
    }
}