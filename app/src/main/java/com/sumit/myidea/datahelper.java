package com.sumit.myidea;

public class datahelper {
    private String username;
    private String email;
    private String password;
    private String mobileno;

    public datahelper() {

    }

    public datahelper(String username, String email, String password, String mobileno) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMobileno() {
        return mobileno;
    }
}
