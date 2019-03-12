package com.example.learnfirestore;

import java.io.Serializable;

public class StudentModel implements Serializable {
    private String fName, lName, email, regNo, pwd, imageUrl;

    public StudentModel(String fName, String lName, String email, String regNo, String pwd, String imageUrl) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.regNo = regNo;
        this.pwd = pwd;
        if (imageUrl != null) this.imageUrl = imageUrl;
        else this.imageUrl = "noavatar";
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
