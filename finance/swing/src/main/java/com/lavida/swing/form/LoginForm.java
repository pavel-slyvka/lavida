package com.lavida.swing.form;

import com.lavida.swing.exception.ExceptionHandler;
import com.lavida.swing.exception.SwingExceptionHandler;
import com.lavida.swing.handler.LoginFormHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

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

    @Resource
    private LoginFormHandler handler;

    @Resource
    private SwingExceptionHandler swingExceptionHandler;

    private JButton submitButton, cancelButton;
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
        form.setBounds(200, 200, 300, 200);
        form.setTitle(messageSource.getMessage("loginForm.form.title", null, localeHolder.getLocale()));
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }


    @Override
    protected void initializeComponents() {
        informPanel = new JPanel(new GridLayout(2, 1));
        informPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        instructionsLabel = new JLabel(messageSource.getMessage("loginForm.label.instruction.title", null, localeHolder.getLocale()));
        instructionsLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.doLayout();
        informPanel.add(instructionsLabel);
        informPanel.add(errorLabel);

        GridBagLayout bagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(2,5,2,5);
        credentialPanel = new JPanel(bagLayout);

        loginLabel = new JLabel(messageSource.getMessage("loginForm.label.login.title", null, localeHolder.getLocale()));
        loginLabel.setHorizontalAlignment(JLabel.RIGHT);
        loginLabel.setLabelFor(loginField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        credentialPanel.add(loginLabel, constraints);

        loginField = new JTextField(20);
        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    passwordField.requestFocusInWindow();
                }
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        credentialPanel.add(loginField, constraints);

        passwordLabel = new JLabel(messageSource.getMessage("loginForm.label.password.title", null, localeHolder.getLocale()));
        passwordLabel.setHorizontalAlignment(JLabel.RIGHT);
        passwordLabel.setLabelFor(passwordField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        credentialPanel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(20);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if ( key == KeyEvent.VK_ENTER) {
                    loginField.requestFocusInWindow();
                    submitButton.doClick();
                }
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        credentialPanel.add(passwordField, constraints);

        buttonPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        buttonPanel.setLayout(flowLayout);
        submitButton = new JButton(messageSource.getMessage("loginForm.button.login.title", null, localeHolder.getLocale()));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.submitButtonClicked(loginField.getText().trim(), new String(passwordField.getPassword()));
            }
        });
        cancelButton = new JButton(messageSource.getMessage("loginForm.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

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

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
        LoginForm form = context.getBean(LoginForm.class);
        form.show();
    }
}
