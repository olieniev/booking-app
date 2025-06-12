package com.olieniev.bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.olieniev.bookingapp.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("""
        Find user by email returns requested user
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void find_userByEmail_returnsExpectedUser() {
        User user = userRepository.findByEmail("user40@email.com").get();
        Long actual = user.getId();
        Long expected = 40L;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
        Check if user exists by existing email
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void check_userExistsByEmail_returnsTrue() {
        Boolean actual = userRepository.existsByEmail("user20@email.com");
        Boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
        Check if user NOT exists by inexisting email
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void check_userNotExistsByEmail_returnsFalse() {
        Boolean actual = userRepository.existsByEmail("notexist@email.com");
        Boolean expected = false;
        assertEquals(expected, actual);
    }
}
