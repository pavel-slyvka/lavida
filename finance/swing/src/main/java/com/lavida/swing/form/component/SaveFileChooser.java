package com.lavida.swing.form.component;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The SaveFileChooser is a custom JFileChooser with the internationalization ability.
 * Created: 21:08 29.08.13
 *
 * @author Ruslan
 */
public class SaveFileChooser extends JFileChooser {
    public SaveFileChooser() {
        super();
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить");
        UIManager.put("FileChooser.saveInLabelText", "Сохранить в");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filenameTextField", "postponed" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Все");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить выбраный файл");
        UIManager.put("FileChooser.cancelButtonToolTipText","Отмена");
        UIManager.put("FileChooser.fileNameHeaderText","Имя файла");
        UIManager.put("FileChooser.upFolderToolTipText", "Выше на один уровень");
        UIManager.put("FileChooser.homeFolderToolTipText","Рабочий стол");
        UIManager.put("FileChooser.newFolderToolTipText","Создать новую папку");
        UIManager.put("FileChooser.listViewButtonToolTipText","Список");
        UIManager.put("FileChooser.newFolderButtonText","Создать новую папку");
        UIManager.put("FileChooser.renameFileButtonText", "Переименовать файл");
        UIManager.put("FileChooser.deleteFileButtonText", "Удалить файл");
        UIManager.put("FileChooser.filterLabelText", "Типы файлов");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Детали");
        UIManager.put("FileChooser.fileSizeHeaderText","Размер");
        UIManager.put("FileChooser.fileDateHeaderText", "Дата изменения");
        SwingUtilities.updateComponentTreeUI(this);

    }
}
