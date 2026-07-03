package com.LibGO.Library.scheduler;

import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Due;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.repository.IssueRepository;
import com.LibGO.Library.service.BookService;
import com.LibGO.Library.service.DueService;
import com.LibGO.Library.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LibraryScheduler {

    @Autowired
    DueService dueService;

    @Autowired
    IssueService issueService;

    @Autowired
    BookService bookService;
    @Autowired
    private IssueRepository issueRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoExpiredUncollectedBook(){

        List<Issue> pendingIssues = issueService.getAllPendingIssues();

        for (Issue issue : pendingIssues){

            if (LocalDateTime.now().isAfter(issue.getIssueDateTime().plusDays(4))){

                issue.setCurrentStatus(Issue.CurrentStatus.EXPIRED);
                Book book = issue.getBookIssued();
                book.setAvailableCopies(book.getAvailableCopies()+1);
                bookService.updateBook(book);
                issueRepository.save(issue);

            }

        }
   }

   @Scheduled(cron = "0 0 0 * * *")
    public void blockDueUsers(){
        List<Issue> activeIssues = issueService.getAllActiveIssues();

        for (Issue issue : activeIssues){

            if (LocalDateTime.now().isAfter(issue.getDueDate())){

                try {
                    dueService.createDue(issue.getIssuer(), issue);
                }
                catch (LibGOException e){
                    System.err.println(e.getMessage());
                }

            }

        }

   }

}
