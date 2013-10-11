package com.lavida.swing.handler;

import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.xml.PostponedType;
import com.lavida.service.xml.PostponedXmlService;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.PostponedChangesDialog;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import com.lavida.swing.form.component.FileChooserComponent;
import com.lavida.swing.service.ChangedFieldServiceSwingWrapper;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ConcurrentOperationsService;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The ArticlePostponedChangesDialogHandler
 * <p/>
 * Created: 10.10.13 11:55.
 *
 * @author Ruslan.
 */
@Component
public class PostponedChangesDialogHandler {
    //    private static final Logger logger = LoggerFactory.getLogger(ArticlePostponedChangesDialogHandler.class);
    private static final String DATE_FORMAT = "dd.MM.yyyy";

    @Resource
    private PostponedChangesDialog dialog;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource
    private ChangedFieldServiceSwingWrapper changedFieldServiceSwingWrapper;

    @Resource
    private PostponedXmlService postponedXmlService;

    @Resource
    private FileChooserComponent fileChooser;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;


    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedChangedField(null);
        dialog.hide();

    }

    public void deselectAllItemClicked() {
        for (ChangedFieldJdo changedFieldJdo : dialog.getTableModel().getTableData()) {
            if (changedFieldJdo.isSelected()) {
                changedFieldJdo.setSelected(false);
            }
        }

    }

    public void deleteSelectedItemClicked() {
        for (ChangedFieldJdo changedFieldJdo : dialog.getTableModel().getTableData()) {
            if (changedFieldJdo.isSelected()) {
                changedFieldServiceSwingWrapper.delete(changedFieldJdo);
            }
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
            int choice = fileChooser.showSaveDialog(dialog.getDialog());
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
        dialog.getMainForm().update();
        fileChooser.setSelectedFile(new File("postponed_" +
                new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".xml"));

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
            postponedXmlService.marshal(postponedType, file);
        } catch (JAXBException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.JAXB_EXCEPTION, e);
        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.IO_EXCEPTION, e);
        }
    }

    /**
     * Shows a dialog for choosing the xml file with postponed operations and loads them to the database.
     */
    public void loadPostponedItemClicked() {
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        fileChooser.setSelectedFile(null);
        File file;
        while (true) {
            int choice = fileChooser.showOpenDialog(dialog.getDialog());
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
        dialog.getMainForm().update();
        fileChooser.setSelectedFile(null);

    }

    /**
     * Loads postponed operations to the database.
     *
     * @param file the chosen xml file with postponed operations to be loaded.
     */
    public void loadPostponed(File file) {
        List<ArticleJdo> loadedArticles;
        List<DiscountCardJdo> loadedDiscountCards;
        PostponedType postponedType;
        try {
            postponedType = postponedXmlService.unmarshal(file);
        } catch (JAXBException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.JAXB_EXCEPTION, e);
        } catch (FileNotFoundException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_IO_EXCEPTION, e);
        }
        loadedArticles = postponedType.getArticles();
        loadedDiscountCards = postponedType.getDiscountCards();
        List<ChangedFieldJdo> changedFieldJdoList = changedFieldServiceSwingWrapper.getAll();
        if (loadedArticles != null) {
            List<ArticleJdo> forUpdateArticles = articleServiceSwingWrapper.mergePostponedWithDatabase(loadedArticles);
            for (ArticleJdo articleJdo : forUpdateArticles) {
                articleServiceSwingWrapper.update(articleJdo);
                if (!hasArticlePostponed(changedFieldJdoList, articleJdo)) {
                    ChangedFieldJdo changedFieldJdo = new ChangedFieldJdo(articleJdo.getPostponedOperationDate(), ChangedFieldJdo.ObjectType.ARTICLE,
                            articleJdo.getId(), articleJdo.getCode(), articleJdo.getSize(), null, null, null, null, articleJdo.getPostponedOperationDate());
                    changedFieldServiceSwingWrapper.update(changedFieldJdo);
                }
            }
            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
        }
        if (loadedDiscountCards != null) {
            List<DiscountCardJdo> forUpdateDiscountCards = discountCardServiceSwingWrapper.mergePostponedWithDatabase(loadedDiscountCards);
            for (DiscountCardJdo discountCardJdo : forUpdateDiscountCards) {
                discountCardServiceSwingWrapper.update(discountCardJdo);
                if (!hasDiscountCardPostponed (changedFieldJdoList, discountCardJdo)) {
                    ChangedFieldJdo changedFieldJdo = new ChangedFieldJdo(discountCardJdo.getPostponedDate(), ChangedFieldJdo.ObjectType.DISCOUNT_CARD,
                            discountCardJdo.getId(), discountCardJdo.getNumber(), null, null, null, null, null, discountCardJdo.getPostponedDate());
                    changedFieldServiceSwingWrapper.update(changedFieldJdo);
                }
            }
            dialog.getMainForm().getHandler().showPostponedOperationsMessage();
        }
    }

    private boolean hasDiscountCardPostponed(List<ChangedFieldJdo> changedFieldJdoList, DiscountCardJdo discountCardJdo) {
        for (ChangedFieldJdo changedFieldJdo : changedFieldJdoList) {
            if (ChangedFieldJdo.ObjectType.DISCOUNT_CARD.equals(changedFieldJdo.getObjectType())) {
                if (changedFieldJdo.getPostponedDate() != null && changedFieldJdo.getObjectId() == discountCardJdo.getId() ){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasArticlePostponed(List<ChangedFieldJdo> changedFieldJdoList, ArticleJdo articleJdo) {
        for (ChangedFieldJdo changedFieldJdo : changedFieldJdoList) {
            if (ChangedFieldJdo.ObjectType.ARTICLE.equals(changedFieldJdo.getObjectType())) {
                if (changedFieldJdo.getPostponedDate() != null && changedFieldJdo.getObjectId() == articleJdo.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void recommitPostponedItemClicked() {
        concurrentOperationsService.startOperation("Recommit.", new Runnable() {
            @Override
            public void run() {
                dialog.getRecommitPostponedItem().setEnabled(false);
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                boolean postponed = false;
                RemoteUpdateException exception = null;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(messageSource.getMessage("dialog.changed.field.postponed.recommit.finished.message",
                        null, localeHolder.getLocale()));
                stringBuilder.append("\n");

                List<ArticleJdo> articles = articleServiceSwingWrapper.getAll();
                for (ArticleJdo articleJdo : articles) {
                    if (articleJdo.getPostponedOperationDate() != null) {
                        ArticleJdo oldArticle;
                        try {
                            oldArticle = (ArticleJdo) articleJdo.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                        changedFieldServiceSwingWrapper.delete(articleJdo);

                        if (articleJdo.getSold() != null && (dateFormat.format(articleJdo.getPostponedOperationDate())).
                                equals(formatCalendar(articleJdo.getSaleDate()))) {   //recommit selling
                            try {
                                articleJdo.setPostponedOperationDate(null);
                                articleServiceSwingWrapper.updateToSpreadsheet(oldArticle, articleJdo, true);
                            } catch (RemoteUpdateException e) {
                                postponed = true;
                                exception = e;
                            }
                        } else if (articleJdo.getSold() == null && dateFormat.format(articleJdo.getRefundDate()).
                                equals(dateFormat.format(articleJdo.getRefundDate()))) {  // recommit refunding
                            try {
                                articleJdo.setPostponedOperationDate(null);
                                articleServiceSwingWrapper.updateToSpreadsheet(oldArticle, articleJdo, false);
                            } catch (RemoteUpdateException e) {
                                postponed = true;
                                exception = e;
                            }
                            articleJdo.setSaleDate(null);
                        } else {
                            try {
                                articleJdo.setPostponedOperationDate(null);
                                articleServiceSwingWrapper.updateToSpreadsheet(oldArticle, articleJdo, null); // recommit other changes
                            } catch (RemoteUpdateException e) {
                                postponed = true;
                                exception = e;

                            }
                        }
                    }
                }

                List<DiscountCardJdo> discountCardJdoList = discountCardServiceSwingWrapper.getAll();
                for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
                    if (discountCardJdo.getPostponedDate() != null) {
                        discountCardJdo.setPostponedDate(null);
                        changedFieldServiceSwingWrapper.delete(discountCardJdo);
                        DiscountCardJdo oldDiscountCardJdo;
                        try {
                            oldDiscountCardJdo = (DiscountCardJdo) discountCardJdo.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            discountCardServiceSwingWrapper.updateToSpreadsheet(oldDiscountCardJdo, discountCardJdo);
                        } catch (RemoteUpdateException e) {
                            postponed = true;
                            exception = e;
                        }
                    }
                }
                String message = convertToMultiline(new String(stringBuilder));
                dialog.getMainForm().showInfoToolTip(message);
                dialog.getRecommitPostponedItem().setEnabled(true);
                dialog.getMainForm().getHandler().showPostponedOperationsMessage();
                dialog.getMainForm().update();
                if (postponed) {
                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, exception);
                }
            }

        });

    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>"); //+ "</html>"
    }

    private String formatCalendar(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (calendar != null) {
            return dateFormat.format(calendar.getTime());
        } else return null;
    }

    /**
     * Deletes all postponed operations from the database.
     */
    public void deletePostponedItemClicked() {
        int result = dialog.showConfirmDialog("mainForm.menu.postponed.delete.message.title",
                "mainForm.menu.postponed.delete.message.body");
        switch (result) {
            case JOptionPane.YES_OPTION:
                deletePostponedOperations();
                dialog.getMainForm().update();
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CLOSED_OPTION:
        }

    }

    /**
     * Deletes all postponed operations from the database and updates the PostponedOperationsMessage label of the
     * mainForm.
     */
    public void deletePostponedOperations() {
        List<ArticleJdo> allArticles = articleServiceSwingWrapper.getAll();
        for (ArticleJdo articleJdo : allArticles) {
            if (articleJdo.getPostponedOperationDate() != null) {
                articleJdo.setPostponedOperationDate(null);
                changedFieldServiceSwingWrapper.delete(articleJdo);
                articleServiceSwingWrapper.update(articleJdo);
            }
        }
        List<DiscountCardJdo> discountCardJdoList = discountCardServiceSwingWrapper.getAll();
        for (DiscountCardJdo discountCardJdo : discountCardJdoList) {
            if (discountCardJdo.getPostponedDate() != null) {
                discountCardJdo.setPostponedDate(null);
                changedFieldServiceSwingWrapper.delete(discountCardJdo);
                discountCardServiceSwingWrapper.update(discountCardJdo);
            }
        }

        dialog.getMainForm().getHandler().showPostponedOperationsMessage();
    }


}
