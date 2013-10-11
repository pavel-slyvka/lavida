package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.regex.Pattern;

/**
 * The FileChooserComponent is a custom JFileChooser with the internationalization ability.
 * Created: 21:08 29.08.13
 *
 * @author Ruslan
 */
@Component
public class FileChooserComponent extends JFileChooser {
    @Resource
    private MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    public FileChooserComponent() {
        super();
    }

    @PostConstruct
    public void init() {
        setLocale(localeHolder.getLocale());
        UIManager.put("FileChooser.saveDialogTitleText", messageSource.
                getMessage("component.FileChooser.saveDialogTitleText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.saveInLabelText", messageSource.
                getMessage("component.FileChooser.saveInLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.saveButtonText", messageSource.
                getMessage("component.FileChooser.saveButtonText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.lookInLabelText", messageSource.
                getMessage("component.FileChooser.lookInLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.openButtonText", messageSource.
                getMessage("component.FileChooser.openButtonText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.cancelButtonText", messageSource.
                getMessage("component.FileChooser.cancelButtonText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.helpButtonText", messageSource.
                getMessage("component.FileChooser.helpButtonText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.fileNameLabelText", messageSource.
                getMessage("component.FileChooser.fileNameLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.filesOfTypeLabelText", messageSource.
                getMessage("component.FileChooser.filesOfTypeLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.acceptAllFileFilterText", messageSource.
                getMessage("component.FileChooser.acceptAllFileFilterText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.saveButtonToolTipText", messageSource.
                getMessage("component.FileChooser.saveButtonToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.openButtonToolTipText", messageSource.
                getMessage("component.FileChooser.openButtonToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.cancelButtonToolTipText", messageSource.
                getMessage("component.FileChooser.cancelButtonToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.helpButtonToolTipText", messageSource.
                getMessage("component.FileChooser.helpButtonToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.fileNameHeaderText", messageSource.
                getMessage("component.FileChooser.fileNameHeaderText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.upFolderToolTipText", messageSource.
                getMessage("component.FileChooser.upFolderToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.homeFolderToolTipText", messageSource.
                getMessage("component.FileChooser.homeFolderToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.newFolderToolTipText", messageSource.
                getMessage("component.FileChooser.newFolderToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.listViewButtonToolTipText", messageSource.
                getMessage("component.FileChooser.listViewButtonToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.newFolderButtonText", messageSource.
                getMessage("component.FileChooser.newFolderButtonText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.renameFileButtonText", messageSource.
                getMessage("component.FileChooser.renameFileButtonText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.deleteFileButtonText", messageSource.
                getMessage("component.FileChooser.deleteFileButtonText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.filterLabelText", messageSource.
                getMessage("component.FileChooser.filterLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", messageSource.
                getMessage("component.FileChooser.detailsViewButtonToolTipText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.fileSizeHeaderText", messageSource.
                getMessage("component.FileChooser.fileSizeHeaderText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.fileDateHeaderText", messageSource.
                getMessage("component.FileChooser.fileDateHeaderText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.viewMenuLabelText", messageSource.
                getMessage("component.FileChooser.viewMenuLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.refreshActionLabelText", messageSource.
                getMessage("component.FileChooser.refreshActionLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.newFolderActionLabelText", messageSource.
                getMessage("component.FileChooser.newFolderActionLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.listViewActionLabelText", messageSource.
                getMessage("component.FileChooser.listViewActionLabelText", null, localeHolder.getLocale()));
        UIManager.put("FileChooser.detailsViewActionLabelText", messageSource.
                getMessage("component.FileChooser.detailsViewActionLabelText", null, localeHolder.getLocale()));

        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Shows warning messageDialog.
     *
     * @param titleKey   the title of the messageDialog.
     * @param messageKey the contents of the messageDialog.
     */
    public void showWarningMessage(String titleKey, String messageKey) {
        JOptionPane.showMessageDialog(this,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows confirm dialog.
     *
     * @param titleKey   the title of the messageDialog.
     * @param messageKey the contents of the messageDialog.
     */
    public int showConfirmDialog(String titleKey, String messageKey) {
        return JOptionPane.showConfirmDialog(this,
                messageSource.getMessage(messageKey, null, localeHolder.getLocale()),
                messageSource.getMessage(titleKey, null, localeHolder.getLocale()),
                JOptionPane.YES_NO_CANCEL_OPTION);
    }

    /**
     * Checks if {@link FileNameExtensionFilter} is selected.
     *
     * @return true if {@link FileNameExtensionFilter} is selected.
     */
    public boolean isFileFilterSelected() {
        return getFileFilter() instanceof FileNameExtensionFilter;
    }

    /**
     * Improves file's extension if it is empty.
     *
     * @param file the file to be improved.
     * @return the improved file.
     */
    public File improveFileExtension(File file) {
        String enteredFileName = file.getName();
        String fileExtension = null;
        int index = enteredFileName.lastIndexOf('.');
        if (index > 0) {
            fileExtension = enteredFileName.substring(index + 1);
        }
        FileNameExtensionFilter extensionFilter = (FileNameExtensionFilter) getFileFilter();
        String[] extensions = extensionFilter.getExtensions();
        if (!isRightFileExtension(fileExtension)) {
            enteredFileName = enteredFileName.substring(0, index);
            fileExtension = null;
        }
        if (fileExtension == null) {
            String filterExtension = extensions[0]; // xml
            String filePath = getCurrentDirectory().toString()+ "\\" + enteredFileName + "." + filterExtension;
            file = new File(filePath);
        }
        return file;
    }

    /**
     * Checks if the {@link FileNameExtensionFilter} contains the current file extension.
     * @param fileExtension the current file extension to be checked.
     * @return true if the {@link FileNameExtensionFilter} contains the current file extension.
     */
    private boolean isRightFileExtension(String fileExtension) {
        FileNameExtensionFilter extensionFilter = (FileNameExtensionFilter) getFileFilter();
        String[] extensions = extensionFilter.getExtensions();
        for (String extension : extensions) {
            if (extension.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates the entered file name for file separators and dots.
     *
     * @param file the file to be validated.
     * @return true if the file is valid.
     */

    public boolean isValidFile(File file) {
        String currentDirectory = getCurrentDirectory().toString();
        String parentDirectory = file.getParent();
        if (!currentDirectory.equalsIgnoreCase(parentDirectory)) {
            return false;
        }

        String enteredFileName = file.getName();
        String[] nameParts = enteredFileName.split(Pattern.quote("."));
        return !(nameParts.length > 2);
    }

}
