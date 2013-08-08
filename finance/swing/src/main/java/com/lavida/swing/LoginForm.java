package com.lavida.swing;

import com.lavida.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

/**
 * LoginForm allows users to enter the program LaVida.
 * If userName or password are not correct the error label will be shown.
 * <p/>
 * Created: 16:50 02.08.13
 *
 * @author Ruslan
 */
public class LoginForm extends JFrame implements MessageSourceAware{

    // Russian names for components.
    private static final String LOGIN_FORM_NAME_RU = "Вход в систему";

//    private static final String LOGIN_BUTTON_RU = "Войти";
//    private static final String LOGIN_LABEL_RU = "Имя пользователя:";
//    private static final String PASSWORD_LABEL_RU = "Пароль:";
//    private static final String INSTRUCTION_LABEL_RU = "Введите имя пользователя и пароль!";

    // Regular expression for checking input data in loginField and passwordField
    private static final String REGULAR_EXPRESSION_FOR_CREDENTIALS = "[A-Za-zА-Яа-я0-9.-_]*";

    private Locale currentLocale = new Locale.Builder().setLanguage("ru").setRegion("RU").setScript("Cyrl").build();

    private String errorMessage;
    private JButton submitButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel instructionsLabel;
    private JLabel errorLabel;
    private JPanel informPanel;
    private JPanel credentialPanel;
    private JPanel buttonPanel;

    private MainApplicationWindow mainApplicationWindow;
    private UserService userService;
    private MessageSource messageSource;

    public LoginForm() {
        super(LOGIN_FORM_NAME_RU);
    }

    public void init() {
        this.setBounds(200, 200, 400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.LIGHT_GRAY);

        instructionsLabel = new JLabel(messageSource.getMessage("loginForm.label.instruction.title", null, currentLocale));
        loginLabel = new JLabel(messageSource.getMessage("loginForm.label.login.title", null, currentLocale));
        passwordLabel = new JLabel(messageSource.getMessage("loginForm.label.password.title", null, currentLocale));
        loginField = new JTextField();
        passwordField = new JPasswordField();
        submitButton = new JButton(messageSource.getMessage("loginForm.button.login.title", null, currentLocale));
        submitButton.addActionListener(new SubmitButtonEventListener());
        submitButton.setMnemonic(KeyEvent.VK_ENTER);  // Alt+Enter hot keys
        submitButton.setBackground(Color.orange);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);

        credentialPanel = new JPanel(new GridLayout(2, 2));
        credentialPanel.add(loginLabel);
        credentialPanel.add(loginField);
        credentialPanel.add(passwordLabel);
        credentialPanel.add(passwordField);
        credentialPanel.setBorder(new LineBorder(Color.ORANGE));
        credentialPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        informPanel = new JPanel(new GridLayout(2, 1));
        informPanel.add(instructionsLabel);
        informPanel.add(errorLabel);
        informPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));

        container.add(informPanel, BorderLayout.NORTH);
        container.add(credentialPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);


    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
        LoginForm form = context.getBean(LoginForm.class);
        form.setVisible(true);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * EventListener for submit button checks user credentials from database "lavida".
     * If input fields are empty or incorrect the error message will be shown in error label.
     */
    private class SubmitButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = loginField.getText().trim();
            String password = new String(passwordField.getPassword());
            errorLabel.setText(null);

            try {
                validateCredentials(login, password);
                userService.login(login, password);
                loginField.setText("");
                passwordField.setText("");
                mainApplicationWindow.filterByPermissions(mainApplicationWindow.getArticlesTable());
                mainApplicationWindow.setVisible(true);
                dispose();
            } catch (UserValidationException e1) {
                errorMessage = e1.getMessage();
                errorLabel.setText(errorMessage);
            }
        }
    }

    /**
     * Validates the input data in login form for not empty fields, correct format and existence of user
     * in database "lavida".
     *
     * @param login    the login text from loginField to be validated;
     * @param password the password text from passwordField to be validated;
     * @throws UserValidationException if login and/or password are null, if login and/or password
     *                                 don't match regular expression, if login and/or password don't match database values.
     */
    private void validateCredentials(String login, String password) throws UserValidationException {
        if (login == null || login.trim().isEmpty()) {
            throw new UserValidationException(UserValidationException.NULL_PRINCIPAL_MESSAGE_RU);
        } else if (password == null || password.trim().isEmpty()) {
            throw new UserValidationException(UserValidationException.NULL_CREDENTIALS_MESSAGE_RU);
        } else if (!login.matches(REGULAR_EXPRESSION_FOR_CREDENTIALS) || !password.matches(REGULAR_EXPRESSION_FOR_CREDENTIALS)) {
            throw new UserValidationException(UserValidationException.INCORRECT_FORMAT_MESSAGE_RU);
        }
//        UserJdo user = userService.getByLogin(login);
//        if (user == null) {
//            throw new UserValidationException(UserValidationException.INCORRECT_PRINCIPAL_MESSAGE_RU);
//        }

    }


    public void setMainApplicationWindow(MainApplicationWindow mainApplicationWindow) {
        this.mainApplicationWindow = mainApplicationWindow;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
