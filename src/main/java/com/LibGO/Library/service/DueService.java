package com.LibGO.Library.service;

import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.IssueNotOverDueException;
import com.LibGO.Library.exception.OverDueNotFoundException;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Due;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.BookRepository;
import com.LibGO.Library.repository.DueRepository;
import com.LibGO.Library.repository.IssueRepository;
import com.LibGO.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DueService {

    @Autowired
    private DueRepository dueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private IssueRepository issueRepository;

    public Due createDue(User user, Issue issue) throws LibGOException {

        if (!LocalDateTime.now().isAfter(issue.getDueDate())) throw new IssueNotOverDueException("Issue is not Over Due");

        Due due = new Due();

        due.setDueIssuer(user);
        due.setOverDue(issue);
        due.setOverDueDate(LocalDateTime.now());
        due.setClearedByAdmin(false);

        user.setActive(false);

        userRepository.save(user);

        return dueRepository.save(due);

    }

    public Due clearDue(Long id) throws LibGOException{

        Due due = dueRepository.findById(id).orElseThrow(()-> new OverDueNotFoundException("No overdue Found"));

        due.setClearedByAdmin(true);
        due.setReturnDate(LocalDateTime.now());

        User user = due.getDueIssuer();
        user.setActive(true);

        userRepository.save(user);

        Issue issue = due.getOverDue();
        issue.setCurrentStatus(Issue.CurrentStatus.RETURNED);
        Book book = issue.getBookIssued();
        book.setAvailableCopies(book.getAvailableCopies()+1);

        bookRepository.save(book);
        issueRepository.save(issue);

        return dueRepository.save(due);

    }

    public List<Due> getAllDues(){

        return dueRepository.findAll();

    }

    public List<Due> myDue(User dueIssuer){

        return dueRepository.findDueByDueIssuer(dueIssuer);

    }

}
