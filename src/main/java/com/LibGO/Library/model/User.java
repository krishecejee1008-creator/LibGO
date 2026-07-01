package com.LibGO.Library.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String collageEmailID;
    private Long enrollmentID;
    private String name;
    private Boolean isActive;
    private String password;
    public enum UserType{
        STUDENT,
        FACULTY,
        STAFF,
        ADMIN
    }

    @Enumerated(EnumType.STRING)
    private UserType userType;
    private LocalDateTime joinedAt;

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public void setEnrollmentID(Long enrollmentID){
        this.enrollmentID = enrollmentID;
    }

    public void setUserType(UserType userType){
        this.userType = userType;
    }

    public void setCollageEmailID(String collageEmailID){
        this.collageEmailID = collageEmailID;
    }

    public String getCollageEmailID(){
        return collageEmailID;
    }

    public UserType getUserType(){
        return userType;
    }

    public Long getEnrollmentID() {
        return enrollmentID;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
