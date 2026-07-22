package com.LibGO.Library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String collageEmailID;
    private Long enrollmentID;
    private String firstName;
    private String lastName;
    private Boolean isActive;

    private Integer expPoints = 0;

    public enum Level {
        PAGE_TURNER,
        BOOKWORM,
        AVID_READER,
        SCHOLAR,
        BIBLIOPHILE,
        ARCHIVIST,
        SAGE,
        LUMINARY,
        GRAND_SCHOLAR,
        LIBRARIAN_SUPREME
    }

    @Enumerated(EnumType.STRING)
    private Level level = Level.PAGE_TURNER;

    @JsonIgnore
    private String password;

    public enum UserType {
        STUDENT,
        FACULTY,
        STAFF,
        ADMIN
    }

    private Long jeeApplicationNumber;

    public enum Branch {
        CSE,
        IT,
        ECE,
        IOT,
        EE,
        ME,
        CHE,
        BPHARM
    }

    @Enumerated(EnumType.STRING)
    private Branch branch;
    private Boolean isNewAdmission;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    private LocalDateTime joinedAt;

    // Existing Getters and Setters
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
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

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void setJeeApplicationNumber(Long jeeApplicationNumber) {
        this.jeeApplicationNumber = jeeApplicationNumber;
    }

    public void setNewAdmission(Boolean newAdmission) {
        isNewAdmission = newAdmission;
    }

    public Boolean getNewAdmission() {
        return isNewAdmission;
    }

    public Branch getBranch() {
        return branch;
    }

    public Long getJeeApplicationNumber() {
        return jeeApplicationNumber;
    }

    public Integer getExpPoints() { return expPoints; }
    public void setExpPoints(Integer expPoints) { this.expPoints = expPoints; }

    public Level getLevel() { return level; }
    public void setLevel(Level level) { this.level = level; }
}