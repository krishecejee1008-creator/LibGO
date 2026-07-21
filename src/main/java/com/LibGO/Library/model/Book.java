package com.LibGO.Library.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book")
public class Book{

    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long callNumber;
   private String name;
   public enum Genre{
        CSE,
        IT,
        ECE,
        IOT,
        EE,
        ME,
        CHE,
        BPHARM,
        GENERAL
    }

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String authorBio;

    private String authorImageUrl;
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
   private Genre genre;
   private String author;
   private int totalCopies;
   private int availableCopies;
   private LocalDateTime addedAt;

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setCallNumber(Long callNumber) {
        this.callNumber = callNumber;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public Long getCallNumber() {
        return callNumber;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAuthorBio() { return authorBio; }
    public void setAuthorBio(String authorBio) { this.authorBio = authorBio; }

    public String getAuthorImageUrl() { return authorImageUrl; }
    public void setAuthorImageUrl(String authorImageUrl) { this.authorImageUrl = authorImageUrl; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
}
