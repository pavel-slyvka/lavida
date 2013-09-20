package com.lavida.swing.service;

import com.lavida.service.ViewColumn;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.preferences.user.ColumnSettings;
import com.lavida.swing.preferences.user.PresetSettings;
import com.lavida.swing.preferences.user.TableSettings;
import com.lavida.swing.preferences.user.UsersSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The service for the {@link com.lavida.swing.preferences.user.UsersSettings }.
 * Created: 16:38 18.09.13
 *
 * @author Ruslan
 */
@Service
public class UserSettingsService {

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;


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

    public UsersSettings createDefaultUsersSettings() throws IOException, JAXBException {
        UsersSettings usersSettings = new UsersSettings();
        PresetSettings defaultPresetSettings = new PresetSettings();
        defaultPresetSettings.setName("defaultSettings");

        TableSettings articlesTableSettings = new TableSettings();
        List<ColumnSettings> articleColumns = articlesTableSettings.getColumns();
        int index = 1;
        for (Field field : ArticleJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                field.setAccessible(true);
                ColumnSettings articleColumnSettings = new ColumnSettings();
                if (viewColumn.titleKey().isEmpty()) {
                    articleColumnSettings.setHeader(field.getName());
                } else {
                    articleColumnSettings.setHeader(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                }
                articleColumnSettings.setIndex(index);
                ++ index;
                articleColumnSettings.setWidth(viewColumn.columnWidth());
                articleColumns.add(articleColumnSettings);
            }
        }
        defaultPresetSettings.setArticlesTableSettings(articlesTableSettings);

        TableSettings discountCardsTableSettings = new TableSettings();
        List<ColumnSettings> discountCardsColumns = discountCardsTableSettings.getColumns();
        index = 1;
        for (Field field : DiscountCardJdo.class.getDeclaredFields()) {
            ViewColumn viewColumn = field.getAnnotation(ViewColumn.class);
            if (viewColumn != null && viewColumn.show()) {
                field.setAccessible(true);
                ColumnSettings discountCardsColumnSettings = new ColumnSettings();
                if (viewColumn.titleKey().isEmpty()) {
                    discountCardsColumnSettings.setHeader(field.getName());
                } else {
                    discountCardsColumnSettings.setHeader(messageSource.getMessage(viewColumn.titleKey(), null, localeHolder.getLocale()));
                }
                discountCardsColumnSettings.setIndex(index);
                ++index;
                discountCardsColumnSettings.setWidth(viewColumn.columnWidth());
                discountCardsColumns.add(discountCardsColumnSettings);

            }
        }
        defaultPresetSettings.setDiscountCardsTableSettings(discountCardsTableSettings);

        usersSettings.setDefaultPresetSettings(defaultPresetSettings);
        saveSettings(usersSettings);
        return usersSettings;
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
