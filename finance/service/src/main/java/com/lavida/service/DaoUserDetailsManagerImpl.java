package com.lavida.service;

import com.lavida.service.dao.UserDao;
import com.lavida.service.entity.AuthorityJdo;
import com.lavida.service.entity.UserJdo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserJdo userJdo = userDao.getUserByLogin(username);
//
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(userJdo.getAuthorities().size());
//        for (AuthorityJdo authorityJdo : userJdo.getAuthorities()) {
//            authorities.addAll(AuthorityUtils.createAuthorityList(authorityJdo.getRole()));
//        }
//        return new User(userJdo.getLogin(), userJdo.getPassword(), authorities);
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        UserDetails userDetails = new User(username, "", Collections.<GrantedAuthority>emptyList());
        return new User(username, new Md5PasswordEncoder().encodePassword("password", saltSource.getSalt(userDetails)), AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"));
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

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
