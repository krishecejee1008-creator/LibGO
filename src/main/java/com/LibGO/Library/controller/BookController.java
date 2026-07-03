package com.LibGO.Library.controller;

import com.LibGO.Library.model.Book;
import com.LibGO.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public List<Book> getAllBook() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @GetMapping("/searchBookByName")
    public Optional<Book> getBookByName(@RequestParam String name){
        return bookService.searchBookByName(name);
    }

    @GetMapping("/searchBookByAuthor")
    public List<Book> getBookByAuthor(@RequestParam String name){
        return bookService.searchBookByAuthor(name);
    }

    @GetMapping("/searchBookByGenre")
    public List<Book> getBookByGenre(@RequestParam Book.Genre genre){
        return bookService.searchBookByGenre(genre);
    }

}

