package com.lavida.service.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    @GeneratedValue
    private int id;

    @Column(length = 32)
    private String login;

    @Column(length = 32)
    private String password;

    private boolean enabled;

    @Column(length = 32)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "authority", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority", length = 30)
    private Set<String> authorities = new HashSet<String>();

    public UserJdo() {
    }

    public UserJdo(String login, String password, boolean enabled) {
        this.login = login;
        this.password = password;
        this.enabled = enabled;
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

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthoritiesArray() {
        return authorities.toArray(new String[authorities.size()]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", authorities=" + authorities +
                '}';
    }
}
