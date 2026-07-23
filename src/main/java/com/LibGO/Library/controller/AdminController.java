package com.LibGO.Library.controller;

import com.LibGO.Library.dto.IssueRequest;
import com.LibGO.Library.dto.UpdateUser;
import com.LibGO.Library.exception.BookNotAvailableException;
import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.UserNotAvailableException;
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
import java.util.Optional;

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
            User user = userService.getUserById(request.getUserId()).orElseThrow(()-> new UserNotAvailableException("Invalid UserID"));
            Book book = bookService.getBookById(request.getBookId()).orElseThrow(()-> new UserNotAvailableException("Invalid UserID"));

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

    @GetMapping("/searchUserByEmail")
    public Optional<User> getByEmail(@RequestParam String email){

        return userService.getUserByEmail(email);

    }

    @GetMapping("/searchUserByEnrollmentId")
    public Optional<User> getByEnrollmentId(@RequestParam Long enrollmentId){

        return userService.getUserByEnrollmentID(enrollmentId);

    }

    @PostMapping("/register")
    public User registerNewUser(@RequestBody User user){

        return userService.registerUser(user);

    }

    @PostMapping("/registerNewAdmission")
    public User registerNewAdmission(@RequestBody User user){

        return userService.newAdmissionUser(user);

    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser request){

        try {
            return ResponseEntity.ok(userService.updateUser(request.getJeeApplicationNumber(),request.getCollegeEmailId(),request.getEnrollmentID()));
        }
        catch (LibGOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/return")
    public ResponseEntity<?> returnIssue(@RequestBody IssueRequest request) {
        try {
            User user = userService.getUserById(request.getUserId())
                    .orElseThrow(() -> new UserNotAvailableException("Invalid UserID"));
            Book book = bookService.getBookById(request.getBookId())
                    .orElseThrow(() -> new BookNotAvailableException("Invalid BookID"));

            Issue issue = issueService.returnBook(user, book);
            return ResponseEntity.ok(issue);
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/searchUserByJeeApplicationNumber")
    public Optional<User> getByJeeApplicationNumber(@RequestParam Long jeeApplicationNumber) {
        return userService.getUserByJeeApplicationNumber(jeeApplicationNumber);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book updated = bookService.updateBookById(id, book);
            return ResponseEntity.ok(updated);
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/issues/collect")
    public ResponseEntity<?> collectIssue(@RequestBody IssueRequest request) {
        try {
            User user = userService.getUserById(request.getUserId())
                    .orElseThrow(() -> new UserNotAvailableException("Invalid UserID"));
            Book book = bookService.getBookById(request.getBookId())
                    .orElseThrow(() -> new BookNotAvailableException("Invalid BookID"));
            Issue issue = issueService.collectBook(user, book);
            return ResponseEntity.ok(issue);
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}