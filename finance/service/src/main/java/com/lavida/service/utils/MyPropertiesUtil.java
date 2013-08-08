package com.lavida.service.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created: 13:12 05.08.13
 *
 * @author Ruslan
 */
public class MyPropertiesUtil {
    public static String USER_NAME = "userNameGmail";
    public static String PASSWORD = "passwordGmail";

    public static void saveProperties (String userNameValue, String passwordValue, String filePath) {
        Properties properties = new Properties();
        properties.setProperty(USER_NAME, userNameValue);
        properties.setProperty(PASSWORD, passwordValue);
        try {
            properties.store(new FileOutputStream(new File(filePath)), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(filePath)));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return properties;
    }
    public static void main(String[] args) {
        String filePath ="D:/Projects/LaVida/gmail.properties";
        Properties properties = loadProperties(filePath);
        System.out.println(USER_NAME + " = " + properties.getProperty(USER_NAME));
        System.out.println(PASSWORD + " = " + properties.getProperty(PASSWORD));

    }

}
