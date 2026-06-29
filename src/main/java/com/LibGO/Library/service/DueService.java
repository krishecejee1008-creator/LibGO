package com.LibGO.Library.service;

import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.exception.NoOverDueException;
import com.LibGO.Library.model.Due;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import com.LibGO.Library.repository.DueRepository;
import com.LibGO.Library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DueService {

    @Autowired
    private DueRepository dueRepository;
    @Autowired
    private UserRepository userRepository;

    public Due createDue(User user, Issue issue) throws LibGOException {

        if (!LocalDateTime.now().isAfter(issue.getDueDate())) throw new NoOverDueException("Issue is not Over Due");

        Due due = new Due();

        due.setDueIssuer(user);
        due.setOverDue(issue);
        due.setOverDueDate(LocalDateTime.now());
        due.setClearedByAdmin(false);

        user.setActive(false);

        userRepository.save(user);

        return dueRepository.save(due);



    }

}
