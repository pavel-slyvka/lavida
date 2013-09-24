package com.lavida.swing.dialog.settings;

import com.lavida.swing.dialog.AbstractDialog;
import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.form.component.TableViewComponent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;

/**
 * The SoldArticlesTableViewSettingsDialog
 * <p/>
 * Created: 24.09.13 21:47.
 *
 * @author Ruslan.
 */
@Component
public class SoldArticlesTableViewSettingsDialog extends AbstractDialog {

    @Resource
    private SoldProductsDialog soldProductsDialog;

    private TableViewComponent tableViewComponent = new TableViewComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.settings.view.table.articles.sold", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
    }

    public void postInit() {
        rootContainer.setLayout(new BorderLayout());

        tableViewComponent.initializeComponents(this, soldProductsDialog.getArticleTableComponent().getArticlesTable(),
                messageSource, localeHolder);
        JPanel mainPanel = tableViewComponent.getMainPanel();
        rootContainer.add(mainPanel);

    }

    }
