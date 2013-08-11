package com.lavida.service;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * EncryptionService
 * <p/>
 * Created: 23:16 10.08.13
 *
 * @author Pavel
 */
@Service
public class EncryptionService {
    private static final String KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMiMG2Cdc1PLIjbsWx+wV5dFsOIBi6dczQff71EXep9h\n" +
            "OXjN28ifOTZIl2qgm0TDSgw/um6+gU3B0OzpuNpV01kCAwEAAQ==";
    public static final String ALGORITHM = "RSA";

    private BASE64Encoder base64Encoder;
    private Cipher cipher;

    @PostConstruct
    public void init() {
        try {
            base64Encoder = new BASE64Encoder();
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(KEY));
            Key key = keyFactory.generatePublic(keySpec);
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

        } catch (Exception e) {
            throw new RuntimeException(e);  // todo change exception
        }
    }

    public String encrypt(String data) {
        try {
            return base64Encoder.encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);  // todo change exception
        }
    }
}
