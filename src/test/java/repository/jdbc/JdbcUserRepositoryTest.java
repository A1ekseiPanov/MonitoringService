package repository.jdbc;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.panov.domain.model.User;
import ru.panov.repository.UserRepository;
import util.TestcontainersAbstract;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestData.*;


class JdbcUserRepositoryTest extends TestcontainersAbstract {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Сохранение и получение пользователя по имени пользователя")
    void saveAndGetByUsernameTest() {
        User saveUser = userRepository.save(NEW_USER);
        Optional<User> user = userRepository.findByUsername(NEW_USER.getUsername());

        assertThat(user).isNotNull();
        user.ifPresent(u -> assertThat(u).isEqualTo(saveUser));
    }

    @Test
    @DisplayName("Поиск пользователя по несуществующему имени пользователя")
    void findByUsernameNonExistentTest() {
        Optional<User> retrievedUser = userRepository.findByUsername("not found");
        assertThat(retrievedUser).isEmpty();
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateTest() {
        User user = userRepository.findByUsername(USER1.getUsername()).orElse(null);
        User updated = UPDATED_USER;
        updated.setId(user.getId());

        User updatedUser = userRepository.update(user.getId(), updated);
        Optional<User> retrievedUser = userRepository.findByUsername(UPDATED_USER.getUsername());

        retrievedUser.ifPresent(u -> {
            assertThat(u).isEqualTo(updatedUser);
            assertThat(u.getId()).isEqualTo(updatedUser.getId());
        });
    }

    @Test
    @DisplayName("Поиск пользователя по идентификатору")
    void findByIdTest() {
        Optional<User> retrievedUser = userRepository.findById(USER2.getId());

        retrievedUser.ifPresent(u -> assertThat(u).isEqualTo(USER2));
    }

    @Test
    @DisplayName("Поиск пользователя по несуществующему идентификатору")
    void findByIdNonExistentTest() {
        Optional<User> retrievedUser = userRepository.findById(123L);

        assertThat(retrievedUser).isEmpty();
    }
}