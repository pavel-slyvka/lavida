package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.TaskProgressEvent;
import com.lavida.service.ArticleUpdateInfo;
import com.lavida.service.DiscountCardsUpdateInfo;
import com.lavida.swing.form.component.TablePrintPreviewComponent;
import com.lavida.swing.preferences.UsersSettings;
import com.lavida.swing.service.*;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.settings.Settings;
import com.lavida.service.settings.SettingsService;
import com.lavida.swing.preferences.UsersSettingsHolder;
import com.lavida.service.xml.PostponedType;
import com.lavida.service.xml.PostponedXmlService;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.*;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.form.MainForm;
import com.lavida.swing.form.component.FileChooserComponent;
import com.lavida.swing.form.component.ProgressComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * MainFormHandler
 * <p/>
 * Created: 21:14 09.08.13
 *
 * @author Pavel
 */
@Component
public class MainFormHandler implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(MainFormHandler.class);
    private static final String DATE_FORMAT = "dd.MM.yyyy";

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource
    private ArticleChangedFieldServiceSwingWrapper articleChangedFieldServiceSwingWrapper;

    @Resource
    private PostponedXmlService postponedXmlService;

    @Resource
    private MainForm form;

    @Resource
    private SellDialog sellDialog;

//    @Resource
//    private SoldProductsDialog soldProductsDialog;
//
//    @Resource
//    private AddNewProductsDialog addNewProductsDialog;
//
//    @Resource
//    private NotSoldArticlesTableViewSettingsDialog notSoldArticlesTableViewSettingsDialog;
//
//    @Resource
//    private SoldArticlesTableViewSettingsDialog soldArticlesTableViewSettingsDialog;
//
//    @Resource
//    private AllDiscountCardsDialog allDiscountCardsDialog;


    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private FileChooserComponent fileChooser;

    @Resource
    private ProgressComponent progressComponent;

    @Resource
    private SettingsService settingsService;

    @Resource
    private UsersSettingsHolder usersSettingsHolder;

    @Resource
    private UserSettingsService userSettingsService;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

