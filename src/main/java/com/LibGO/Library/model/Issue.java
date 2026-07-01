package com.LibGO.Library.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User issuer;

    @ManyToOne
    private Book bookIssued;
    private LocalDateTime issueDateTime;
    private LocalDateTime dueDate;

    public enum CurrentStatus {
        PENDING,
        ACTIVE,
        RETURNED,
        EXPIRED
    }

    @Enumerated(EnumType.STRING)
    private CurrentStatus currentStatus;
    private Boolean isCollected;
    private  LocalDateTime collectedAt;
    private Boolean isExtended;
    private LocalDateTime extendedAt;

    public void setId(long id) {
        this.id = id;
    }


    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setIssueDateTime(LocalDateTime issueDateTime) {
        this.issueDateTime = issueDateTime;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    public void setBookIssued(Book bookIssued) {
        this.bookIssued = bookIssued;
    }

    public void setIssuer(User issuer) {
        this.issuer = issuer;
    }

    public User getIssuer() {
        return issuer;
    }

    public Book getBookIssued() {
        return bookIssued;
    }

    public void setCollected(Boolean collected) {
        isCollected = collected;
    }

    public void setCollectedAt(LocalDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }

    public void setExtended(Boolean extended) {
        isExtended = extended;
    }

    public void setExtendedAt(LocalDateTime extendedAt) {
        this.extendedAt = extendedAt;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public Boolean isCollected() {
        return isCollected;
    }

    public Boolean isExtended() {
        return isExtended;
    }

    public LocalDateTime getExtendedAt() {
        return extendedAt;
    }
}

