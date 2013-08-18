package com.lavida.job.user;

import com.lavida.job.AbstractJob;
import com.lavida.service.UserService;
import com.lavida.service.entity.UserJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * ActivateUserJob
 * Created: 10:06 18.08.13
 *
 * @author Pavel
 */
@Component
public class ActivateUserJob extends AbstractJob {

    @Resource
    private UserService userService;

    public void activateUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(messageSource.getMessage("job.user.activate.activate.enter.login", null, locale));
        String login = scanner.next();
        System.out.println(messageSource.getMessage("job.user.activate.enter.password", null, locale));
        String password = scanner.next();

        UserJdo userJdo = userService.getByLogin(login.trim());
        if (userJdo != null && userJdo.getPassword().equals(password)) {
            userJdo.setEnabled(true);
            userService.update(userJdo);
            System.out.println(messageSource.getMessage("job.user.activate.activate.success", null, locale));
        } else {
            System.out.println(messageSource.getMessage("job.user.activate.error.wrong.enter", null, locale));
        }
    }

    public void deactivateUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(messageSource.getMessage("job.user.activate.deactivate.enter.login", null, locale));
        String login = scanner.next();
        System.out.println(messageSource.getMessage("job.user.activate.enter.password", null, locale));
        String password = scanner.next();

        UserJdo userJdo = userService.getByLogin(login.trim());
        if (userJdo != null && userJdo.getPassword().equals(password)) {
            userJdo.setEnabled(false);
            userService.update(userJdo);
            System.out.println(messageSource.getMessage("job.user.activate.deactivate.success", null, locale));
        } else {
            System.out.println(messageSource.getMessage("job.user.activate.error.wrong.enter", null, locale));
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jobs.xml");
        ActivateUserJob job = context.getBean(ActivateUserJob.class);

        if (args.length > 0 && "activate".equals(args[0])) {
            job.activateUser();

        } else if (args.length > 0 && "deactivate".equals(args[0])) {
            job.deactivateUser();
        }
    }
}