//    @Resource
//    private UpdateInfoMessageDialog updateInfoMessageDialog;

    private ApplicationContext applicationContext;
    private String[] shopArray = {"", "LA VIDA", "СЛАВЯНСКИЙ", "НОВОМОСКОВСК"};

    /**
     * The ActionListener for refreshButton component.
     * When button is clicked data for ArticleJdo List loads from Google spreadsheet
     * and saves to database, then it goes through filterSold() for deleting all sold
     * goods and renders to the articleTable.
     */
    public void refreshTableItemClicked() {
        final boolean needToLoadPostponed = hasPostponed();
        String postponedOperations = messageSource.getMessage("mainForm.handler.string.postponed.operations", null, localeHolder.getLocale());
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                postponedOperations  + ".xml";
        final File fileToLoad = new File(filePath);
        if (hasPostponed()) {
            savePostponed(fileToLoad);
            deletePostponedOperations();
        }

        concurrentOperationsService.startOperation("Synchronization.", new Runnable() {
            @Override
            public void run() {
                form.setRefreshTableItemEnable(false);
                ArticleUpdateInfo articleUpdateInfo;
                DiscountCardsUpdateInfo discountCardsUpdateInfo;
                try {
                    List<Long> refreshTaskTimes = Arrays.asList(6000L, 14000L, 8000L, 300L, 3000L, 100L);
                    String refreshTaskTimesString = settingsService.getSettings().getSheetRefreshTasksTimes();
                    if (refreshTaskTimesString != null && !refreshTaskTimesString.trim().isEmpty()) {
                        String[] refreshTaskTimesArray = refreshTaskTimesString.split(", ");
                        if (refreshTaskTimesArray.length == refreshTaskTimes.size()) {
                            try {
                                List<Long> newRefreshTaskTimes = new ArrayList<>(refreshTaskTimes.size());
                                for (String time : refreshTaskTimesArray) {
                                    newRefreshTaskTimes.add(Long.valueOf(time));
                                }
                                refreshTaskTimes = newRefreshTaskTimes;
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }

                    progressComponent.reinitialize(messageSource.getMessage("mainForm.progress.label.refresh", null, localeHolder.getLocale()))
                            .addWork(refreshTaskTimes.get(0), true).addWork(refreshTaskTimes.get(1), true).addWork(refreshTaskTimes.get(2), true)
                            .addWork(refreshTaskTimes.get(3), true).addWork(refreshTaskTimes.get(4), true).addWork(refreshTaskTimes.get(5), true)
                            .start();
                    List<ArticleJdo> articles = articleServiceSwingWrapper.loadArticlesFromRemoteServer();
                    articleUpdateInfo = articleServiceSwingWrapper.updateDatabaseFromRemote(articles);
                    applicationContext.publishEvent(new TaskProgressEvent(this, TaskProgressEvent.TaskProgressType.COMPLETE));

                    List<DiscountCardJdo> discountCardJdoList = discountCardServiceSwingWrapper.loadDiscountCardsFromRemoteServer();
                    applicationContext.publishEvent(new TaskProgressEvent(this, TaskProgressEvent.TaskProgressType.COMPLETE));
                    discountCardsUpdateInfo = discountCardServiceSwingWrapper.updateDatabaseFromRemote(discountCardJdoList);
                    applicationContext.publishEvent(new TaskProgressEvent(this, TaskProgressEvent.TaskProgressType.COMPLETE));
                } catch (IOException | ServiceException  e) {
                    Thread.currentThread().interrupt();
                    progressComponent.getProgressBar().setVisible(false);
                    progressComponent.getLabel().setVisible(false);
                    if (needToLoadPostponed) {
                        loadPostponed(fileToLoad);
                    }
                    form.setRefreshTableItemEnable(true);
                    form.update();    // repaint MainForm in some time
                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e);
                }
                showUpdateInfoMessage(articleUpdateInfo, discountCardsUpdateInfo);
                if (articleUpdateInfo.getChangedFieldJdoList() != null) {
                    articleChangedFieldServiceSwingWrapper.update(articleUpdateInfo.getChangedFieldJdoList());
                }
                form.update();    // repaint MainForm in some time
                form.setRefreshTableItemEnable(true);

                Long[] correctedTaskTimes = progressComponent.getCorrectedTaskTimes();
                StringBuilder correctedTimesBuilder = new StringBuilder();
                for (long correctedTaskTime : correctedTaskTimes) {
                    correctedTimesBuilder.append(", ").append(correctedTaskTime);
                }
                Settings settings = settingsService.getSettings();
                settings.setSheetRefreshTasksTimes(correctedTimesBuilder.toString().substring(2));
                settingsService.saveSettings(settings);

                if (needToLoadPostponed) {
                    loadPostponed(fileToLoad);
                    showPostponedOperationsMessage();
                }
            }
        });
    }

    private boolean hasPostponed() {
        for (ArticleJdo articleJdo : articleServiceSwingWrapper.getAll()) {
            if (articleJdo.getPostponedOperationDate() != null) return true;
        }
        for (DiscountCardJdo discountCardJdo : discountCardServiceSwingWrapper.getAll()) {
            if (discountCardJdo.getPostponedDate() != null) return true;
        }
        return false;
    }

    /**
     * Shows a JOptionPane message with the information about the articles updating  process.
     *
     * @param articleUpdateInfo       the ArticleUpdateInfo to be shown.
     * @param discountCardsUpdateInfo the DiscountCardsUpdateInfo to be shown.
     */
    private void showUpdateInfoMessage(ArticleUpdateInfo articleUpdateInfo, DiscountCardsUpdateInfo discountCardsUpdateInfo) {
        StringBuilder messageBuilder = new StringBuilder();
        if (articleUpdateInfo.getAddedCount() > 0 || discountCardsUpdateInfo.getAddedCount() > 0) {
            messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.added",
                    null, localeHolder.getLocale()));
            if (articleUpdateInfo.getAddedCount() > 0) {
                messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.articles.added",
                        null, localeHolder.getLocale()));
                messageBuilder.append(articleUpdateInfo.getAddedCount());
                messageBuilder.append(", ");
            }
            if (discountCardsUpdateInfo.getAddedCount() > 0) {
                messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.discountCards.added",
                        null, localeHolder.getLocale()));
                messageBuilder.append(discountCardsUpdateInfo.getAddedCount());
            }
            messageBuilder.append(". \n");
        }
        if (articleUpdateInfo.getUpdatedCount() > 0 || discountCardsUpdateInfo.getUpdatedCount() > 0) {
            messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.updated",
                    null, localeHolder.getLocale()));
            if (articleUpdateInfo.getUpdatedCount() > 0) {
                messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.articles.added",
                        null, localeHolder.getLocale()));
                messageBuilder.append(articleUpdateInfo.getUpdatedCount());
                messageBuilder.append(", ");
            }
            if (discountCardsUpdateInfo.getUpdatedCount() > 0) {
                messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.discountCards.added",
                        null, localeHolder.getLocale()));
                messageBuilder.append(discountCardsUpdateInfo.getUpdatedCount());
            }
            messageBuilder.append(". \n");
        }

        if (articleUpdateInfo.getDeletedCount() > 0 || discountCardsUpdateInfo.getDeletedCount() > 0) {
            messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.deleted",
                    null, localeHolder.getLocale()));
            if (articleUpdateInfo.getDeletedCount() > 0) {
                messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.articles.added",
                        null, localeHolder.getLocale()));
                messageBuilder.append(articleUpdateInfo.getDeletedCount());
                messageBuilder.append(", ");
            }
            if (discountCardsUpdateInfo.getDeletedCount() > 0) {
                messageBuilder.append(messageSource.getMessage("mainForm.panel.refresh.message.discountCards.added",
                        null, localeHolder.getLocale()));
                messageBuilder.append(discountCardsUpdateInfo.getDeletedCount());
            }
            messageBuilder.append(". \n");
        }
        String message = convertToMultiline(new String(messageBuilder));
