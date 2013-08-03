package com.lavida.swing;

/**
 * The {@code UserValidationException} is used in {@code LoginForm}  by {@code SubmitButtonEventListener}
 * to validate input data.
 *
 * @author Ruslan
 * Created: 15:04 03.08.13
 */
public class UserValidationException extends Exception {
    public static final String NULL_PRINCIPAL_MESSAGE_RU = "Имя пользователя не должно быть пустым!";
    public static final String NULL_CREDENTIALS_MESSAGE_RU = "Пароль не должен быть пустым!";
    public static final String INCORRECT_CREDENTIALS_MESSAGE_RU = "Не верный пароль!";
    public static final String INCORRECT_PRINCIPAL_MESSAGE_RU = "Не верное имя пользователя!";
    public static final String INCORRECT_FORMAT_MESSAGE_RU = "Не правильный формат данных!";

    public UserValidationException() {
        super();
    }

    public UserValidationException(String message) {
        super(message);
    }
}
