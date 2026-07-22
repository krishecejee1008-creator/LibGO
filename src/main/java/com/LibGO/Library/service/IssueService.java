package com.LibGO.Library.service;

import com.LibGO.Library.exception.*;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.BookRepository;
import com.LibGO.Library.repository.CartRepository;
import com.LibGO.Library.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private BookRepository bookRepository;

    public Optional<Issue> findByBookAndIssuer(User user, Book book) {

        return issueRepository.findByIssuerAndBookIssued(user, book);

    }

    public Issue createIssue(User user, Book book) throws LibGOException {

        if (book.getAvailableCopies() == 0) {
            throw new BookNotAvailableException("The Book, " + book.getName() + ", is not Available");

        }

        if (!user.isActive()) {
            throw new UserBlockException("You," + user.getFirstName() + ", your book," + book.getName() + ", is due. Return to continue issuing, Thank you!");
        }

        Optional<Issue> existingIssue = findByBookAndIssuer(user, book);
        Issue issue;

        if (existingIssue.isPresent()) {
            Issue.CurrentStatus status = existingIssue.get().getCurrentStatus();
            if (status == Issue.CurrentStatus.PENDING || status == Issue.CurrentStatus.ACTIVE) {
                throw new BookAlreadyIssuedException("The Book, " + book.getName() + ", is already issued");
            }
            // Reuse expired/returned issue
            issue = existingIssue.get();
            issue.setDueDate(null);
            issue.setCollectedAt(null);
        } else {
            // Create new issue
            issue = new Issue();
            issue.setIssuer(user);
            issue.setBookIssued(book);
        }

        issue.setIssueDateTime(LocalDateTime.now());
        issue.setCurrentStatus(Issue.CurrentStatus.PENDING);
        issue.setCollected(false);
        issue.setExtended(false);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        cartRepository.findByCartOwnerAndCartBook(user, book)
                .ifPresent(cart -> cartRepository.delete(cart));
        return issueRepository.save(issue);
    }

    public Issue collectBook(User user, Book book) throws LibGOException {

        Issue issue = findByBookAndIssuer(user, book).orElseThrow(() -> new InvalidIssueStateException("Issue Not Found"));

        if (issue.getCurrentStatus() != Issue.CurrentStatus.PENDING)
            throw new InvalidIssueStateException("Issue is not in PENDING state");

        issue.setCollected(true);
        issue.setCollectedAt(LocalDateTime.now());
        issue.setDueDate(issue.getCollectedAt().plusMonths(3));
        issue.setCurrentStatus(Issue.CurrentStatus.ACTIVE);

        return issueRepository.save(issue);
    }

    public Issue extendIssue(User user, Book book) throws LibGOException {

        Issue issue = findByBookAndIssuer(user, book).orElseThrow(() -> new InvalidIssueStateException("Issue Not Found"));

        if (!issue.getCurrentStatus().equals(Issue.CurrentStatus.ACTIVE)) throw new InvalidIssueStateException("No issue ACTIVE");

        if (issue.isExtended()) throw new InvalidIssueStateException(("Your issue has already been extended"));

        if (issue.getDueDate().minusDays(7).isAfter(LocalDateTime.now())) throw new InvalidIssueStateException("Issue Extension Window not open");

        if (LocalDateTime.now().equals(issue.getDueDate()) || LocalDateTime.now().isAfter(issue.getDueDate())) throw new InvalidIssueStateException("Due date has been Passed, no extension Allowed");

        issue.setExtended(true);
        issue.setDueDate(issue.getDueDate().plusMonths(1));

        return issueRepository.save(issue);
    }

    public Issue cancelIssue(User user, Book book) throws LibGOException{

        Issue issue = findByBookAndIssuer(user, book).orElseThrow(()-> new InvalidIssueStateException("Issue Not Found"));

        if (!issue.getCurrentStatus().equals(Issue.CurrentStatus.PENDING)) throw new  InvalidIssueStateException("Cannot cancel — book already collected or issue expired");

        issue.setCurrentStatus(Issue.CurrentStatus.EXPIRED);
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);

        return issueRepository.save(issue);

    }

    public List<Issue> getAllActiveIssues(){

        return issueRepository.findByCurrentStatus(Issue.CurrentStatus.ACTIVE);

    }

    public List<Issue> getAllPendingIssues(){

        return issueRepository.findByCurrentStatus(Issue.CurrentStatus.PENDING);

    }

    public List<Issue> myBooks(User issuer){

        return issueRepository.findIssueByIssuer(issuer);

    }

    public Issue returnBook(User user, Book book) throws LibGOException {
        Issue issue = findByBookAndIssuer(user, book)
                .orElseThrow(() -> new InvalidIssueStateException("Issue Not Found"));

        if (!issue.getCurrentStatus().equals(Issue.CurrentStatus.ACTIVE))
            throw new InvalidIssueStateException("Book is not currently active");

        issue.setCurrentStatus(Issue.CurrentStatus.RETURNED);
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);
        return issueRepository.save(issue);
    }

}
