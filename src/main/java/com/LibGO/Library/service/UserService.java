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

}
