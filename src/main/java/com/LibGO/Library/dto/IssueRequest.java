package com.LibGO.Library.dto;

public class IssueRequest {

    Long userId;
    Long bookId;

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getUserId() {
        return userId;
    }
}
