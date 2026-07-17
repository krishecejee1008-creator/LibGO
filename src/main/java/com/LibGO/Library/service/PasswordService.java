package com.LibGO.Library.service;

import com.LibGO.Library.dto.ChangePasswordRequest;
import com.LibGO.Library.dto.ResetPasswordRequest;
import com.LibGO.Library.exception.*;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;


@Service
public class PasswordService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    HashMap<String, String> otpStorage = new HashMap<>();

    public User changePassword(String email, ChangePasswordRequest request) throws LibGOException {

       User user = userRepository.findByCollageEmailID(email).orElseThrow(()-> new  UserNotAvailableException("User is Not available"));
       if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())) throw new InvalidPasswordException("Entered wrong password");

       if (!request.getNewPassword().equals(request.getConfirmPassword())) throw new PasswordDontMatchException("Password dont match try again");

       user.setPassword(bCryptPasswordEncoder.encode(request.getConfirmPassword()));

       return userRepository.save(user);

    }

    public String sendOtp(String email) throws LibGOException{

        User user = userRepository.findByCollageEmailID(email).orElseThrow(()-> new UserNotAvailableException("User is Not available"));

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        otpStorage.put(email, otp);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + resendApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"from\":\"LibGO <onboarding@resend.dev>\",\"to\":[\"" + email + "\"],\"subject\":\"LibGO Password Reset OTP\",\"text\":\"Your OTP is: " + otp + "\\nValid for 10 minutes only\"}";

        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(body, headers);
        restTemplate.postForObject("https://api.resend.com/emails", entity, String.class);

        String otpSent = "OTP sent Successfully";
        return otpSent;

    }

    public User resetPassword(ResetPasswordRequest request) throws LibGOException{

        User user = userRepository.findByCollageEmailID(request.getEmail()).orElseThrow(()-> new UserNotAvailableException("User is Not Available"));

        if(otpStorage.get(user.getCollageEmailID()) == null) throw new OtpException("Please enter OTP");
        if (!otpStorage.get(user.getCollageEmailID()).equals(request.getOtp())) throw new OtpException("OTP doesnt match");
        if (!request.getNewPassword().equals(request.getConfirmPassword()))throw new PasswordDontMatchException("Password dont match try again");

        user.setPassword(bCryptPasswordEncoder.encode(request.getConfirmPassword()));

        otpStorage.remove(user.getCollageEmailID());
        return userRepository.save(user);
    }

}


