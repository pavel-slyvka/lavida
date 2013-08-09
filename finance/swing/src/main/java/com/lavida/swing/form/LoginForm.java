package com.lavida.swing.form;

import com.lavida.swing.handler.LoginFormHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * LoginForm allows users to enter the program LaVida.
 * If userName or password are not correct the error label will be shown.
 * <p/>
 * Created: 16:50 02.08.13
 *
 * @author Ruslan
 */
@Component
public class LoginForm extends AbstractForm {

    // Russian names for components.
    private static final String LOGIN_FORM_NAME_RU = "Вход в систему";
    private static final String LOGIN_BUTTON_RU = "Войти";
    private static final String LOGIN_LABEL_RU = "Имя пользователя:";
    private static final String PASSWORD_LABEL_RU = "Пароль:";
    private static final String INSTRUCTION_LABEL_RU = "Введите имя пользователя и пароль!";

    @Resource
    private LoginFormHandler handler;

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

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setBounds(200, 200, 400, 200);
        form.setTitle(LOGIN_FORM_NAME_RU);
    }

    @Override
    protected void initializeComponents() {
        instructionsLabel = new JLabel(INSTRUCTION_LABEL_RU);
        loginLabel = new JLabel(LOGIN_LABEL_RU);
        passwordLabel = new JLabel(PASSWORD_LABEL_RU);
        loginField = new JTextField();
        passwordField = new JPasswordField();
        submitButton = new JButton(messageSource.getMessage("loginForm.button.login.title", null, locale));
        submitButton.setMnemonic(KeyEvent.VK_ENTER);  // Alt+Enter hot keys
        submitButton.setBackground(Color.orange);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.submitButtonClicked(loginField.getText().trim(), new String(passwordField.getPassword()));
            }
        });

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

        rootContainer.setLayout(new BorderLayout());
        rootContainer.setBackground(Color.LIGHT_GRAY);
        rootContainer.add(informPanel, BorderLayout.NORTH);
        rootContainer.add(credentialPanel, BorderLayout.CENTER);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void clearFields() {
        errorLabel.setText(null);
        loginField.setText("");
        passwordField.setText("");
    }

    public void showErrorMessage(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    public void setHandler(LoginFormHandler handler) {
        this.handler = handler;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
        LoginForm form = context.getBean(LoginForm.class);
        form.show();
    }
}
