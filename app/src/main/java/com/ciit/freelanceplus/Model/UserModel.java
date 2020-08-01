package com.ciit.freelanceplus.Model;

public class UserModel {

    public String name, email,account_balance, user_id, user_role;
    public int id, verified;

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", account_balance='" + account_balance + '\'' +
                ", user_id='" + user_id + '\'' +
                ", id=" + id +
                '}';
    }
}
