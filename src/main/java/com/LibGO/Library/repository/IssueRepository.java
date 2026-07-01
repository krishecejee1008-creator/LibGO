package com.LibGO.Library.repository;

import com.LibGO.Library.model.Book;
import com.LibGO.Library.model.Issue;
import com.LibGO.Library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    Optional<Issue> findByIssuerAndBookIssued(User issuer, Book bookIssued);
    List<Issue> findByCurrentStatus(Issue.CurrentStatus currentStatus);
}
