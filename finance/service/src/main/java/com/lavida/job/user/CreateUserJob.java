package com.lavida.job.user;

import com.lavida.job.AbstractJob;
import com.lavida.job.service.EmailService;
import com.lavida.service.UserService;
import com.lavida.service.settings.Settings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created: 0:06 11.08.13
 * The UserRegister is a job for registering new users from console.
 *
 * @author Ruslan
 */
@Component
public class CreateUserJob extends AbstractJob {
    public static final String ROLE_SELLER_LA_VIDA = "ROLE_SELLER_LA_VIDA";
    public static final String ROLE_SELLER_SLAVYANKA = "ROLE_SELLER_SLAVYANKA";
    public static final String ROLE_SELLER_NOVOMOSKOVSK = "ROLE_SELLER_NOVOMOSKOVSK";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";

    @Resource
    private UserService userService;

    @Resource
    private EmailService emailService;

    /**
     * Registers new userJdo saves it to database and sends mail with user's credentials to user's e-mail.
     *
     * @throws NoSuchElementException                               if no more tokens are available
     * @throws IllegalStateException                                if this scanner is closed
     * @throws IllegalArgumentException                             if the instance is not an
     *                                                              entity
     * @throws javax.persistence.TransactionRequiredException       if invoked on a
     *                                                              container-managed entity manager of type
     *                                                              <code>PersistenceContextType.TRANSACTION</code> and there is
     *                                                              no transaction
     * @throws org.springframework.mail.MailParseException          in case of failure when parsing the message
     * @throws org.springframework.mail.MailAuthenticationException in case of authentication failure
     * @throws org.springframework.mail.MailSendException           in case of failure when sending the message
     */
    public void registerUser() {
        String login, password, role, emailTo;
        Settings settings = settingsService.getSettings();
        Scanner scanner = new Scanner(System.in);

        System.out.println(messageSource.getMessage("job.user.create.enter.username", null, locale));
        login = scanner.next();
        System.out.println(messageSource.getMessage("job.user.create.enter.email", null, locale));
        emailTo = scanner.next();

        // password
        while (true) {
            System.out.println(messageSource.getMessage("job.user.create.enter.password", null, locale));
            password = scanner.next();
            System.out.println(messageSource.getMessage("job.user.create.confirm.password", null, locale));
            if (!password.equals(scanner.next())) {
                System.out.println(messageSource.getMessage("job.user.create.enter.password.error", null, locale));
                continue;
            }
            System.out.println(messageSource.getMessage("job.user.create.password.confirmed", null, locale));
            break;
        }

        // role
        while (true) {
            System.out.println(messageSource.getMessage("job.user.create.enter.role", null, locale));
            String roleNumber = scanner.next();
            if ("1".equals(roleNumber.trim())) {
                role = ROLE_SELLER_LA_VIDA;
                break;
            } else if ("2".equals(roleNumber.trim())) {
                role = ROLE_SELLER_SLAVYANKA;
                break;
            } else if ("3".equals(roleNumber.trim())) {
                role = ROLE_SELLER_NOVOMOSKOVSK;
                break;
            } else if ("4".equals(roleNumber.trim())) {
                role = ROLE_MANAGER;
                break;
            } else {
                System.out.println(messageSource.getMessage("job.user.create.enter.role.error", null, locale));
            }
        }

        // save and send email
        userService.save(login, password, true, Arrays.asList(role), emailTo);

        String emailFrom = settings.getEmail();
        String emailSubject = messageSource.getMessage("job.user.create.email.subject", null, locale);
        String dear = messageSource.getMessage("job.user.create.email.dear", null, locale);
        String yourPassword = messageSource.getMessage("job.user.create.email.password", null, locale);
        emailService.sendMail(emailFrom, emailTo, emailSubject,
                (dear + login + yourPassword + password + "\'."));
        System.out.println(messageSource.getMessage("job.user.create.password.sent", null, locale));
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jobs.xml");
        CreateUserJob job = context.getBean(CreateUserJob.class);
        job.registerUser();
    }
}
