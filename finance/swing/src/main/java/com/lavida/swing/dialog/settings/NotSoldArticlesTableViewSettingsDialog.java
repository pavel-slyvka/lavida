package com.lavida.swing.dialog.settings;

import com.lavida.swing.dialog.AbstractDialog;
import com.lavida.swing.form.component.TableViewComponent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

/**
 * The dialog for choosing which columns to show or hide.
 * Created: 12:40 04.09.13
 *
 * @author Ruslan
 */
@Component
public class NotSoldArticlesTableViewSettingsDialog extends AbstractDialog {

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

        tableViewComponent.initializeComponents(this, mainForm.getArticleTableComponent().getArticlesTable(),
                messageSource, localeHolder);
        JPanel mainPanel = tableViewComponent.getMainPanel();
        rootContainer.add(mainPanel);

    }

    public  boolean applyUserSettings() {
        String presetName = usersSettingsHolder.getPresetName();

        return false;
    }
}
