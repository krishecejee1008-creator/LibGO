package com.LibGO.Library.dto;

public class ResetPasswordRequest {

    private String email;
    private String otp;
    private String newPassword;
    private String confirmPassword;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }
}
