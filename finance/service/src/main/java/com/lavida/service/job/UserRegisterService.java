package com.lavida.service.job;

import com.lavida.service.UserService;
import com.lavida.service.email.EmailSender;
import com.lavida.service.settings.Settings;
import com.lavida.service.settings.SettingsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created: 0:06 11.08.13
 *
 * @author Ruslan
 */
@Service
public class UserRegisterService {
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
     * Registers new userJdo saves it to database and sends mail with user's credentials to user's e-mail.
     *
     * @throws NoSuchElementException   if no more tokens are available
     * @throws IllegalStateException    if this scanner is closed
     * @throws IllegalArgumentException if the instance is not an
     *                                  entity
     * @throws javax.persistence.TransactionRequiredException
     *                                  if invoked on a
     *                                  container-managed entity manager of type
     *                                  <code>PersistenceContextType.TRANSACTION</code> and there is
     *                                  no transaction
     */
    public void registerUser()
//            throws NoSuchElementException, IllegalStateException, IllegalArgumentException, TransactionRequiredException
    {
        String login, password, role, emailTo;
        Settings settings = settingsService.getSettings();
        String emailFrom = settings.getEmail();

        String emailSubject = messageSource.getMessage("service.user.register.email.subject", null, locale);
        String dear = messageSource.getMessage("service.user.register.email.dear", null, locale);
        String yourPassword = messageSource.getMessage("service.user.register.email.password", null, locale);

        Scanner scanner = new Scanner(System.in);
        System.out.println(messageSource.getMessage("service.user.register.enter.username", null, locale));
        login = scanner.next();
        System.out.println(messageSource.getMessage("service.user.register.enter.email", null, locale));
        emailTo = scanner.next();
        while (true) {
            System.out.println(messageSource.getMessage("service.user.register.enter.password", null, locale));
            String password1 = scanner.next();
            System.out.println(messageSource.getMessage("service.user.register.confirm.password", null, locale));
            String password2 = scanner.next();
            if (password1.equals(password2)) {
                password = password1;
                System.out.println(messageSource.getMessage("service.user.register.password.confirmed", null, locale));
                break;
            } else {
                System.out.println(messageSource.getMessage("service.user.register.enter.password.error", null, locale));
            }
        }

        while (true) {
            System.out.println(messageSource.getMessage("service.user.register.enter.role", null, locale));
            int roleNumber = scanner.nextInt();
            if (roleNumber == 1) {
                role = messageSource.getMessage("lavida.authority.seller", null, Locale.US);
                break;
            } else if (roleNumber == 2) {
                role = messageSource.getMessage("lavida.authority.manager", null, Locale.US);
                break;
            } else {
                System.out.println(messageSource.getMessage("service.user.register.enter.role.error", null, locale));
            }
        }

        userService.save(login, password, true, Arrays.asList(role), emailTo);
        emailSender.sendMail(emailFrom, emailTo, emailSubject,
                (dear + login + yourPassword + password + "\'."));
        System.out.println(messageSource.getMessage("service.user.register.password.sent", null, locale));
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        UserRegisterService registerService = context.getBean(UserRegisterService.class);
        registerService.registerUser();
    }
}
