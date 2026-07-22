package com.LibGO.Library.repository;

import com.LibGO.Library.model.Cart;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCartOwner(User user);
    Optional<Cart> findByCartOwnerAndCartBook(User user, Book book);
    int countByCartOwner(User user);
}