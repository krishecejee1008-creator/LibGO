package com.LibGO.Library.dto;

public class UpdateUser {

    Long jeeApplicationNumber;
    String collegeEmailId;
    Long enrollmentID;

    public void setJeeApplicationNumber(Long jeeApplicationNumber) {
        this.jeeApplicationNumber = jeeApplicationNumber;
    }

    public void setCollegeEmailId(String collegeEmailId) {
        this.collegeEmailId = collegeEmailId;
    }

    public void setEnrollmentID(Long enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public Long getJeeApplicationNumber() {
        return jeeApplicationNumber;
    }

    public String getCollegeEmailId() {
        return collegeEmailId;
    }

    public Long getEnrollmentID() {
        return enrollmentID;
    }
}
