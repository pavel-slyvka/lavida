package com.lavida.swing.handler;

import com.lavida.service.UserService;
import com.lavida.swing.dialog.SellDialog;
import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.exception.UserValidationException;
import com.lavida.swing.form.LoginForm;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * LoginFormHandler - handler for login form.
 * <p/>
 * Created: 20:08 08.08.13
 *
 * @author Pavel
 */
@Component
public class LoginFormHandler {

    // Regular expression for checking input data in loginField and passwordField
    private static final String REGULAR_EXPRESSION_FOR_CREDENTIALS = "[A-Za-zА-Яа-я0-9.-_]*";

    @Resource
    private LoginForm form;

    @Resource
    private UserService userService;

    @Resource
    private MainForm mainForm;

    /**
     * EventListener for submit button checks user credentials from database "lavida".
     * If input fields are empty or incorrect the error message will be shown in error label.
     */
    public void submitButtonClicked(String loginEntered, String passwordEntered) {
        try {
            form.clearFields();
            validateCredentials(loginEntered, passwordEntered);
            userService.login(loginEntered, passwordEntered);
            mainForm.filterTableByRoles(userService.getCurrentUserRoles());
            mainForm.filterTableDataByRole(userService.getCurrentUserRoles());
            mainForm.filterAnalyzePanelByRoles(userService.getCurrentUserRoles());
            mainForm.filterMenuBarByRoles(userService.getCurrentUserRoles());
            mainForm.removeFiltersByRoles(userService.getCurrentUserRoles());
            mainForm.initializeSellDialogByUser(userService.getCurrentUserRoles());
            mainForm.initializeArticleTableColumnLists();
            mainForm.show();
            form.hide();
        } catch (UserValidationException e1) {
            form.showErrorMessage(e1.getMessage());
        } catch (BadCredentialsException e) {
            form.showErrorMessage(e.getMessage());
        } catch (AuthenticationServiceException e) {
            form.showErrorMessage(e.getMessage());
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
    }
}
