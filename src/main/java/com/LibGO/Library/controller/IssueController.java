package com.LibGO.Library.controller;

import com.LibGO.Library.dto.IssueRequest;
import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.service.BookService;
import com.LibGO.Library.service.IssueService;
import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    IssueService issueService;

    @PostMapping("/issueHere")
    public ResponseEntity<?> createIssue(@RequestBody IssueRequest request){
        try {
            User user = userService.getUserById(request.getUserId()).get();
            Book book = bookService.getBookById(request.getBookId()).get();

            Issue issue = issueService.createIssue(user, book);

            return ResponseEntity.ok(issue);
        }
        catch (LibGOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/collect")
    public ResponseEntity<?> collectIssue(@RequestBody IssueRequest request){
        try {
            User user = userService.getUserById(request.getUserId()).get();
            Book book = bookService.getBookById(request.getBookId()).get();

            Issue issue = issueService.collectBook(user, book);

            return ResponseEntity.ok(issue);
        }
        catch (LibGOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/extend")
    public ResponseEntity<?> extendIssue(@RequestBody IssueRequest request){
        try {
            User user = userService.getUserById(request.getUserId()).get();
            Book book = bookService.getBookById(request.getBookId()).get();

            Issue issue = issueService.extendIssue(user, book);

            return ResponseEntity.ok(issue);
        }
        catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelIssue(@RequestBody IssueRequest request){
        try {
            User user = userService.getUserById(request.getUserId()).get();
            Book book = bookService.getBookById(request.getBookId()).get();

            Issue issue = issueService.cancelIssue(user, book);

            return ResponseEntity.ok(issue);
        }
        catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
