package com.LibGO.Library.repository;

import com.LibGO.Library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCollageEmailID(String email);
    Optional<User> findByEnrollmentID(Long enrollmentID);
    Optional<User> findByJeeApplicationNumber(Long jeeApplicationNumber);

    // New query to look up users by their password reset token
    Optional<User> findByResetToken(String resetToken);
}