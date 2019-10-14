package com.zwb.budget.entity;

import javax.persistence.*;

/**
 * Created by zwb
 * Time: 2019/10/12
 */
@Entity
@Table(name = "user")
public class UserInfo {
    @Id
    @GeneratedValue
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "factor_date")
    private String factorDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFactorDate() {
        return factorDate;
    }

    public void setFactorDate(String factorDate) {
        this.factorDate = factorDate;
    }
}
