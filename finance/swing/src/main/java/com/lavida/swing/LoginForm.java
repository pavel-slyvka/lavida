package com.lavida.swing;

import com.lavida.service.UserService;
import com.lavida.service.entity.UserJdo;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginForm allows users to enter the program LaVida.
 * If userName or password are not correct the error label will be shown.
 * <p/>
 * Created: 16:50 02.08.13
 *
 * @author Ruslan
 */
public class LoginForm extends JFrame {

    // Russian names for components.
    private static final String LOGIN_FORM_NAME_RU = "Вход в систему";
    private static final String LOGIN_BUTTON_RU = "Войти";
    private static final String LOGIN_LABEL_RU = "Имя пользователя:";
    private static final String PASSWORD_LABEL_RU = "Пароль:";
    private static final String INSTRUCTION_LABEL_RU = "Введите имя пользователя и пароль!";

    // Regular expression for checking input data in loginField and passwordField
    private static final String REGULAR_EXPRESSION_FOR_CREDENTIALS = "[A-Za-zА-Яа-я0-9.-_]*";
//    private static final String REGULAR_EXPRESSION_FOR_CREDENTIALS = "\w*\d*";

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

    public LoginForm() {
        super(LOGIN_FORM_NAME_RU);
        this.setBounds(200, 200, 400, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.LIGHT_GRAY);

        instructionsLabel = new JLabel(INSTRUCTION_LABEL_RU);
        loginLabel = new JLabel(LOGIN_LABEL_RU);
        passwordLabel = new JLabel(PASSWORD_LABEL_RU);
        loginField = new JTextField();
        passwordField = new JPasswordField();
        submitButton = new JButton(LOGIN_BUTTON_RU);
        submitButton.addActionListener(new SubmitButtonEventListener());

        errorLabel = new JLabel();
//        errorLabel.setText(errorMessage);
        errorLabel.setForeground(Color.RED);
        credentialPanel = new JPanel(new GridLayout(2, 2));

        // todo make spacing before
        credentialPanel.add(loginLabel);
        credentialPanel.add(loginField);
        credentialPanel.add(passwordLabel);
        credentialPanel.add(passwordField);

        informPanel = new JPanel(new GridLayout(2, 1));
        informPanel.add(instructionsLabel);
        informPanel.add(errorLabel);

        container.add(informPanel, BorderLayout.NORTH);
        container.add(credentialPanel, BorderLayout.CENTER);
        container.add(submitButton, BorderLayout.SOUTH);

        setVisible(true);
    }


    public static void main(String[] args) {
        LoginForm form = new LoginForm();
    }

    /**
     * EventListener for submit button checks user credentials from database "lavida".
     * If input fields are empty or incorrect the error message will be shown in error label.
     */
    private class SubmitButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            errorLabel.setText(null);

            try {
                validateCredentials(login, password);
                errorLabel.setText("hi!");
            } catch (UserValidationException e1) {
                errorMessage = e1.getMessage();
                errorLabel.setText(errorMessage);
            }
        }
    }

    /** Validates the input data in login form for not empty fields, correct format and existence of user
     * in database "lavida".
     *
     * @param login  the login text from loginField to be validated;
     * @param password the password text from passwordField to be validated$
     * @throws UserValidationException if login and/or password are null, if login and/or password
     * don't match regular expression   todo authentication
     */
    private void validateCredentials(String login, String password) throws UserValidationException {
        if (login == null || login.trim().isEmpty()) {
            throw new UserValidationException(UserValidationException.NULL_PRINCIPAL_MESSAGE_RU);
        } else if (password == null || password.trim().isEmpty()) {
            throw new UserValidationException(UserValidationException.NULL_CREDENTIALS_MESSAGE_RU);
        }else if (!login.matches(REGULAR_EXPRESSION_FOR_CREDENTIALS) || !password.matches(REGULAR_EXPRESSION_FOR_CREDENTIALS)) {
            throw new UserValidationException(UserValidationException.INCORRECT_FORMAT_MESSAGE_RU);
        }

        String filePath = "spring-context.xml";
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(filePath);
        UserService service = context.getBean("userService", UserService.class);
        UserJdo user = service.getByLogin(login);

        if (user == null) {
            throw new UserValidationException(UserValidationException.INCORRECT_PRINCIPAL_MESSAGE_RU);
        } else if (!user.getPassword().equals(password)) {
            throw new UserValidationException(UserValidationException.INCORRECT_CREDENTIALS_MESSAGE_RU);
        }

    }

}
