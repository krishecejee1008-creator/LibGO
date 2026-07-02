package com.LibGO.Library.controller;

import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.UserNotAvailableException;
import com.LibGO.Library.model.Due;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.service.DueService;
import com.LibGO.Library.service.IssueService;
import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private DueService dueService;

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



}
