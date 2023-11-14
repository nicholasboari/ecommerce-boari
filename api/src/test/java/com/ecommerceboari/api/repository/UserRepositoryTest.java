package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.User;
import com.ecommerceboari.api.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Save creates user when Successful")
    @Test
    void save_PersistUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createValidUser();

        User userSaved = this.userRepository.save(userToBeSaved);

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getId()).isNotNull();
    }

    @DisplayName("Save updates user when Successful")
    @Test
    void update_ReplaceUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createValidUser();

        User userSaved = this.userRepository.save(userToBeSaved);

        userSaved.setEmail("updated-email@example.com");

        User userUpdated = this.userRepository.save(userSaved);

        Assertions.assertThat(userUpdated).isNotNull();
        Assertions.assertThat(userUpdated.getId()).isNotNull();
        Assertions.assertThat(userUpdated.getEmail()).isEqualTo(userSaved.getEmail());
    }

    @DisplayName("Delete removes user when Successful")
    @Test
    void delete_RemovesUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createValidUser();

        User userSaved = this.userRepository.save(userToBeSaved);

        this.userRepository.delete(userSaved);

        Optional<User> userOptional = this.userRepository.findById(userSaved.getId());

        Assertions.assertThat(userOptional).isNotPresent();
    }

    @DisplayName("Find by email returns user when Successful")
    @Test
    void findByEmail_ReturnsUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createValidUser();
        this.userRepository.save(userToBeSaved);

        Optional<User> foundUser = this.userRepository.findByEmail(userToBeSaved.getEmail());

        Assertions.assertThat(foundUser).isPresent().isEqualTo(userToBeSaved);
    }

    @DisplayName("Find by username returns user when Successful")
    @Test
    void findByUsername_ReturnsUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createValidUser();
        this.userRepository.save(userToBeSaved);

        Optional<User> foundUser = this.userRepository.findByUsername(userToBeSaved.getUsername());

        Assertions.assertThat(foundUser).isPresent().isEqualTo(userToBeSaved);
    }
}