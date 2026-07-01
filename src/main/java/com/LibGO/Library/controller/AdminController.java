package com.LibGO.Library.controller;

import com.LibGO.Library.dto.IssueRequest;
import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Due;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.service.BookService;
import com.LibGO.Library.service.DueService;
import com.LibGO.Library.service.IssueService;
import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    DueService dueService;

    @Autowired
    IssueService issueService;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping("/issues/active")
    public List<Issue> getAllActiveIssues(){

        return issueService.getAllActiveIssues();

    }

    @GetMapping("/issues/pending")
    public List<Issue> getAllPendingIssues(){

        return issueService.getAllPendingIssues();

    }

    @GetMapping("/issues/dues")
    public List<Due> getAllDues(){

        return dueService.getAllDues();

    }

    @PutMapping("/issues/due/{id}/clear")
    public ResponseEntity<?> clearDue(@PathVariable Long id){
        try {
            return ResponseEntity.ok(dueService.clearDue(id));
        }
        catch (LibGOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/offlineIssue")
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

    @PostMapping("/addBooks")
    public Book addBook(@RequestBody Book book){
        return bookService.addBook(book);
    }

}
