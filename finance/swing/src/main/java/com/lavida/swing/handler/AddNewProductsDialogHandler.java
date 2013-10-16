package com.lavida.swing.handler;

import com.lavida.service.ArticleCalculator;
import com.lavida.service.UserService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.remote.google.LavidaGoogleException;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AddNewProductsDialog;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import com.lavida.swing.form.component.TablePrintPreviewComponent;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ConcurrentOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The AddNewProductsDialogHandler is a handler for the {@link com.lavida.swing.dialog.AddNewProductsDialog}.
 * Created: 21:57 02.09.13
 *
 * @author Ruslan
 */
@Component
public class AddNewProductsDialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddNewProductsDialogHandler.class);

    @Resource
    private AddNewProductsDialog dialog;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    private ArticleCalculator articleCalculator;

    @Resource
    private UserService userService;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    private static final String FILE_PATH = System.getProperty("user.dir") + System.getProperty("file.separator") + "productsAutoSave.xml";
    private static final File FILE_TO_SAVE = new File(FILE_PATH);

    public void addRowButtonClicked() {
        ArticleJdo articleJdo = new ArticleJdo();
        articleJdo.setQuantity(1);
        articleJdo.setMultiplier(2.5);
        articleJdo.setSalePrice(-1.0);
        articleJdo.setShop(messageSource.getMessage("sellDialog.text.field.shop.LaVida", null, localeHolder.getLocale()));
        if (dialog.getTableModel().getTableData().size() == 0) {
            articleJdo.setDeliveryDate(Calendar.getInstance());
        }
        if (dialog.getTableModel().getTableData().size() > 0) {
            if (dialog.getTableModel().getTableData().get(0).getDeliveryDate() == null) {
                dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.add.new.product.deliveryDate.not.filled.message");
                return;
            } else {
                articleJdo.setDeliveryDate(dialog.getTableModel().getTableData().get(0).getDeliveryDate());
            }
        }
        dialog.getTableModel().getTableData().add(articleJdo);
        dialog.getTableModel().fireTableDataChanged();
        int row = dialog.getTableModel().getTableData().size() - 1;
        dialog.getArticleTableComponent().getArticlesTable().editCellAt(row, 1);
        dialog.getArticleTableComponent().getArticlesTable().transferFocus();
        dialog.getTableModel().setSelectedArticle(null);
    }

    public void deleteRowButtonClicked() {
        ArticleJdo selectedArticle = dialog.getTableModel().getSelectedArticle();
        dialog.getTableModel().getTableData().remove(selectedArticle);
        dialog.getTableModel().setSelectedArticle(null);
        dialog.getTableModel().fireTableDataChanged();
    }

    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedArticle(null);
        if (dialog.getTableModel().getTableData().size() > 0) {
            int result = dialog.showConfirmDialog("dialog.add.new.products.save.confirm.title", "dialog.add.new.products.save.confirm.message");
            switch (result) {
                case JOptionPane.YES_OPTION:
                    saveData();
                    break;
                case JOptionPane.NO_OPTION:
                    break;
            }

        }
        dialog.getTableModel().setTableData(new ArrayList<ArticleJdo>());
        dialog.getTableModel().fireTableDataChanged();
        dialog.hide();
        dialog.getMainForm().update();
    }

    public void acceptProductsButtonClicked() {
        saveData();
        if (userService.hasForbiddenRole()) {
            return;
        }
        final String acceptingOk = messageSource.getMessage("dialog.add.new.products.accepting.ok.message", null, localeHolder.getLocale());
        concurrentOperationsService.startOperation("Accept new products", new Runnable() {
            @Override
            public void run() {
                dialog.getAcceptProductsButton().setEnabled(false);
                boolean postponed = false;
                Exception exception = null;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(acceptingOk);
                stringBuilder.append("\n");
                List<ArticleJdo> newArticles = dialog.getTableModel().getTableData();
                while (newArticles.size() > 0) {
                    ArticleJdo newArticle = newArticles.get(0);
                    ArticleJdo oldArticle;
                    try {
                        oldArticle = (ArticleJdo) newArticle.clone();
                    } catch (CloneNotSupportedException e) {
                        dialog.getAcceptProductsButton().setEnabled(true);
                        throw new RuntimeException(e);
                    }
                    if (newArticle.getCode().isEmpty() || newArticle.getDeliveryDate() == null
                            || newArticle.getTotalCostUAH() == 0 || newArticle.getSalePrice() == -1.0) {
                        dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.add.new.product.code.deliveryDate.totalCostUAH.not.filled.message");
                        dialog.getTableModel().fireTableDataChanged();
                        dialog.getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
                        dialog.getAcceptProductsButton().setEnabled(true);
                        return;
                    }
                    try {
                        articleServiceSwingWrapper.updateToSpreadsheet(oldArticle, newArticle, null);
                    } catch (RemoteUpdateException | LavidaGoogleException e) {
                        postponed = true;
                        exception = e;
                    }
                    newArticles.remove(newArticle);
                }
                dialog.getTableModel().fireTableDataChanged();
                dialog.getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
                dialog.getAcceptProductsButton().setEnabled(true);
                String message = convertToMultiline(new String(stringBuilder));
                dialog.getMainForm().showInfoToolTip(message);
                if (postponed) {
                    if (exception instanceof RemoteUpdateException) {
                        throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, exception);
                    } else if (exception instanceof LavidaGoogleException) {
                        throw new LavidaSwingRuntimeException(((LavidaGoogleException) exception).getErrorCode(), exception);
                    }
                }
            }
        });

    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>"); //+ "</html>"
    }

    public void copyRowButtonClicked() {
        if (dialog.getTableModel().getSelectedArticle() == null) {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.sold.article.not.chosen");
            return;
        }
        ArticleJdo copiedArticle;
        try {
            copiedArticle = (ArticleJdo) dialog.getTableModel().getSelectedArticle().clone();
        } catch (CloneNotSupportedException e) {
            logger.warn(e.getMessage(), e);
            return;
        }
        dialog.getTableModel().getTableData().add(copiedArticle);
        dialog.getTableModel().fireTableDataChanged();
        dialog.getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();

    }

    public void printItemClicked() {
        TablePrintPreviewComponent tablePrintPreviewComponent = new TablePrintPreviewComponent();
        boolean done = tablePrintPreviewComponent.showPrintPreviewDialog(dialog.getDialog(), dialog.getArticleTableComponent().getArticlesTable(),
                messageSource, localeHolder);
        if (done) {
            dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
        } else {
            dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
        }
    }

    public void calculateTransportCostEURItemClicked() {
        if (dialog.getTableModel().getTableData().size() > 0) {
            String totalTransportCostEURStr = (String) dialog.showInputDialog("dialog.add.new.products.calculate.transportCost.message",
                    "dialog.add.new.products.calculate.transportCost.title", null, null, null);
            if (totalTransportCostEURStr != null) {
                totalTransportCostEURStr = totalTransportCostEURStr.replace(",", ".").replaceAll("[^0-9.]", "");
                double totalTransportCostEUR;
                if (!StringUtils.isEmpty(totalTransportCostEURStr)) {
                    totalTransportCostEUR = Double.parseDouble(totalTransportCostEURStr);
                } else {
                    dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.add.new.product.totalTransportCostEUR.not.filled.message");
                    calculateTransportCostEURItemClicked();
                    return;
                }
                double totalPurchaseCostEUR = Double.parseDouble(dialog.getArticleTableComponent().getArticleFiltersComponent().
                        getArticleAnalyzeComponent().getTotalPurchaseCostEURField().getText());
                double aspectRatio;
                if (totalPurchaseCostEUR > 0.0) {
                    aspectRatio = totalTransportCostEUR / totalPurchaseCostEUR;
                } else {
                    dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.add.new.product.purchasePriceEUR.not.filled.message");
                    return;
                }
                for (ArticleJdo articleJdo : dialog.getTableModel().getTableData()) {
                    double purchasePriceEUR = articleJdo.getPurchasePriceEUR();
                    double transportCostEUR = aspectRatio * purchasePriceEUR;
                    transportCostEUR = BigDecimal.valueOf(transportCostEUR).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    articleJdo.setTransportCostEUR(transportCostEUR);
                    articleCalculator.calculateTotalCostsAndCalculatedSalePrice(articleJdo);

                }
                dialog.getTableModel().fireTableDataChanged();
                dialog.getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();
            }
        } else {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.add.new.product.articles.not.added.message");
        }
    }

    public void saveItemClicked() {
        saveData();
    }

    private void saveData() {
        try {
            articleServiceSwingWrapper.saveToXml(dialog.getTableModel().getTableData(), FILE_TO_SAVE);
        } catch (JAXBException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.JAXB_EXCEPTION, e);
        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.IO_EXCEPTION, e);
        }
    }

    public void openItemClicked() {
        openData(FILE_TO_SAVE);
        dialog.getTableModel().fireTableDataChanged();
        dialog.getArticleTableComponent().getArticleFiltersComponent().updateAnalyzeComponent();

    }

    private void openData(File file) {
        List<ArticleJdo> loadedArticles;
        try {
            loadedArticles = articleServiceSwingWrapper.loadFromXml(file);
            dialog.getTableModel().getTableData().addAll(loadedArticles);
        } catch (JAXBException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.JAXB_EXCEPTION, e);
        } catch (IOException e) {
            throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.IO_EXCEPTION, e);
        }
    }

    public void deselectArticlesItemClicked() {
        if (dialog.getTableModel().getTableData().size() > 0) {
            for (ArticleJdo articleJdo : dialog.getTableModel().getTableData()) {
                if (articleJdo.isSelected()) {
                    articleJdo.setSelected(false);
                }
            }
            dialog.getTableModel().fireTableDataChanged();
        }
    }
}
