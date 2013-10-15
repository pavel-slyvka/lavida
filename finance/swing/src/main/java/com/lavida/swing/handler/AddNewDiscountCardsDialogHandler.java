package com.lavida.swing.handler;

import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import com.lavida.swing.form.component.TablePrintPreviewComponent;
import com.lavida.swing.service.ConcurrentOperationsService;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AddNewDiscountCardsDialog;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created: 17:06 07.09.13
 *
 * @author Ruslan
 */
@Component
public class AddNewDiscountCardsDialogHandler {
//    private static final Logger logger = LoggerFactory.getLogger(AddNewDiscountCardsDialogHandler.class);

    @Resource
    private AddNewDiscountCardsDialog dialog;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedCard(null);
        dialog.getTableModel().setTableData(new ArrayList<DiscountCardJdo>());
        dialog.getTableModel().fireTableDataChanged();
        dialog.hide();
        dialog.getMainForm().update();
    }

    public void addRowButtonClicked() {
        DiscountCardJdo discountCardJdo = new DiscountCardJdo();
        dialog.getTableModel().getTableData().add(discountCardJdo);
        dialog.getTableModel().fireTableDataChanged();
        int row = dialog.getTableModel().getTableData().size() - 1;
        dialog.getCardTableComponent().getDiscountCardsTable().editCellAt(row, 0);
        dialog.getCardTableComponent().getDiscountCardsTable().transferFocus();
    }

    public void deleteRowButtonClicked() {
        if (dialog.getCardTableComponent().getDiscountCardsTable().isEditing()) {
            return;
        }
        DiscountCardJdo discountCardJdo = dialog.getTableModel().getSelectedCard();
        dialog.getTableModel().getTableData().remove(discountCardJdo);
        dialog.getTableModel().setSelectedCard(null);
        dialog.getTableModel().fireTableDataChanged();
    }

    public void acceptCardsButtonClicked() {
        if (dialog.getCardTableComponent().getDiscountCardsTable().isEditing()) {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.discountCards.addNew.editing.not.finished.message");
            return;
        }
        concurrentOperationsService.startOperation("Accepting new discount cards", new Runnable() {
            @Override
            public void run() {
                dialog.getAcceptCardsButton().setEnabled(false);
                boolean postponed = false;
                RemoteUpdateException exception = null;
                String acceptingOk = messageSource.getMessage("dialog.discounts.card.addNew.accepting.ok.message", null, localeHolder.getLocale());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(acceptingOk);
                stringBuilder.append("\n");
                List<DiscountCardJdo> discountCardJdoList = dialog.getTableModel().getTableData();
                while (discountCardJdoList.size() > 0) {
                    DiscountCardJdo discountCardJdo = discountCardJdoList.get(0);
                    DiscountCardJdo oldDiscountCardJdo;
                    try {
                        oldDiscountCardJdo = (DiscountCardJdo) discountCardJdo.clone();
                    } catch (CloneNotSupportedException e) {
                        dialog.getAcceptCardsButton().setEnabled(true);
                        throw new RuntimeException(e);
                    }
                    String cardNumber = discountCardJdo.getNumber();
                    if (!StringUtils.isEmpty(cardNumber)) {
                        DiscountCardJdo existingCard = discountCardServiceSwingWrapper.getByNumber(cardNumber);
                        if (existingCard == null) {
                            discountCardJdo.setRegistrationDate(Calendar.getInstance());
                            discountCardJdo.setActivationDate(Calendar.getInstance());
                            try {
                                discountCardServiceSwingWrapper.updateToSpreadsheet(oldDiscountCardJdo, discountCardJdo);
                            } catch (RemoteUpdateException e) {
                                postponed = true;
                                exception = e;
                            }
                        } else {
                            discountCardJdo.setNumber(null);
                            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.sell.handler.discount.card.number.exists.message");
                            dialog.getTableModel().fireTableDataChanged();
                            dialog.getCardTableComponent().getCardFiltersComponent().updateAnalyzeComponent();
                            dialog.getAcceptCardsButton().setEnabled(true);
                            return;
                        }
                    } else {
                        discountCardJdo.setNumber(null);
                        dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.sell.handler.discount.card.number.enter.message");
                        dialog.getTableModel().fireTableDataChanged();
                        dialog.getCardTableComponent().getCardFiltersComponent().updateAnalyzeComponent();
                        dialog.getAcceptCardsButton().setEnabled(true);
                        return;
                    }
                    discountCardJdoList.remove(discountCardJdo);
                }
                dialog.getAcceptCardsButton().setEnabled(true);
                dialog.getTableModel().fireTableDataChanged();
                dialog.getCardTableComponent().getCardFiltersComponent().updateAnalyzeComponent();
                String message = convertToMultiline(new String(stringBuilder));
                dialog.getMainForm().showInfoToolTip(message);
                concurrentOperationsService.publishEvent();
                if (postponed) {
                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, exception);
                }
            }
        });
    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>"); //+ "</html>"
    }

    public void printItemClicked() {
        TablePrintPreviewComponent tablePrintPreviewComponent = new TablePrintPreviewComponent();
        boolean done = tablePrintPreviewComponent.showPrintPreviewDialog(dialog.getDialog(), dialog.getCardTableComponent().getDiscountCardsTable(),
                messageSource, localeHolder);
        if (done) {
            dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
        } else {
            dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                    messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
        }

/*
        MessageFormat header = new MessageFormat(messageSource.getMessage("dialog.discounts.card.menu.file.print.header", null, localeHolder.getLocale()));
        MessageFormat footer = new MessageFormat(messageSource.getMessage("mainForm.menu.table.print.footer", null, localeHolder.getLocale()));
        boolean fitPageWidth = false;
        boolean showPrintDialog = true;
        boolean interactive = true;
        JTable.PrintMode printMode = fitPageWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            boolean complete = dialog.getCardTableComponent().getDiscountCardsTable().print(printMode, header, footer,
                    showPrintDialog, null, interactive, null);
            if (complete) {
                dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.finished.message.body", null, localeHolder.getLocale()));
            } else {
                dialog.showInformationMessage("mainForm.menu.table.print.message.title",
                        messageSource.getMessage("mainForm.menu.table.print.cancel.message.body", null, localeHolder.getLocale()));
            }
        } catch (PrinterException e) {
            logger.warn(e.getMessage(), e);
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "mainForm.handler.print.exception.message");
        }
*/
    }
}
