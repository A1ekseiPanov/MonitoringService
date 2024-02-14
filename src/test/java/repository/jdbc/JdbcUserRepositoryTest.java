package repository.jdbc;

import entity.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestData.*;


class JdbcUserRepositoryTest extends TestcontainersAbstract {
    @Test
    void saveAndGetByUsernameTest() {
        User saveUser = userRepository.save(NEW_USER,connection);
        Optional<User> user = userRepository.findByUsername(NEW_USER.getUsername(),connection);

        assertThat(user).isNotNull();
        user.ifPresent(u -> assertThat(u).isEqualTo(saveUser));
    }

    @Test
    void findByUsernameNonExistentTest() {
        Optional<User> retrievedUser = userRepository.findByUsername("not found",connection);
        assertThat(retrievedUser).isEmpty();
    }

    @Test
    void updateTest() {
       User user = userRepository.findByUsername(USER1.getUsername(),connection).orElse(null);
       User updated = UPDATED_USER;
        updated.setId(user.getId());

        User updatedUser = userRepository.update(user.getId(),updated,connection);
        Optional<User> retrievedUser = userRepository.findByUsername(UPDATED_USER.getUsername(),connection);

        retrievedUser.ifPresent(u -> {
            assertThat(u).isEqualTo(updatedUser);
            assertThat(u.getId()).isEqualTo(updatedUser.getId());
        });
    }

    @Test
    void findByIdTest() {

        Optional<User> retrievedUser = userRepository.findById(USER2.getId(),connection);

        retrievedUser.ifPresent(u -> assertThat(u).isEqualTo(USER2));
    }

    @Test
    void findByIdNonExistentTest() {
        Optional<User> retrievedUser = userRepository.findById(123L,connection);

        assertThat(retrievedUser).isEmpty();
    }
}