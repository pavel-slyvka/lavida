package com.lavida.swing.dialog;

import com.lavida.swing.form.component.TablePrintPreviewComponent;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * The TablePrintPreviewDialog
 * <p/>
 * Created: 04.10.13 10:45.
 *
 * @author Ruslan.
 */
@Component
public class TablePrintPreviewDialog extends AbstractDialog {

    private TablePrintPreviewComponent tablePrintPreviewComponent = new TablePrintPreviewComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.print.preview.table", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 900, 700);
        dialog.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {

    }

    public void showPrintPreview() {

    }
}
