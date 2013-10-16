package com.lavida.swing.handler;

import com.lavida.service.remote.google.LavidaGoogleException;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import com.lavida.swing.form.component.TablePrintPreviewComponent;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AllDiscountCardsDialog;
import com.lavida.swing.service.ConcurrentOperationsService;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.Calendar;

/**
 * The handler for the {@link com.lavida.swing.dialog.AllDiscountCardsDialog}.
 * Created: 11:34 06.09.13
 *
 * @author Ruslan
 */
@Component
public class AllDiscountCardsDialogHandler {
//    private static final Logger logger = LoggerFactory.getLogger(AllDiscountCardsDialogHandler.class);

    @Resource
    private AllDiscountCardsDialog dialog;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource
    protected MessageSource messageSource;

    @Resource
    protected LocaleHolder localeHolder;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;

    public void activateButtonClicked() {
        if (dialog.getTableModel().getSelectedCard() != null) {
            DiscountCardJdo discountCardJdo = dialog.getTableModel().getSelectedCard();
            DiscountCardJdo oldDiscountCardJdo;
            try {
                oldDiscountCardJdo = (DiscountCardJdo) discountCardJdo.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (discountCardJdo.getActivationDate() == null) {
                int result = dialog.showConfirmDialog("dialog.discounts.card.all.confirm.activate", "dialog.discounts.card.all.confirm.activate.question");
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        discountCardJdo.setActivationDate(Calendar.getInstance());
                        changeDiscountCard(oldDiscountCardJdo, discountCardJdo);
                        dialog.getTableModel().setSelectedCard(null);
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CLOSED_OPTION:
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                }
            }
        } else {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.discounts.allCards.not.chosen");
        }
    }

    private void changeDiscountCard(final DiscountCardJdo oldDiscountCardJdo, final DiscountCardJdo discountCardJdo) {
        concurrentOperationsService.startOperation("Discount card activation state changing" , new Runnable() {
            @Override
            public void run() {
                boolean postponed = false;
                Exception exception = null;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(messageSource.getMessage("dialog.discounts.card.all.activation.finished.message",
                        null, localeHolder.getLocale()));
                stringBuilder.append("\n");

                try {
                    discountCardServiceSwingWrapper.updateToSpreadsheet(oldDiscountCardJdo, discountCardJdo);
                } catch (RemoteUpdateException | LavidaGoogleException e) {
                    postponed = true;
                    exception = e;
                }
                dialog.getTableModel().fireTableDataChanged();
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

    public void disableButtonClicked() {
        if (dialog.getTableModel().getSelectedCard() != null) {
            DiscountCardJdo discountCardJdo = dialog.getTableModel().getSelectedCard();
            DiscountCardJdo oldDiscountCardJdo;
            try {
                oldDiscountCardJdo = (DiscountCardJdo) discountCardJdo.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (discountCardJdo.getActivationDate() != null) {
                int result = dialog.showConfirmDialog("dialog.discounts.card.all.confirm.disable", "dialog.discounts.card.all.confirm.disable.question");
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        discountCardJdo.setActivationDate(null);
                        changeDiscountCard(oldDiscountCardJdo, discountCardJdo);
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CLOSED_OPTION:
                        dialog.getTableModel().setSelectedCard(null);
                        dialog.getTableModel().fireTableDataChanged();
                        break;
                }
            }
        } else {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.discounts.allCards.not.chosen");
        }
    }

    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedCard(null);
        dialog.hide();
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

    }
}
