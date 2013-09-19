package com.lavida.service;

import com.lavida.service.settings.user.ColumnSettings;
import com.lavida.service.settings.user.PresetSettings;
import com.lavida.service.settings.user.TableSettings;
import com.lavida.service.settings.user.UsersSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Arrays;

/**
 * The service for the {@link com.lavida.service.settings.user.UsersSettings }.
 * Created: 16:38 18.09.13
 *
 * @author Ruslan
 */
@Service
public class UserSettingsService {

    private File settingsFile;

    public void saveSettings(UsersSettings userSettings) throws IOException, JAXBException {
        OutputStream outputStream = new FileOutputStream(settingsFile);
        JAXBContext context = JAXBContext.newInstance(UsersSettings.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(userSettings, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public UsersSettings getSettings() throws JAXBException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(settingsFile);
        JAXBContext context = JAXBContext.newInstance(UsersSettings.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (UsersSettings) unmarshaller.unmarshal(inputStream);
    }

    @PostConstruct
    public void init() {
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "finance.settings.xml";
        settingsFile = new File(filePath);
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public static void main(String[] args) {
        UsersSettings settings = new UsersSettings();
        PresetSettings defaultPresetSettings = new PresetSettings();
        defaultPresetSettings.setName("defaultSettings");
        defaultPresetSettings.setArticlesTableSettings(new TableSettings(Arrays.asList(
                new ColumnSettings("Код", 0, 75),
                new ColumnSettings("Наименование товара", 1, 250),
                new ColumnSettings("Бренд", 2, 100),
                new ColumnSettings("Кол-во", 3, 75),
                new ColumnSettings("Размер", 4, 65),
                new ColumnSettings("Дата поставки", 5, 100),
                new ColumnSettings("Закупочная цена, евро", 6, 150),
                new ColumnSettings("Транспортные расхды, евро", 7, 175),
                new ColumnSettings("Себестоимость, евро", 8, 125),
                new ColumnSettings("Себестоимость, грн", 9, 120),
                new ColumnSettings("Коэффициент умножения", 10, 150),
                new ColumnSettings("Рассчитанная цена продажи, грн", 11, 200),
                new ColumnSettings("Цена продажи, грн", 12, 115),
                new ColumnSettings("Цена поднятая, грн", 13, 120),
                new ColumnSettings("Цена продажи старая, грн", 14, 160),
                new ColumnSettings("Своим", 16, 100),
                new ColumnSettings("Дата продажи", 17, 100),
                new ColumnSettings("Дата возврата", 18, 120),
                new ColumnSettings("Магазин", 19, 100),
                new ColumnSettings("Продавец", 20, 150),
                new ColumnSettings("Теги", 21, 150),
                new ColumnSettings("Примечание", 22, 300)
        )));
        settings.setDefaultPresetSettings(defaultPresetSettings);

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        UserSettingsService service = context.getBean(UserSettingsService.class);
        try {
            service.saveSettings(settings);
//            UsersSettings usersSettings = service.getSettings();
//            System.out.println(usersSettings);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
