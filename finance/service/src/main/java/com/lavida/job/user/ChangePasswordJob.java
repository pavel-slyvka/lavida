package com.lavida.job.user;

import com.lavida.job.AbstractJob;
import com.lavida.job.service.EmailService;
import com.lavida.service.UserService;
import com.lavida.service.entity.UserJdo;
import com.lavida.service.settings.Settings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * Created: 8:44 12.08.13
 * The UserPasswordChanger is a job for changing user's password from console.
 *
 * @author Ruslan
 */
@Component
public class ChangePasswordJob extends AbstractJob {

    @Resource
    private UserService userService;

    @Resource
    private EmailService emailService;

    /**
     * Changes user's password for La Vida.
     *
     * @throws javax.persistence.NoResultException                  if no entity found for query
     * @throws java.util.NoSuchElementException                     if no more tokens are available
     * @throws IllegalStateException                                if this scanner is closed
     * @throws IllegalArgumentException                             if the instance is not an
     *                                                              entity
     * @throws javax.persistence.TransactionRequiredException       if invoked on a
     *                                                              container-managed entity manager of type
     *                                                              <code>PersistenceContextType.TRANSACTION</code> and there is
     *                                                              no transaction
     * @throws org.springframework.mail.MailParseException          in case of failure when parsing the message
     * @throws org.springframework.mail.MailAuthenticationException in case of authentication failure
     * @throws org.springframework.mail.MailSendException
     */
    public void changePassword() {
        String login, password, emailTo;
        Settings settings = settingsService.getSettings();

        Scanner scanner = new Scanner(System.in);
        UserJdo currentUser;
        while (true) {
            System.out.println(messageSource.getMessage("job.user.create.enter.username", null, locale));
            login = scanner.next();
            try {
                currentUser = userService.getByLogin(login);
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println(messageSource.getMessage("job.user.change.password.error.no.result", null, locale));
                continue;
            }
            break;
        }
        emailTo = currentUser.getEmail();
        while (true) {
            System.out.println(messageSource.getMessage("job.user.create.enter.password", null, locale));
            password = scanner.next();
            System.out.println(messageSource.getMessage("job.user.create.confirm.password", null, locale));
            if (password.equals(scanner.next())) {
                System.out.println(messageSource.getMessage("job.user.create.password.confirmed", null, locale));
                break;

            } else {
                System.out.println(messageSource.getMessage("job.user.create.enter.password.error", null, locale));
            }
        }
        userService.updatePassword(currentUser, password);

        String emailFrom = settings.getEmail();
        String emailSubject = messageSource.getMessage("job.user.change.password.email.subject", null, locale);
        String dear = messageSource.getMessage("job.user.create.email.dear", null, locale);
        String yourPassword = messageSource.getMessage("job.user.change.password.email.password", null, locale);
        emailService.sendMail(emailFrom, emailTo, emailSubject,
                (dear + login + yourPassword + password + "\'."));
        System.out.println(messageSource.getMessage("job.user.create.password.sent", null, locale));
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jobs.xml");
        ChangePasswordJob job = context.getBean(ChangePasswordJob.class);
        job.changePassword();
    }
}
