package com.LibGO.Library.controller;

import com.LibGO.Library.dto.IssueRequest;
import com.LibGO.Library.exception.BookNotAvailableException;
import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.UserNotAvailableException;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Cart;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.service.BookService;
import com.LibGO.Library.service.CartService;
import com.LibGO.Library.service.IssueService;
import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private IssueService issueService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody IssueRequest request) {
        try {
            User user = userService.getUserById(request.getUserId())
                    .orElseThrow(() -> new UserNotAvailableException("Invalid UserID"));
            Book book = bookService.getBookById(request.getBookId())
                    .orElseThrow(() -> new BookNotAvailableException("Invalid BookID"));
            Cart cart = cartService.addToCart(user, book);
            return ResponseEntity.ok(cart);
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody IssueRequest request) {
        try {
            User user = userService.getUserById(request.getUserId())
                    .orElseThrow(() -> new UserNotAvailableException("Invalid UserID"));
            Book book = bookService.getBookById(request.getBookId())
                    .orElseThrow(() -> new BookNotAvailableException("Invalid BookID"));
            cartService.removeFromCart(user, book);
            return ResponseEntity.ok("Book removed from cart!");
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/myCart")
    public ResponseEntity<?> getCart(@RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new UserNotAvailableException("Invalid UserID"));
            return ResponseEntity.ok(cartService.getCart(user));
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/issueAll")
    public ResponseEntity<?> issueAll(@RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new UserNotAvailableException("Invalid UserID"));
            List<Cart> cartItems = cartService.getCart(user);
            List<Issue> issuedBooks = new ArrayList<>();
            for (Cart item : cartItems) {
                Issue issue = issueService.createIssue(user, item.getCartBook());
                issuedBooks.add(issue);
            }
            cartService.clearCart(user);
            return ResponseEntity.ok(issuedBooks);
        } catch (LibGOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}