package com.lavida.service.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 01.08.13
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Authorities {
    @Id
    private int id;
    private String Role;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserJdo user;

    public Authorities() {
    }

    public Authorities(String role) {
        Role = role;
    }

    public Authorities(String role, UserJdo user) {
        Role = role;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public UserJdo getUser() {
        return user;
    }

    public void setUser(UserJdo user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authorities that = (Authorities) o;

        if (id != that.id) return false;
        if (!Role.equals(that.Role)) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Role.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "id=" + id +
                ", Role='" + Role + '\'' +
                ", user=" + user +
                '}';
    }
}
