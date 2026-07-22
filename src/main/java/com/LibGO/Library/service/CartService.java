package com.LibGO.Library.service;

import com.LibGO.Library.exception.*;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Cart;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private IssueService issueService;

    public Cart addToCart(User user, Book book) throws LibGOException {

        if (cartRepository.findByCartOwnerAndCartBook(user, book).isPresent()) {
            throw new LibGOException("Book is already in your cart!");
        }

        int cartCount = cartRepository.countByCartOwner(user);
        List<Issue> activeIssues = issueService.myBooks(user);
        long activeCount = activeIssues.stream()
                .filter(i -> i.getCurrentStatus() == Issue.CurrentStatus.ACTIVE ||
                        i.getCurrentStatus() == Issue.CurrentStatus.PENDING)
                .count();

        if (cartCount + activeCount >= 6) {
            throw new LibGOException("You can have a maximum of 6 books at a time!");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No copies available for this book!");
        }

        Cart cart = new Cart();
        cart.setCartOwner(user);
        cart.setCartBook(book);
        cart.setAddedAt(LocalDateTime.now());

        return cartRepository.save(cart);
    }

    public void removeFromCart(User user, Book book) throws LibGOException {
        Cart cart = cartRepository.findByCartOwnerAndCartBook(user, book)
                .orElseThrow(() -> new LibGOException("Book not found in cart!"));
        cartRepository.delete(cart);
    }

    public List<Cart> getCart(User user) {
        return cartRepository.findByCartOwner(user);
    }

    public void clearCart(User user) {
        List<Cart> cartItems = cartRepository.findByCartOwner(user);
        cartRepository.deleteAll(cartItems);
    }
}