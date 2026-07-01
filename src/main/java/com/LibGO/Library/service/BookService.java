package com.LibGO.Library.service;

import com.LibGO.Library.model.Book;
import com.LibGO.Library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book){

        book.setAddedAt(LocalDateTime.now());

        return bookRepository.save(book);

    }

    public Optional<Book> searchBookByName(String name){

        return bookRepository.findByName(name);
    }

    public Optional<Book> searchBookByGenre(Book.Genre genre){

        return bookRepository.findByGenre(genre);

    }

    public Optional<Book> searchBookByAuthor(String name){

        return bookRepository.findByAuthor(name);

    }

    public List<Book> getAllBooks(){

        return bookRepository.findAll();

    }

    public Optional<Book> getBookById(Long Id){

        return bookRepository.findById(Id);

    }

}

