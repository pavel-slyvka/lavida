package com.lavida.job.tag;

import com.lavida.job.AbstractJob;
import com.lavida.service.TagService;
import com.lavida.service.UserService;
import com.lavida.service.entity.TagJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Scanner;

/**
 * Created: 20:31 19.08.13
 * The CreateTagJob is a job for creating new tag.
 *
 * @author Ruslan
 */
@Component
public class CreateTagJob extends AbstractJob {

    @Resource
    private TagService tagService;

    @Resource
    private UserService userService;

    /**
     * Creates new TagJdo.
     */
    public void createTag() {
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;
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
            }
        }
        String uniqueName;
        String title;
        if (!userService.hasForbiddenRole()) {
            System.out.println(messageSource.getMessage("job.tag.create.uniqueName.enter", null, locale));
            uniqueName = scanner.next().trim();
            System.out.println(messageSource.getMessage("job.tag.create.title.enter", null, locale));
            title = scanner.next().trim();
            TagJdo newTag = new TagJdo(uniqueName, title, new Date(), true);
            tagService.save(newTag);
            System.out.println(messageSource.getMessage("job.google.change.credentials.successfully", null, locale));
        } else {
            System.out.println(messageSource.getMessage("job.google.change.credentials.exception.access.denied", null, locale));
            System.exit(1);

        }

    }


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jobs.xml");
        CreateTagJob job = context.getBean(CreateTagJob.class);
        job.createTag();
    }

}
