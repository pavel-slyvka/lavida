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
import java.util.*;

/**
 * Created: 21:36 19.08.13
 *
 * @author Ruslan
 */
@Component
public class DeleteTagJob extends AbstractJob {

    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();

    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    @Resource
    private TagService tagService;

    @Resource
    private UserService userService;

    /**
     * Deletes  the TagJdo.
     */
    public void deleteTag() {
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
        String idNumber;
        int id;
        int[] tagsId;
        if (!isForbidden(userRoles)) {
            System.out.println(messageSource.getMessage("job.tag.delete.show.all", null, locale));
            List<TagJdo> tags = tagService.getAll();
            System.out.println(tags);
            tagsId = new int[tags.size()];
            for (int i = 0; i < tags.size(); i++) {
                tagsId[i] = tags.get(i).getId();
            }
            while (true) {
                System.out.println(messageSource.getMessage("job.tag.delete.chose.name", null, locale));
                idNumber = scanner.next().trim();
                if (idNumber.matches("\\d") && Arrays.asList(tagsId).contains(Integer.parseInt(idNumber))) {
                    id = Integer.parseInt(idNumber);
                    break;
                } else {
                    System.out.println(messageSource.getMessage("job.tag.delete.validation.wrong.id", null, locale));
                }
            }
            tagService.delete(id);
            System.out.println(messageSource.getMessage("job.google.change.credentials.successfully", null, locale));
        } else {
            System.out.println(messageSource.getMessage("job.google.change.credentials.exception.access.denied", null, locale));
            System.exit(1);

        }

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


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jobs.xml");
        DeleteTagJob job = context.getBean(DeleteTagJob.class);
        job.deleteTag();
    }

}
