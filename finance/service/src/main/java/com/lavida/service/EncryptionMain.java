package com.lavida.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * EncryptionMain
 * <p/>
 * Created: 0:02 11.08.13
 *
 * @author Pavel
 */
public class EncryptionMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        EncryptionService encryptionService = context.getBean(EncryptionService.class);
        String what = "something";
        String encrypted = encryptionService.encrypt(what);
        System.out.println("Encrypted: " + encrypted);

        DecryptionService decryptionService = context.getBean(DecryptionService.class);
        System.out.println("Decrypted: " + decryptionService.decrypt(encrypted));
    }
}
