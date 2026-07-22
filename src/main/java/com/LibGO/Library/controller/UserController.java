package com.LibGO.Library.controller;

import com.LibGO.Library.dto.LoginRequest;
import com.LibGO.Library.exception.InvalidPasswordException;
import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.UserNotAvailableException;
import com.LibGO.Library.model.Due;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.security.JwtUtil;
import com.LibGO.Library.service.DueService;
import com.LibGO.Library.service.IssueService;
import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private DueService dueService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/myBooks")
    public List<Issue> myBooks(@RequestParam Long userId) throws LibGOException{

        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotAvailableException("Invalid UserID"));
        return issueService.myBooks(user);

    }

    @GetMapping("/myDues")
    public List<Due> myDues(@RequestParam Long userID) throws LibGOException {

        User user = userService.getUserById(userID).orElseThrow(()-> new UserNotAvailableException("Invalid User ID"));
        return dueService.myDue(user);

    }

    @PostMapping("/login")
    public String loginRequest(@RequestBody LoginRequest loginRequest) throws LibGOException{

        User user = userService.getUserByEmail(loginRequest.getCollageEmailId()).orElseThrow(()-> new UserNotAvailableException("User Not Found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) throw new InvalidPasswordException("Password entered is Wrong");

        return jwtUtil.generateToken(loginRequest.getCollageEmailId(), user.getUserType());

    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestParam String email) throws LibGOException {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotAvailableException("User not found"));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String email) throws LibGOException {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotAvailableException("User not found"));
        return ResponseEntity.ok(user);
    }

}
