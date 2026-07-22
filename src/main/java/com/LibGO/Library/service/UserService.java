package com.LibGO.Library.service;

import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.UserNotAvailableException;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        User user = userRepository.findByJeeApplicationNumber(applicationNumber)
                .orElseThrow(() -> new UserNotAvailableException("User Not Available"));
        user.setCollageEmailID(email);
        user.setEnrollmentID(enrollmentID);
        user.setNewAdmission(false);
        if (user.getFirstName() == null || enrollmentID == null) {
            throw new LibGOException("Cannot update: First name or Enrollment ID is missing.");
        }
        String updatePassword = user.getFirstName() + user.getEnrollmentID().toString();
        user.setPassword(passwordEncoder.encode(updatePassword));
        return userRepository.save(user);
    }

    public void updateLevel(User user) {
        int xp = user.getExpPoints();
        User.Level newLevel;

        if (xp >= 27500) newLevel = User.Level.LIBRARIAN_SUPREME;
        else if (xp >= 20000) newLevel = User.Level.GRAND_SCHOLAR;
        else if (xp >= 15000) newLevel = User.Level.LUMINARY;
        else if (xp >= 11000) newLevel = User.Level.SAGE;
        else if (xp >= 7500) newLevel = User.Level.ARCHIVIST;
        else if (xp >= 5000) newLevel = User.Level.BIBLIOPHILE;
        else if (xp >= 3000) newLevel = User.Level.SCHOLAR;
        else if (xp >= 1500) newLevel = User.Level.AVID_READER;
        else if (xp >= 500) newLevel = User.Level.BOOKWORM;
        else newLevel = User.Level.PAGE_TURNER;

        user.setLevel(newLevel);
        userRepository.save(user);
    }

    public void addXP(User user, int points) {
        user.setExpPoints(user.getExpPoints() + points);
        updateLevel(user);
    }
}