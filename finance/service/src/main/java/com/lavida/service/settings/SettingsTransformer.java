package com.lavida.service.settings;

import com.lavida.service.DecryptionService;
import com.lavida.service.EncryptionService;
import com.lavida.service.entity.SettingsJdo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * SettingsTransformer
 * <p/>
 * Created: 17:13 11.08.13
 *
 * @author Pavel
 */
@Component
public class SettingsTransformer {
    private Logger logger = LoggerFactory.getLogger(SettingsTransformer.class);

    @Resource
    private DecryptionService decryptionService;

    @Resource
    private EncryptionService encryptionService;

    public Settings transformSettingsJdoToSettings(List<SettingsJdo> settingsJdos) {
        Settings settings = new Settings();
        for (SettingsJdo settingsJdo : settingsJdos) {
            if (!StringUtils.isEmpty(settingsJdo.getKey()) && !StringUtils.isEmpty(settingsJdo.getValue())) {
                try {
                    for (Field field : Settings.class.getDeclaredFields()) {
                        SettingsMapping settingsMapping = field.getAnnotation(SettingsMapping.class);
                        if (settingsMapping != null && settingsJdo.getKey().equals(settingsMapping.value())) {
                            String value = !settingsMapping.encrypted() ? settingsJdo.getValue()
                                    : decryptionService.decrypt(settingsJdo.getValue());
                            field.setAccessible(true);
                            if (int.class == field.getType()) {
                                field.setInt(settings, Integer.parseInt(value));
                            } else if (boolean.class == field.getType()) {
                                field.setBoolean(settings, Boolean.parseBoolean(value));
                            } else if (double.class == field.getType()) {
                                field.setDouble(settings, Double.parseDouble(value));
                            } else if (char.class == field.getType()) {
                                field.setChar(settings, value.charAt(0));
                            } else if (long.class == field.getType()) {
                                field.setLong(settings, Long.parseLong(value));
                            } else if (Integer.class == field.getType()) {
                                field.set(settings, Integer.parseInt(value));
                            } else if (Boolean.class == field.getType()) {
                                field.set(settings, Boolean.parseBoolean(value));
                            } else if (Double.class == field.getType()) {
                                field.set(settings, Double.parseDouble(value));
                            } else if (Character.class == field.getType()) {
                                field.set(settings, value.charAt(0));
                            } else if (Long.class == field.getType()) {
                                field.set(settings, Long.parseLong(value));
                            } else {
                                field.set(settings, value);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return settings;
    }

    public List<SettingsJdo> transformSettingsToSettingsJdo(Settings settings) {
        List<SettingsJdo> settingsJdos = new ArrayList<SettingsJdo>();
        for (Field field : Settings.class.getDeclaredFields()) {
            try {
                SettingsMapping settingsMapping = field.getAnnotation(SettingsMapping.class);
                if (settingsMapping != null && !StringUtils.isEmpty(settingsMapping.value())) {
                    field.setAccessible(true);
                    if (field.get(settings) == null) {
                        continue;
                    }
                    String value = field.get(settings).toString();
                    if (settingsMapping.encrypted()) {
                        value = encryptionService.encrypt(value);
                    }
                    settingsJdos.add(new SettingsJdo(settingsMapping.value(), value));
                }
            } catch (IllegalAccessException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return settingsJdos;
    }

    public void setDecryptionService(DecryptionService decryptionService) {
        this.decryptionService = decryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
}
