package com.lavida.service;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Enumeration;
import java.util.Properties;

/**
 * DecryptPropertyConfigurer
 * <p/>
 * Created: 23:58 10.08.13
 *
 * @author Pavel
 */
public class DecryptPropertyConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected void convertProperties(Properties props) {
        Enumeration<?> propertyNames = props.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String propertyName = (String) propertyNames.nextElement();
            String propertyValue = props.getProperty(propertyName);
            DecryptionService decryptionService = new DecryptionService();
            decryptionService.init();
            String convertedValue = decryptionService.decrypt(propertyValue);
            if (!ObjectUtils.nullSafeEquals(propertyValue, convertedValue)) {
                props.setProperty(propertyName, convertedValue);
            }
        }
    }
}