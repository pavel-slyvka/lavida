package com.lavida.service.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@code UserJdo} is entity for users of application related to database.
 * <p/>
 * Created with IntelliJ IDEA.
 * UserJdo: Admin
 * Date: 01.08.13
 * Time: 16:46
 */

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = UserJdo.FIND_BY_LOGIN, query = "select u from UserJdo u where u.login = :login")
})
public class UserJdo {
    public static final String FIND_BY_LOGIN = "UserJdo.findByLogin";

    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true, length = 32)
    private String login;

    @Column(length = 32)
    private String password;

    @Column(length = 254)
    private String email;

    private boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getAuthorities() {
        return authorities;
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
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                '}';
    }
}
