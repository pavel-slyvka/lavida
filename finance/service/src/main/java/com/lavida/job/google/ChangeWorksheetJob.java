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
 * Created: 16:36 19.08.13
 *
 * @author Ruslan
 */
@Component
public class ChangeWorksheetJob extends AbstractJob {
    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();

    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    @Resource
    private UserService userService;

    @Resource
    private SettingsService settingsService;

    /**
     * Changes settings in the database
     *
     * @param spreadsheetName the RemoteUser to be changed;
     * @param worksheetNumber the RemotePass to be changed.
     */
    private void changeSettings(String spreadsheetName, int worksheetNumber) {
        Settings currentSettings = settingsService.getSettings();
        currentSettings.setSpreadsheetName(spreadsheetName);
        currentSettings.setWorksheetNumber(worksheetNumber);
        settingsService.saveSettings(currentSettings);
    }

    /**
     * Checks if current user has forbidden roles.
     *
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

    /**
     * Changes spreadsheetName and worksheetNumber in dialog from console.
     */
    private void changeWorksheet() {
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
                authenticated = true;
            } catch (AuthenticationException e) {
                System.out.println(messageSource.getMessage("job.google.change.credentials.exception.wrong.user", null, locale));
                continue authentication;
            }
        }
        List<String> userRoles = userService.getCurrentUserRoles();
        if (!isForbidden(userRoles)) {
            System.out.println(messageSource.getMessage("job.google.change.spreadsheet.title.enter", null, locale));
            String spreadsheetTitle = scanner.next().trim();
            String index;
            int worksheetIndex;
            while (true) {
                System.out.println(messageSource.getMessage("job.google.change.spreadsheet.worksheet.index.enter", null, locale));
                index = scanner.next().trim();
                if (index.matches("\\d") && Integer.parseInt(index) > 0) {
                    worksheetIndex = Integer.parseInt(index);
                    break;
                } else {
                    System.out.println(messageSource.getMessage("job.google.change.spreadsheet.validation.wrong.worksheet.index", null, locale));

                }
            }
            changeSettings(spreadsheetTitle, worksheetIndex);
            System.out.println(messageSource.getMessage("job.google.change.credentials.successfully", null, locale));
        } else {
            System.out.println(messageSource.getMessage("job.google.change.credentials.exception.access.denied", null, locale));
            System.exit(1);
        }

    }


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jobs.xml");
        ChangeWorksheetJob job = context.getBean(ChangeWorksheetJob.class);
        job.changeWorksheet();
    }


}
