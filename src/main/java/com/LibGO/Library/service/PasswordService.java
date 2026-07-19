package com.LibGO.Library.service;

import com.LibGO.Library.dto.ChangePasswordRequest;
import com.LibGO.Library.dto.ResetPasswordRequest;
import com.LibGO.Library.exception.*;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PasswordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    HashMap<String, String> otpStorage = new HashMap<>();

    public User changePassword(String email, ChangePasswordRequest request) throws LibGOException {
        User user = userRepository.findByCollageEmailID(email).orElseThrow(() -> new UserNotAvailableException("User is Not available"));
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())) throw new InvalidPasswordException("Entered wrong password");
        if (!request.getNewPassword().equals(request.getConfirmPassword())) throw new PasswordDontMatchException("Password dont match try again");
        user.setPassword(bCryptPasswordEncoder.encode(request.getConfirmPassword()));
        return userRepository.save(user);
    }

    public String sendOtp(String email) throws LibGOException {
        userRepository.findByCollageEmailID(email).orElseThrow(() -> new UserNotAvailableException("User is Not available"));
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpStorage.put(email, otp);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("LibGO Password Reset OTP");
        message.setText("Your OTP is: " + otp + "\nValid for 10 minutes only.");
        javaMailSender.send(message);
        return "OTP sent Successfully";
    }

    public User resetPassword(ResetPasswordRequest request) throws LibGOException {
        User user = userRepository.findByCollageEmailID(request.getEmail()).orElseThrow(() -> new UserNotAvailableException("User is Not Available"));
        if (otpStorage.get(user.getCollageEmailID()) == null) throw new OtpException("Please enter OTP");
        if (!otpStorage.get(user.getCollageEmailID()).equals(request.getOtp())) throw new OtpException("OTP doesnt match");
        if (!request.getNewPassword().equals(request.getConfirmPassword())) throw new PasswordDontMatchException("Password dont match try again");
        user.setPassword(bCryptPasswordEncoder.encode(request.getConfirmPassword()));
        otpStorage.remove(user.getCollageEmailID());
        return userRepository.save(user);
    }
}