package com.lavida.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * SecurityMain
 * <p/>
 * Created: 23:58 02.08.13
 *
 * @author Pavel
 */
public class SecurityMain {

    @Secured("ROLE_ADMIN")
    public void securedMethod() {
        System.out.println("Entered!!!");
    }

    private static void login(ApplicationContext context, String username, String password) {
        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
        Authentication authenticationToken = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        login(context, "user12", "password");
//        login(context, "login1", "pass1");
        SecurityMain securityMain = context.getBean(SecurityMain.class);
        securityMain.securedMethod();
    }
}
