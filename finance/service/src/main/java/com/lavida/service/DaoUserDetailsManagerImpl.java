package com.lavida.service;

import com.lavida.service.dao.UserDao;
import com.lavida.service.entity.UserJdo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DaoUserDetailsManagerImpl
 * <p/>
 * Created: 23:37 02.08.13
 *
 * @author Pavel
 */
public class DaoUserDetailsManagerImpl implements UserDetailsManager {
    private AuthenticationManager authenticationManager;
    private UserDao userDao;

    public String encodePassword(String username, String rowPassword) {
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        UserDetails userDetails = new User(username, "", Collections.<GrantedAuthority>emptyList());
        return new Md5PasswordEncoder().encodePassword(rowPassword, saltSource.getSalt(userDetails));
    }

    public void login(String username, String rowPassword) {
        Authentication authenticationToken = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rowPassword));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public List<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        List<String> roles = new ArrayList<String>(authorities.size());
        for (SimpleGrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        return roles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJdo userJdo = userDao.getUserByLogin(username);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userJdo.getAuthoritiesArray());
        return new User(userJdo.getLogin(), userJdo.getPassword(), authorities);
//        ReflectionSaltSource saltSource = new ReflectionSaltSource();
//        saltSource.setUserPropertyToUse("username");
//        UserDetails userDetails = new User(username, "", Collections.<GrantedAuthority>emptyList());
//        return new User(username, new Md5PasswordEncoder().encodePassword("password", saltSource.getSalt(userDetails)), AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"));
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Not realized yet!");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Not realized yet!");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Not realized yet!");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not realized yet!");
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
