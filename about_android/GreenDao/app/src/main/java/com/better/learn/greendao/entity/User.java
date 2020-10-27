package com.better.learn.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

/**
 * 只对Java生效？？
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String password;

    //    @Generated(hash = 1691991404)
    @Keep
    public User(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    @Keep
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    //    @Generated(hash = 586692638)
    @Keep
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
