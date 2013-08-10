package com.lavida.service;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * DecryptionService
 * <p/>
 * Created: 23:17 10.08.13
 *
 * @author Pavel
 */
@Service
public class DecryptionService {
    private static final String KEY = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAyIwbYJ1zU8siNuxbH7BXl0Ww4gGL\n" +
            "p1zNB9/vURd6n2E5eM3byJ85NkiXaqCbRMNKDD+6br6BTcHQ7Om42lXTWQIDAQABAkA8dShntu+m\n" +
            "ii11W0mZQwMU4niRmZ+tQ6e3wQWXb7mSxCb+wf3tVD1ljOG9HVLLB3QhQRSQWPMmRqjGVCYiQtwV\n" +
            "AiEA4qre7sc1UFmg3GVtoRtd7o5iV/N5BxS18K+57DIl5UMCIQDif+qxW7mQAKkJFsFyA21thOyC\n" +
            "JNoD1z8ErOtuFpxNMwIgC4RnI3bXzKdhNxGoSmFbmn19nb5vTd7Nh9dF+SxrJIUCIH6Pk4h/BHUe\n" +
            "qVVWzcjzeVXVDIK8LMYwpBnstbDSGYxZAiA4USXJrkpQ0vFU2/qYqhueIORwNOOzdAmoYOKpoTXD\n" +
            "+Q==";
    public static final String ALGORITHM = "RSA";

    private BASE64Decoder base64Decoder;
    private PrivateKey key;
    private Cipher cipher;

    @PostConstruct
    public void init() {
        try {
            base64Decoder = new BASE64Decoder();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(KEY));
            key = keyFactory.generatePrivate(keySpec);
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

        } catch (Exception e) {
            throw new RuntimeException(e);  // todo change exception
        }
    }

    public String decrypt(String code) {
        try {
            return new String(cipher.doFinal(base64Decoder.decodeBuffer(code))).intern();
        } catch (Exception e) {
            throw new RuntimeException(e);  // todo change exception
        }
    }
}