//        updateInfoMessageDialog.showUpdateInfoMessage(message);
        form.showInfoToolTip(message);
//        form.showInformationMessage("mainForm.panel.refresh.message.title", message);
    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>"); //+ "</html>"
    }

    public void sellButtonClicked() {
        if (tableModel.getSelectedArticle() != null) {
            sellDialog.initWithArticleJdo(tableModel.getSelectedArticle());
            sellDialog.show();

        } else {
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.sold.article.not.chosen");
        }
    }

    private String formatCalendar(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (calendar != null) {
            return  dateFormat.format(calendar.getTime());
        }else return null;
    }

    public void recommitPostponedItemClicked() {
        concurrentOperationsService.startOperation("Recommit.", new Runnable() {
            @Override
            public void run() {
                form.getRecommitPostponedItem().setEnabled(false);
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                List<ArticleJdo> articles = articleServiceSwingWrapper.getAll();
                for (ArticleJdo articleJdo : articles) {
                    if (articleJdo.getPostponedOperationDate() != null) {
                        try {
                            if (articleJdo.getSold() != null && dateFormat.format(articleJdo.getPostponedOperationDate()).
                                    equals(formatCalendar(articleJdo.getSaleDate()))) {   //recommit selling
                                articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, true);
                            } else if (articleJdo.getSold() == null && dateFormat.format(articleJdo.getRefundDate()).
                                    equals(dateFormat.format(articleJdo.getRefundDate()))) {  // recommit refunding
                                articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, false);
                                articleJdo.setSaleDate(null);
                            } else {
                                articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, null); // recommit other changes
                            }
                            articleJdo.setPostponedOperationDate(null);
                            articleServiceSwingWrapper.update(articleJdo);
                        } catch (IOException | ServiceException e) {
                            showPostponedOperationsMessage();
                            form.getRecommitPostponedItem().setEnabled(true);
                            form.update();
                            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e);
                        }
                    }
                }
                List<DiscountCardJdo> discountCardJdoList = discountCardServiceSwingWrapper.getAll();
                for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
                    if (discountCardJdo.getPostponedDate() != null) {
                        try {
                            discountCardServiceSwingWrapper.updateToSpreadsheet(discountCardJdo);
                            discountCardJdo.setPostponedDate(null);
                            discountCardServiceSwingWrapper.update(discountCardJdo);
                        } catch (IOException | ServiceException e) {
                            showPostponedOperationsMessage();
                            form.getRecommitPostponedItem().setEnabled(true);
                            form.update();
                            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, e);
                        }
                    }
                }
                showPostponedOperationsMessage();
                form.getRecommitPostponedItem().setEnabled(true);
                form.update();

            }
        });
    }

    /**
     * Shows postponedOperationMessageLabel in the statusBarPanel of the mainForm.
     */
    public void showPostponedOperationsMessage() {
        int count = 0;
        List<ArticleJdo> articles = articleServiceSwingWrapper.getAll();
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getPostponedOperationDate() != null) {
                ++count;
            }
        }
        List<DiscountCardJdo> discountCardJdoList = discountCardServiceSwingWrapper.getAll();
        for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
            if (discountCardJdo.getPostponedDate() != null) {
                ++count;
            }
        }
        if (count > 0) {
            form.getPostponedMessage().setText(String.valueOf(count));
            form.getPostponedMessage().setVisible(true);
            form.getPostponedOperations().setVisible(true);
            form.getSavePostponedItem().setEnabled(true);
            form.getRecommitPostponedItem().setEnabled(true);
            form.getDeletePostponedItem().setEnabled(true);
        } else {
            form.getPostponedMessage().setText(String.valueOf(count));
            form.getPostponedMessage().setVisible(false);
            form.getPostponedOperations().setVisible(false);
            form.getSavePostponedItem().setEnabled(false);
            form.getRecommitPostponedItem().setEnabled(false);
            form.getDeletePostponedItem().setEnabled(false);
        }
    }

    /**
     * Saves the List{@code <}{@link ArticleJdo}{@code >} with postponed operation to chosen xml file.
     *
     * @param file the chosen xml file.
     */
    public void savePostponed(File file) {
        List<ArticleJdo> articlesPostponed = new ArrayList<>();
        List<ArticleJdo> articlesAll = articleServiceSwingWrapper.getAll();
        for (ArticleJdo articleJdo : articlesAll) {
            if (articleJdo.getPostponedOperationDate() != null) {
                articlesPostponed.add(articleJdo);
            }
        }

        List<DiscountCardJdo> discountCardsPostponed = new ArrayList<>();
        List<DiscountCardJdo> discountCardJdoList = discountCardServiceSwingWrapper.getAll();
        for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
            if (discountCardJdo.getPostponedDate() != null) {
                discountCardsPostponed.add(discountCardJdo);
            }
        }

        PostponedType postponedType = new PostponedType();
        postponedType.setArticles(articlesPostponed);
        postponedType.setDiscountCards(discountCardsPostponed);
        try {
//            articleServiceSwingWrapper.saveToXml(articlesPostponed, file);
            postponedXmlService.marshal(postponedType, file);
        } catch (JAXBException e) {
            logger.warn(e.getMessage(), e);
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.exception.xml.JAXB.message");
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.exception.io.xml.file");
        }
    }

    /**
     * Shows a dialog for choosing the file for saving postponed operations.
     */
    public void savePostponedItemClicked() {
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        fileChooser.setSelectedFile(new File("postponed_" +
                new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".xml"));
        File file;
        while (true) {
            int choice = fileChooser.showSaveDialog(form.getForm());
            if (choice == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                if (!fileChooser.isValidFile(file)) {
                    fileChooser.showWarningMessage("mainForm.exception.message.dialog.title",
                            "mainForm.handler.fileChooser.fileName.format.message");
                    fileChooser.setSelectedFile(new File("postponed_" +
                            new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".xml"));
                    continue;
                }

                if (!fileChooser.isFileFilterSelected()) {
                    fileChooser.showWarningMessage("mainForm.exception.message.dialog.title",
                            "mainForm.handler.fileChooser.fileFilter.selection.message");
                    continue;
                }

                file = fileChooser.improveFileExtension(file);

                if (file.exists()) {
                    int result = fileChooser.showConfirmDialog("mainForm.handler.fileChooser.file.exists.dialog.title",
                            "mainForm.handler.fileChooser.file.exists.dialog.message");
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            break;
                        case JOptionPane.NO_OPTION:
                            continue;
                        case JOptionPane.CLOSED_OPTION:
                            continue;
                        case JOptionPane.CANCEL_OPTION:
                            fileChooser.setSelectedFile(new File("postponed_" +
                                    new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".xml"));
                            fileChooser.cancelSelection();
                            return;
                    }
                }
                break;
            } else {
                fileChooser.setSelectedFile(new File("postponed_" +
                        new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".xml"));
                fileChooser.cancelSelection();
                return;
            }
        }
        savePostponed(file);
        form.update();
        fileChooser.setSelectedFile(new File("postponed_" +
                new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".xml"));
    }

    /**
     * Deletes all postponed operations from the database.
     */
    public void deletePostponedItemClicked() {
        int result = form.showConfirmDialog("mainForm.menu.postponed.delete.message.title",
                "mainForm.menu.postponed.delete.message.body");
        switch (result) {
            case JOptionPane.YES_OPTION:
                deletePostponedOperations();
                form.update();
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CLOSED_OPTION:
        }
    }

    /**
     * Deletes all postponed operations from the database and updates the PostponedOperationsMessage label of the
     * mainForm.
     */
    private void deletePostponedOperations() {
        List<ArticleJdo> allArticles = articleServiceSwingWrapper.getAll();
        for (ArticleJdo articleJdo : allArticles) {
            if (articleJdo.getPostponedOperationDate() != null) {
                articleJdo.setPostponedOperationDate(null);
                articleServiceSwingWrapper.update(articleJdo);
            }
        }
        List<DiscountCardJdo> discountCardJdoList = discountCardServiceSwingWrapper.getAll();
        for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
            if (discountCardJdo.getPostponedDate() != null) {
                discountCardJdo.setPostponedDate(null);
                discountCardServiceSwingWrapper.update(discountCardJdo);
            }
        }

        showPostponedOperationsMessage();
    }

    /**
     * Shows a dialog for choosing the xml file with postponed operations and loads them to the database.
     */
    public void loadPostponedItemClicked() {
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        fileChooser.setSelectedFile(null);
        File file;
        while (true) {
            int choice = fileChooser.showOpenDialog(form.getForm());
            if (choice == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                if (!fileChooser.isValidFile(file)) {
                    fileChooser.showWarningMessage("mainForm.exception.message.dialog.title",
                            "mainForm.handler.fileChooser.fileName.format.message");
                    fileChooser.setSelectedFile(null);
                    continue;
                }

                if (!fileChooser.isFileFilterSelected()) {
                    fileChooser.showWarningMessage("mainForm.exception.message.dialog.title",
                            "mainForm.handler.fileChooser.fileFilter.selection.message");
                    continue;
                }
                break;
            } else {
                fileChooser.setSelectedFile(null);
                fileChooser.cancelSelection();
                return;
            }
        }
        loadPostponed(file);
        form.update();
        fileChooser.setSelectedFile(null);
    }

    /**
     * Loads postponed operations to the database.
     *
     * @param file the chosen xml file with postponed operations to be loaded.
     */
    private void loadPostponed(File file) {
        List<ArticleJdo> loadedArticles;
        List<DiscountCardJdo> loadedDiscountCards;
        PostponedType postponedType;
        try {
            postponedType = postponedXmlService.unmarshal(file);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.exception.xml.JAXB.message");
            return;
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.exception.io.xml.file");
            return;
        }
        loadedArticles = postponedType.getArticles();
        loadedDiscountCards = postponedType.getDiscountCards();
        if (loadedArticles != null) {
            List<ArticleJdo> forUpdateArticles = articleServiceSwingWrapper.mergePostponedWithDatabase(loadedArticles);
            for (ArticleJdo articleJdo : forUpdateArticles) {
                articleServiceSwingWrapper.update(articleJdo);
            }
            showPostponedOperationsMessage();
//        } else {
//            form.showWarningMessage("mainForm.attention.message.dialog.title", "mainForm.handler.postponed.articles.not.exist.message");
        }
        if (loadedDiscountCards != null) {
            List<DiscountCardJdo> forUpdateDiscountCards = discountCardServiceSwingWrapper.mergePostponedWithDatabase(loadedDiscountCards);
            for (DiscountCardJdo discountCardJdo : forUpdateDiscountCards) {
                discountCardServiceSwingWrapper.update(discountCardJdo);
            }
            showPostponedOperationsMessage();
//        } else {
//            form.showWarningMessage("mainForm.attention.message.dialog.title", "mainForm.handler.postponed.discountCards.not.exist.message");
        }
    }

    public void printItemClicked() {
        TablePrintPreviewComponent tablePrintPreviewComponent = new TablePrintPreviewComponent();
        boolean done = tablePrintPreviewComponent.showPrintPreviewDialog(form.getForm(), form.getArticleTableComponent().getArticlesTable(),
                messageSource, localeHolder);
        if (done) {
            form.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
        } else {
            form.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
        }
        form.getArticleTableComponent().initTableColumnsEditors();
/*
        MessageFormat header = new MessageFormat(messageSource.getMessage("mainForm.menu.table.print.header", null, localeHolder.getLocale()));
        MessageFormat footer = new MessageFormat(messageSource.getMessage("mainForm.menu.table.print.footer", null, localeHolder.getLocale()));
        boolean fitPageWidth = false;
        boolean showPrintDialog = true;
//        boolean interactive = false;
        boolean interactive = true;
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        HashPrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

        JTable.PrintMode printMode = fitPageWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            boolean complete = form.getArticleTableComponent().getArticlesTable().print(printMode, header, footer,
                    showPrintDialog, null, interactive, null);
            if (complete) {
                form.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
            } else {
                form.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
            }
        } catch (PRINTER_EXCEPTION e) {
            logger.warn(e.getMessage(), e);
            Toolkit.getDefaultToolkit().beep();
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.print.exception.message");
        }
*/
    }

    public void savePresetItemClicked() {
        String defaultPresetName = messageSource.getMessage("settings.user.preset.default.name", null, localeHolder.getLocale());
        if (!defaultPresetName.equals(usersSettingsHolder.getPresetName())) {
            form.holdAllTables();
            UsersSettings usersSettings = userSettingsService.updatePresetSettings();
            try {
                userSettingsService.saveSettings(usersSettings);
            } catch (IOException | JAXBException e) {
                logger.warn(e.getMessage(), e);
                Toolkit.getDefaultToolkit().beep();
                form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.save.usersSettings.error.message");
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void fixTableDataItemClicked() {
// todo fixTableData
    }

    public void moveToShopItemClicked() {
        String shop = (String) (form.showInputDialog("mainForm.menu.selected.moveToShop.select.title", "mainForm.menu.selected.moveToShop.select.message",
                null, shopArray, shopArray[0]));

        if (shop != null) {
            for (ArticleJdo articleJdo : form.getTableModel().getTableData()) {
                if (articleJdo.isSelected()) {
                    articleJdo.setShop(shop);
                    articleJdo.setSelected(false);
                    try {
                        articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, null);
                    } catch (IOException | ServiceException e) {
                        logger.warn(e.getMessage(), e);
                        Toolkit.getDefaultToolkit().beep();
                        articleJdo.setPostponedOperationDate(new Date());
                        form.showWarningMessage("mainForm.exception.message.dialog.title", "sellDialog.handler.sold.article.not.saved.to.worksheet");
                    }
                    articleServiceSwingWrapper.update(articleJdo);
                }
            }
            form.update();
        }
    }

    public void deselectArticlesItemClicked() {
        for (ArticleJdo articleJdo : form.getTableModel().getTableData()) {
            if (articleJdo.isSelected()) {
                articleJdo.setSelected(false);
            }
        }
        form.update();
    }

    public void createDefaultPreset() {
        String defaultPresetName = messageSource.getMessage("settings.user.preset.default.name", null, localeHolder.getLocale());
        usersSettingsHolder.setPresetName(defaultPresetName);
        form.holdAllTables();
        UsersSettings usersSettings = userSettingsService.updatePresetSettings();
        try {
            userSettingsService.saveSettings(usersSettings);
        } catch (IOException | JAXBException e) {
            logger.warn(e.getMessage(), e);
            Toolkit.getDefaultToolkit().beep();
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.save.usersSettings.error.message");
        }
    }


    public void selectPresetItemClicked() {
        String currentPresetName = usersSettingsHolder.getPresetName();
        Object[] selectionValues = userSettingsService.getUserPresetNames().toArray();
        String presetName = (String) form.showInputDialog("mainForm.menu.settings.select.title", "mainForm.menu.settings.select.message",
                null, selectionValues, currentPresetName);
        if (presetName != null) {
            usersSettingsHolder.setPresetName(presetName);
            form.initializeUserSettings();
            userSettingsService.getUserSettings().setLastPresetName(presetName);
            form.updatePresetNameField();
            try {
                userSettingsService.saveSettings(usersSettingsHolder.getUsersSettings());
            } catch (IOException | JAXBException e) {
                logger.warn(e.getMessage(), e);
                Toolkit.getDefaultToolkit().beep();
                form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.save.usersSettings.error.message");
            }
        }
    }

    public void createPresetItemClicked() {
        String presetName = (String) form.showInputDialog("mainForm.menu.settings.create.title", "mainForm.menu.settings.create.message",
                null, null, null);
        if (presetName != null) {
            form.holdAllTables();
            usersSettingsHolder.setPresetName(presetName);
            userSettingsService.updatePresetSettings();
        }

    }

    public void deletePresetItemClicked() {
        String currentPresetName = usersSettingsHolder.getPresetName();
        String defaultPresetName = messageSource.getMessage("settings.user.preset.default.name", null, localeHolder.getLocale());
        Object[] presetNames = userSettingsService.getUserPresetNames().toArray();
        if (presetNames.length < 2) {
            Toolkit.getDefaultToolkit().beep();
            form.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.delete.usersSettings.error.message");
            return;
        }
        Object[] selectionValues;
        if (currentPresetName.equals(defaultPresetName)) {
            selectionValues = new Object[presetNames.length - 1];
        } else {
            selectionValues = new Object[presetNames.length - 2];
        }
        int order = 0;
        for (Object preset : presetNames) {
            String presetName = (String) preset;
            if (!(currentPresetName.equals(presetName) || defaultPresetName.equals(presetName))) {
                selectionValues[order] = presetName;
                ++order;
            }
        }
        String presetName = (String) form.showInputDialog("mainForm.menu.settings.delete.title", "mainForm.menu.settings.select.message",
                null, selectionValues, selectionValues[0]);
        if (presetName != null) {
            userSettingsService.deletePresetSettings(presetName);
            try {
                userSettingsService.saveSettings(usersSettingsHolder.getUsersSettings());
            } catch (IOException | JAXBException e) {
                throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.JAXB_EXCEPTION, e);
            }
        }

    }

}
