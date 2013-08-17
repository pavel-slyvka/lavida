package com.lavida.service.settings;

import com.lavida.service.dao.GenericDao;
import com.lavida.service.entity.SettingsJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * SettingsService
 * <p/>
 * Created: 16:03 11.08.13
 *
 * @author Pavel
 */
@Service
public class SettingsService {

    @Resource
    private GenericDao<SettingsJdo> settingsDao;

    @Resource
    private SettingsTransformer settingsTransformer;

    public Settings getSettings() {
        List<SettingsJdo> settingsJdos = settingsDao.getAll(SettingsJdo.class);
        return settingsTransformer.transformSettingsJdoToSettings(settingsJdos);
    }

    @Transactional
    public void saveSettings(Settings settings) {
        List<SettingsJdo> settingsJdosToSave = settingsTransformer.transformSettingsToSettingsJdo(settings);
        List<SettingsJdo> settingsJdosCurrent = settingsDao.getAll(SettingsJdo.class);
        List<SettingsJdo> settingsJdosToDelete = new ArrayList<SettingsJdo>();
        for (SettingsJdo settingJdoCurrent : settingsJdosCurrent) {
            SettingsJdo settingJdoToSave = getSettingsJdoByKey(settingsJdosToSave, settingJdoCurrent.getKey());
            if (settingJdoToSave != null && settingJdoToSave.getId() == 0) {
                settingJdoToSave.setId(settingJdoCurrent.getId());
            } else {
                settingsJdosToDelete.add(settingJdoCurrent);
            }
        }

        // save/update:
        for (SettingsJdo settingsJdo : settingsJdosToSave) {
            if (settingsJdo.getId() == 0) {
                settingsDao.put(settingsJdo);
            } else {
                settingsDao.update(settingsJdo);
            }
        }

        // delete:
        for (SettingsJdo settingsJdo : settingsJdosToDelete) {
            settingsDao.delete(SettingsJdo.class, settingsJdo.getId());
        }
    }

    private SettingsJdo getSettingsJdoByKey(List<SettingsJdo> settingsJdos, String key) {
        for (SettingsJdo settingsJdo : settingsJdos) {
            if (settingsJdo.getKey().equals(key)) {
                return settingsJdo;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Settings settings = new Settings();
//        settings.setRemoteUser("remoteUser1");
//        settings.setRemotePass("remotePass1");
//        settings.setSpreadsheetName("spreadsheet1");
//        settings.setWorksheetNumber(1);

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        SettingsService settingsService = context.getBean(SettingsService.class);
//        settingsService.saveSettings(settings);
        System.out.println(settingsService.getSettings());
    }
}
