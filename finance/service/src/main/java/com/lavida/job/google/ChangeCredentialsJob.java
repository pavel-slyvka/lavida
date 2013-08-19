package com.lavida.job.google;

import com.lavida.job.AbstractJob;
import com.lavida.service.UserService;
import com.lavida.service.entity.UserJdo;
import com.lavida.service.settings.Settings;
import com.lavida.service.settings.SettingsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created: 13:43 19.08.13
 * The ChangeCredentialsJob is a job for changing RemoteUser and RemotePass settings in the database.
 * @author Ruslan
 */
@Component
public class ChangeCredentialsJob extends AbstractJob {
    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();

    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    @Resource
    private UserService userService;

    @Resource
    private SettingsService settingsService;


    /**
     * Changes credentials to the google account in  the setting table of the database.
     */
    public void changeCredentials() {
        UserJdo user;
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;
        authentication:
        while (!authenticated) {
            System.out.println(messageSource.getMessage("job.google.change.credentials.enter.login", null, locale));
            String login = scanner.next().trim();
            System.out.println(messageSource.getMessage("job.google.change.credentials.enter.password", null, locale));
            String password = scanner.next().trim();
            try {
                userService.login(login, password);
                user = userService.getByLogin(login);
                authenticated = true;
            } catch (AuthenticationException e) {
                System.out.println(messageSource.getMessage("job.google.change.credentials.exception.wrong.user", null, locale));
                continue authentication;
            }
        }
        List<String> userRoles = userService.getCurrentUserRoles();
        String spreadsheetLogin;
        if (!isForbidden(userRoles)) {
            while (true) {
                System.out.println(messageSource.getMessage("job.google.change.credentials.enter.spreadsheet.login", null, locale));
                spreadsheetLogin = scanner.next().trim();
                if (spreadsheetLogin.endsWith("@gmail.com")) {
                    break;
                } else {
                    System.out.println(messageSource.getMessage("job.google.change.credentials.validation.wrong.spreadsheet.login", null, locale));
                }
            }
            System.out.println(messageSource.getMessage("job.google.change.credentials.enter.spreadsheet.password", null, locale));
            String spreadsheetPassword = scanner.next().trim();
            changeSettings(spreadsheetLogin, spreadsheetPassword);
            System.out.println(messageSource.getMessage("job.google.change.credentials.successfully", null, locale));
        } else {
            System.out.println(messageSource.getMessage("job.google.change.credentials.exception.access.denied", null, locale));
            System.exit(1);
        }
    }

    /**
     * Changes settings in the database
     * @param spreadsheetLogin the RemoteUser to be changed;
     * @param spreadsheetPassword the RemotePass to be changed.
     */
    private void changeSettings(String spreadsheetLogin, String spreadsheetPassword) {
        Settings currentSettings = settingsService.getSettings();
        currentSettings.setRemoteUser(spreadsheetLogin);
        currentSettings.setRemotePass(spreadsheetPassword);
        settingsService.saveSettings(currentSettings);
    }

    /**
     * Checks if current user has forbidden roles.
     * @param userRoles the List of roles
     * @return true if the user has at least one forbidden role.
     */
    private boolean isForbidden(List<String> userRoles) {
        for (String forbiddenRole : FORBIDDEN_ROLES) {
            if (userRoles.contains(forbiddenRole)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jobs.xml");
        ChangeCredentialsJob job = context.getBean(ChangeCredentialsJob.class);
        job.changeCredentials();
    }
}

