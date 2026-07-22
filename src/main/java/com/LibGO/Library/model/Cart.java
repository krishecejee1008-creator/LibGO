package com.LibGO.Library.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User cartOwner;

    @ManyToOne
    private Book cartBook;

    private LocalDateTime addedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCartOwner() { return cartOwner; }
    public void setCartOwner(User cartOwner) { this.cartOwner = cartOwner; }

    public Book getCartBook() { return cartBook; }
    public void setCartBook(Book cartBook) { this.cartBook = cartBook; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}