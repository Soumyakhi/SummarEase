package com.pdfai.pdfai.entity;

import jakarta.persistence.*;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    private String uname;
    private String sub;
    @Column(unique = true)
    private String email;
    private String pfpLink;
    public String getPfpLink() {
        return pfpLink;
    }
    public void setPfpLink(String pfpLink) {
        this.pfpLink = pfpLink;
    }
    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
