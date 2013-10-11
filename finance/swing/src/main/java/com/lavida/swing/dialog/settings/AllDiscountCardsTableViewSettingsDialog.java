package com.lavida.swing.dialog.settings;

import com.lavida.swing.dialog.AbstractDialog;
import com.lavida.swing.dialog.AllDiscountCardsDialog;
import com.lavida.swing.form.component.TableViewComponent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;

/**
 * The AllDiscountCardsTableViewSettingsDialog
 * <p/>
 * Created: 24.09.13 22:40.
 *
 * @author Ruslan.
 */
@Component
public class AllDiscountCardsTableViewSettingsDialog extends AbstractDialog{

    @Resource
    private AllDiscountCardsDialog allDiscountCardsDialog;

    private TableViewComponent tableViewComponent = new TableViewComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.settings.view.table.articles.notSold", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {

    }

    public void postInit() {
        rootContainer.setLayout(new BorderLayout());

        tableViewComponent.initializeComponents(this, allDiscountCardsDialog.getCardTableComponent().getDiscountCardsTable(),
                allDiscountCardsDialog.getCardTableComponent().getHeadersAndColumnsMap(),
                messageSource, localeHolder);
        JPanel mainPanel = tableViewComponent.getMainPanel();
        rootContainer.add(mainPanel);

    }

    public TableViewComponent getTableViewComponent() {
        return tableViewComponent;
    }
}
