package com.example.Testing.Repository;

import com.example.Testing.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Checks if a user exists with the given email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return True if a user exists with the given email and password, false otherwise.
     */
    User findByEmailAndPassword(String email, String password);

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return The user with the given email, or null if not found.
     */
    User findByEmail(String email);


}
