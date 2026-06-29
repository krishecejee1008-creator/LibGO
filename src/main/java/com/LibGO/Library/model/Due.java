package com.LibGO.Library.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "due")
public class Due {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User dueIssuer;

    @OneToOne
    private Issue overDue;
    private LocalDateTime overDueDate;
    private LocalDateTime returnDate;
    private Boolean isClearedByAdmin;

    public void setClearedByAdmin(Boolean clearedByAdmin) {
        isClearedByAdmin = clearedByAdmin;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOverDue(Issue overDue) {
        this.overDue = overDue;
    }

    public void setOverDueDate(LocalDateTime overDueDate) {
        this.overDueDate = overDueDate;
    }

    public void setDueIssuer(User dueIssuer) {
        this.dueIssuer = dueIssuer;
    }

    public User getDueIssuer() {
        return dueIssuer;
    }

    public long getId() {
        return id;
    }

    public Issue getOverDue() {
        return overDue;
    }

    public LocalDateTime getOverDueDate() {
        return overDueDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public Boolean getClearedByAdmin() {
        return isClearedByAdmin;
    }
}
