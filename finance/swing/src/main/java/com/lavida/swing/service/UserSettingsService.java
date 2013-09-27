package com.lavida.swing.service;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.preferences.*;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * The service for the {@link com.lavida.swing.preferences.UsersSettings }.
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

    @Resource
    private UsersSettingsHolder usersSettingsHolder;

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

    public UsersSettings updatePresetSettings() {
        JTable notSoldArticlesTable = usersSettingsHolder.getNotSoldArticlesTable();
        JTable soldArticlesTable = usersSettingsHolder.getSoldArticlesTable();
        JTable addNewArticlesTable = usersSettingsHolder.getAddNewArticlesTable();
        JTable allDiscountCardsTable = usersSettingsHolder.getAllDiscountCardsTable();
        JTable addNewDiscountCardsTable = usersSettingsHolder.getAddNewDiscountCardsTable();
        String login = usersSettingsHolder.getLogin();
        String presetName = usersSettingsHolder.getPresetName();

        UsersSettings usersSettings = usersSettingsHolder.getUsersSettings();
        List<UserSettings> userSettingsList = usersSettings.getUserSettingsList();
        UserSettings userSettings = null;
        if (userSettingsList.size() > 0) {
            for (UserSettings settings : userSettingsList) {
                if (login.equals(settings.getLogin())) {
                    userSettings = settings;
                }
            }
        }
        if (userSettings == null) {
            userSettings = new UserSettings(login);
            userSettingsList.add(userSettings);
        }

        PresetSettings presetSettings = null;
        if (userSettings.getPresetSettingsList().size() > 0) {
            for (PresetSettings settings : userSettings.getPresetSettingsList()) {
                if (presetName.equals(settings.getPresetName())) {
                    presetSettings = settings;
                }
            }
        }
        if (presetSettings == null) {
            presetSettings = new PresetSettings();
            presetSettings.setPresetName(presetName);
            userSettings.getPresetSettingsList().add(presetSettings);
        }
        userSettings.setLastPresetName(presetName);
        List<TableSettings> tableSettingsList = new ArrayList<>();
        presetSettings.setTableSettings(tableSettingsList);

        TableSettings notSoldArticlesTableSettings = new TableSettings();
        notSoldArticlesTableSettings.setTableSettingsName(PresetSettings.NOT_SOLD_ARTICLES_TABLE);
        List<ColumnSettings> notSoldArticlesColumnSettingsList = getColumnsSettingsList(notSoldArticlesTable);
        notSoldArticlesTableSettings.setColumns(notSoldArticlesColumnSettingsList);
        tableSettingsList.add(notSoldArticlesTableSettings);

        TableSettings soldArticlesTableSettings = new TableSettings();
        soldArticlesTableSettings.setTableSettingsName(PresetSettings.SOLD_ARTICLES_TABLE);
        List<ColumnSettings> soldArticlesColumnSettingsList = getColumnsSettingsList(soldArticlesTable);
        soldArticlesTableSettings.setColumns(soldArticlesColumnSettingsList);
        tableSettingsList.add(soldArticlesTableSettings);

        TableSettings addNewArticlesTableSettings = new TableSettings();
        addNewArticlesTableSettings.setTableSettingsName(PresetSettings.ADD_NEW_ARTICLES_TABLE);
        List<ColumnSettings> addNewArticlesColumnSettingsList = getColumnsSettingsList(addNewArticlesTable);
        addNewArticlesTableSettings.setColumns(addNewArticlesColumnSettingsList);
        tableSettingsList.add(addNewArticlesTableSettings);

        TableSettings allDiscountCardsTableSettings = new TableSettings();
        allDiscountCardsTableSettings.setTableSettingsName(PresetSettings.ALL_DISCOUNT_CARDS_TABLE);
        List<ColumnSettings> allDiscountCardsTableSettingsList = getColumnsSettingsList(allDiscountCardsTable);
        allDiscountCardsTableSettings.setColumns(allDiscountCardsTableSettingsList);
        tableSettingsList.add(allDiscountCardsTableSettings);

        TableSettings addNewDiscountCardsTableSettings = new TableSettings();
        addNewDiscountCardsTableSettings.setTableSettingsName(PresetSettings.ADD_NEW_DISCOUNT_CARDS_TABLE);
        List<ColumnSettings> addNewDiscountCardsTableSettingsList = getColumnsSettingsList(addNewDiscountCardsTable);
        addNewDiscountCardsTableSettings.setColumns(addNewDiscountCardsTableSettingsList);
        tableSettingsList.add(addNewDiscountCardsTableSettings);

        return usersSettings;
    }

    /**
     * Converts the Enumeration of TableColumn  to the List of TableColumn.
     *
     * @param tableColumnEnumeration the Enumeration to be converted.
     * @return the List of TableColumn.
     */
    private List<TableColumn> tableColumnEnumerationToList(Enumeration<TableColumn> tableColumnEnumeration) {
        List<TableColumn> tableColumnList = new ArrayList<>();
        while (tableColumnEnumeration.hasMoreElements()) {
            tableColumnList.add(tableColumnEnumeration.nextElement());
        }
        return tableColumnList;
    }

    /**
     * Constructs a List of {@link com.lavida.swing.preferences.ColumnSettings} for the {@link javax.swing.JTable}.
     *
     * @param table the JTable from which to construct a List of ColumnSettings.
     * @return a List of ColumnSettings.
     */
    private List<ColumnSettings> getColumnsSettingsList(JTable table) {
        List<ColumnSettings> columnSettingsList = new ArrayList<>();
        Enumeration<TableColumn> tableColumnEnumeration = table.getColumnModel().getColumns();
        List<TableColumn> tableColumnList = tableColumnEnumerationToList(tableColumnEnumeration);

        int backgroundColor = table.getBackground().getRGB();
        int foregroundColor = table.getForeground().getRGB();
        for (TableColumn tableColumn : tableColumnList) {
            String header = (String) tableColumn.getHeaderValue();
            int index = tableColumn.getModelIndex();
            int width = tableColumn.getPreferredWidth();
            ColumnSettings columnSettings = new ColumnSettings(header, index, width, backgroundColor, foregroundColor);
            columnSettingsList.add(columnSettings);
        }
        return columnSettingsList;
    }

    /**
     * Checks if the current user has the default presetSettings.
     * @return true if the current user has the default presetSettings.
     */
    public boolean userDefaultPresetExists() {
        String login = usersSettingsHolder.getLogin();
        String defaultPresetName = messageSource.getMessage("settings.user.preset.default.name", null, localeHolder.getLocale());

        UsersSettings usersSettings = usersSettingsHolder.getUsersSettings();
        List<UserSettings> userSettingsList = usersSettings.getUserSettingsList();
        UserSettings userSettings = null;
        if (userSettingsList.size() > 0) {
            for (UserSettings settings : userSettingsList) {
                if (login.equals(settings.getLogin())) {
                    userSettings = settings;
                }
            }
        } else {
            return false;
        }
        if (userSettings == null) return false;

        if (userSettings.getPresetSettingsList().size() > 0) {
            for (PresetSettings settings : userSettings.getPresetSettingsList()) {
                if (defaultPresetName.equals(settings.getPresetName())) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Finds userSettings for the current user.
     * @return userSettings for the current user.
     */
    public UserSettings getUserSettings() {
        String login = usersSettingsHolder.getLogin();

        UsersSettings usersSettings = usersSettingsHolder.getUsersSettings();
        List<UserSettings> userSettingsList = usersSettings.getUserSettingsList();
        UserSettings userSettings = null;
        if (userSettingsList.size() > 0) {
            for (UserSettings settings : userSettingsList) {
                if (login.equals(settings.getLogin())) {
                    userSettings = settings;
                }
            }
        }
        return userSettings;
    }

    /**
     * Creates the {@link com.lavida.swing.preferences.EditorsSettings} and sets it to the UsersSettings.
     * @return the updated UsersSettings.
     */
    public UsersSettings createEditorsSettings() {
        UsersSettings usersSettings = usersSettingsHolder.getUsersSettings();
        EditorsSettings editorsSettings = new EditorsSettings();
        usersSettings.setEditorsSettings(editorsSettings);
        List<TableEditorSettings> tableEditorSettingsList = new ArrayList<>();
        editorsSettings.setTableEditor(tableEditorSettingsList);

        TableEditorSettings articlesTableEditor = new TableEditorSettings();
        articlesTableEditor.setTableEditorSettingsName(EditorsSettings.ARTICLES_TABLE);
        List<ColumnEditorSettings> articleColumnEditors = getColumnEditorSettingsList(usersSettingsHolder.getNotSoldArticlesTable());
        articlesTableEditor.setColumnEditors(articleColumnEditors);
        tableEditorSettingsList.add(articlesTableEditor);

        TableEditorSettings discountCardsTableEditor = new TableEditorSettings();
        discountCardsTableEditor.setTableEditorSettingsName(EditorsSettings.DISCOUNT_CARDS_TABLE);
        List<ColumnEditorSettings> discountCardsColumnEditors = getColumnEditorSettingsList(usersSettingsHolder.getAllDiscountCardsTable());
        discountCardsTableEditor.setColumnEditors(discountCardsColumnEditors);
        tableEditorSettingsList.add(discountCardsTableEditor);

        return usersSettings;
    }

    /**
     * Creates the List of ColumnEditorSettings from the  JTable.
     * @param table the JTable  from which to create the List of ColumnEditorSettings.
     * @return the List of ColumnEditorSettings.
     */
    private List<ColumnEditorSettings> getColumnEditorSettingsList(JTable table) {
        List<ColumnEditorSettings> columnEditorSettingsList = new ArrayList<>();
        Enumeration<TableColumn> tableColumnEnumeration = table.getColumnModel().getColumns();
        List<TableColumn> tableColumnList = tableColumnEnumerationToList(tableColumnEnumeration);
        for (TableColumn tableColumn : tableColumnList) {
            String header = (String) tableColumn.getHeaderValue();
            int index = tableColumn.getModelIndex();

            JComboBox comboBox;
            List<String> comboBoxItems = new ArrayList<>();
            if (tableColumn.getCellEditor() != null) {
                Component component = tableColumn.getCellEditor().getTableCellEditorComponent(
                        table, null, true, 0, index);
                if (component instanceof JComboBox) {
                    comboBox = (JComboBox) component;
                    ComboBoxModel comboBoxModel = comboBox.getModel();
                    int size = comboBoxModel.getSize();
                    for (int i = 0; i < size; i++) {
                        String item = (String) comboBoxModel.getElementAt(i);
                        comboBoxItems.add(item);
                    }
                }
            }
            ColumnEditorSettings columnEditorSettings = new ColumnEditorSettings();
            if (comboBoxItems.size() > 0) {
                columnEditorSettings.setComboBoxItem(comboBoxItems);
                columnEditorSettings.setHeader(header);
                columnEditorSettingsList.add(columnEditorSettings);
            }
        }
        return columnEditorSettingsList;
    }

    /**
     * Gives the List of all user's presetSettings names.
     * @return the List of all user's presetSettings names.
     */
    public List<String> getUserPresetNames() {
        List<String> presetNames = new ArrayList<>();
        UserSettings userSettings = getUserSettings();
        for (PresetSettings presetSettings : userSettings.getPresetSettingsList()) {
            presetNames.add(presetSettings.getPresetName());
        }
        return presetNames;
    }

    /**
     * Gets the current presetSettings for the user.
     * @return the current presetSettings for the user.
     */
    public PresetSettings getPresetSettings() {
        String presetName = usersSettingsHolder.getPresetName();
        PresetSettings presetSettings = null;
        for (PresetSettings settings : getUserSettings().getPresetSettingsList()) {
            if (presetName.equals(settings.getPresetName())) {
                presetSettings = settings;
            }
        }
        return presetSettings;
    }

    /**
     * Deletes the presetSettings from userSettings by the presetName,
     * but not saves this operation to the file.
     * @param presetName the name of the presetSettings to be deleted.
     */
    public void deletePresetSettings(String presetName) {
        UserSettings currentUserSettings = getUserSettings();
        List<PresetSettings> presetSettingsList = currentUserSettings.getPresetSettingsList();
        int size = presetSettingsList.size();
        for (int i = 0; i < size; ++i) {
            if (presetName.equals(presetSettingsList.get(i).getPresetName())) {
                presetSettingsList.remove(i);
                break;
            }
        }
    }
}
