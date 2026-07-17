package com.LibGO.Library.service;

import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.UserNotAvailableException;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public User registerUser(User user) {
        user.setJoinedAt(LocalDateTime.now());
        user.setActive(true);
        String autoPassword = user.getFirstName() + user.getEnrollmentID().toString();
        user.setPassword(passwordEncoder.encode(autoPassword));
        return userRepository.save(user);
    }

    public User newAdmissionUser(User user) {
        user.setJoinedAt(LocalDateTime.now());
        user.setActive(true);
        user.setNewAdmission(true);
        String autoPassword = user.getJeeApplicationNumber().toString() + user.getBranch();
        user.setPassword(passwordEncoder.encode(autoPassword));
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByCollageEmailID(email);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEnrollmentID(Long enrollmentId){
        return userRepository.findByEnrollmentID(enrollmentId);
    }

    public Optional<User> getUserByJeeApplicationNumber(Long jeeApplicationNumber){
        return userRepository.findByJeeApplicationNumber(jeeApplicationNumber);
    }

    public User updateUser(Long applicationNumber, String email, Long enrollmentID) throws LibGOException {
        User user = userRepository.findByJeeApplicationNumber(applicationNumber).orElseThrow(()-> new UserNotAvailableException("User Not Available"));
        user.setCollageEmailID(email);
        user.setEnrollmentID(enrollmentID);
        user.setNewAdmission(false);
        if (user.getFirstName() == null || enrollmentID == null) {
            throw new LibGOException("Cannot update password: First name or Enrollment ID is missing.");
        }
        String updatePassword = user.getFirstName() + user.getEnrollmentID().toString();
        user.setPassword(passwordEncoder.encode(updatePassword));

        return userRepository.save(user);
    }

    // =========================================================================
    // FORGOT PASSWORD MECHANICS VIA GMAIL SMTP
    // =========================================================================

    public boolean processForgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByCollageEmailID(email);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Valid for 1 hour
        userRepository.save(user);

        sendResetEmail(user.getCollageEmailID(), token);
        return true;
    }

    private void sendResetEmail(String recipientEmail, String token) {
        String resetLink = "http://localhost:8080/password/reset?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailSenderAddress); // Reverted to match spring.mail.username exactly
        message.setTo(recipientEmail);
        message.setSubject("LibGO Library System - Password Reset Request");
        message.setText("Hello,\n\nYou requested a password reset for your LibGO account.\n"
                + "Please click the link below to verify your session and set a new password:\n\n"
                + resetLink + "\n\nThis security link will automatically expire in 1 hour.");

        mailSender.send(message);
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return false; // Token expired
        }

        // Encrypt password securely using your operational BCrypt encoder
        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear token fields to protect against link re-use vulnerabilities
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
        return true;
    }
}