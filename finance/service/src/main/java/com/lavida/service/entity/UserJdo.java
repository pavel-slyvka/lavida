package com.lavida.service.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserJdo: Admin
 * Date: 01.08.13
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "users")
public class UserJdo {
    @Id
    private int id;
    private String login;
    private String password;
    private boolean enabled;
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Authorities> authoritieses = new ArrayList<Authorities>();

    public UserJdo() {
    }

    public UserJdo(String login, String password, boolean enabled, String name) {
        this.login = login;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Authorities> getAuthoritieses() {
        return authoritieses;
    }

    public void setAuthoritieses(List<Authorities> authoritieses) {
        this.authoritieses = authoritieses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserJdo userJdo = (UserJdo) o;

        if (id != userJdo.id) return false;
        if (!login.equals(userJdo.login)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + login.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserJdo{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", name='" + name + '\'' +
                ", authoritieses=" + authoritieses +
                '}';
    }
}
