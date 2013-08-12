package com.lavida.service.job;

import com.lavida.service.UserService;
import com.lavida.service.email.EmailSender;
import com.lavida.service.entity.UserJdo;
import com.lavida.service.settings.Settings;
import com.lavida.service.settings.SettingsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created: 8:44 12.08.13
 * The UserPasswordChanger is a job for changing user's password from console.
 *
 * @author Ruslan
 */
@Service
public class UserPasswordChanger {
    protected Locale locale = new Locale.Builder().setLanguage("ru").setRegion("RU").setScript("Cyrl").build();

    @Resource
    private UserService userService;
    @Resource
    private EmailSender emailSender;
    @Resource
    SettingsService settingsService;
    @Resource
    protected MessageSource messageSource;


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Changes user's password for La Vida.
     *
     * @throws javax.persistence.NoResultException
     *                                  if no entity found for query
     * @throws java.util.NoSuchElementException
     *                                  if no more tokens are available
     * @throws IllegalStateException    if this scanner is closed
     * @throws IllegalArgumentException if the instance is not an
     *                                  entity
     * @throws javax.persistence.TransactionRequiredException
     *                                  if invoked on a
     *                                  container-managed entity manager of type
     *                                  <code>PersistenceContextType.TRANSACTION</code> and there is
     *                                  no transaction
     * @throws org.springframework.mail.MailParseException
     *                                  in case of failure when parsing the message
     * @throws org.springframework.mail.MailAuthenticationException
     *                                  in case of authentication failure
     * @throws org.springframework.mail.MailSendException
     *
     */
    public void changePassword() {
        String login, password, role, emailTo;
        Settings settings = settingsService.getSettings();
        String emailFrom = settings.getEmail();

        String emailSubject = messageSource.getMessage("job.user.change.password.email.subject", null, locale);
        String dear = messageSource.getMessage("job.user.register.email.dear", null, locale);
        String yourPassword = messageSource.getMessage("job.user.change.password.email.password", null, locale);

        Scanner scanner = new Scanner(System.in);
        UserJdo currentUser = null;
        while (true) {
            System.out.println(messageSource.getMessage("job.user.register.enter.username", null, locale));
            login = scanner.next();
            try {
            currentUser = userService.getByLogin(login);
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println(messageSource.getMessage("job.user.change.password.exception.no.result", null, locale));
                continue;
            }
            break;
        }
        emailTo = currentUser.getEmail();
        while (true) {
            System.out.println(messageSource.getMessage("job.user.register.enter.password", null, locale));
            String password1 = scanner.next();
            System.out.println(messageSource.getMessage("job.user.register.confirm.password", null, locale));
            String password2 = scanner.next();
            if (password1.equals(password2)) {
                password = password1;
                System.out.println(messageSource.getMessage("job.user.register.password.confirmed", null, locale));
                break;
            } else {
                System.out.println(messageSource.getMessage("job.user.register.enter.password.error", null, locale));
            }
        }
        userService.updatePassword(currentUser, password);
        emailSender.sendMail(emailFrom, emailTo, emailSubject,
                (dear + login + yourPassword + password + "\'."));
        System.out.println(messageSource.getMessage("job.user.register.password.sent", null, locale));

    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        UserPasswordChanger passwordChanger = context.getBean(UserPasswordChanger.class);
        passwordChanger.changePassword();

    }
}
