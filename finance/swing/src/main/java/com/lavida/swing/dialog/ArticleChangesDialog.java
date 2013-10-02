package com.lavida.swing.dialog;

import com.lavida.swing.form.component.ArticleChangedFieldTableComponent;
import com.lavida.swing.handler.ArticleChangesDialogHandler;
import com.lavida.swing.service.ArticleChangedFieldTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ArticleChangesDialog
 * <p/>
 * Created: 02.10.13 13:20.
 *
 * @author Ruslan.
 */
@Component
public class ArticleChangesDialog extends AbstractDialog {

    @Resource
    private ArticleChangesDialogHandler handler;

    @Resource(name = "articleChangedFieldTableModel")
    private ArticleChangedFieldTableModel tableModel;

    private JPanel  southPanel, desktopPanel, filtersPanel, mainPanel, buttonPanel;
    private JButton  cancelButton;
    private JMenuBar menuBar;
    private JMenu  selectedMenu;
    private JMenuItem  deselectAllItem, deleteSelectedItem, revertChangesItem;
    private ArticleChangedFieldTableComponent tableComponent = new ArticleChangedFieldTableComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.changed.field.article.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 900, 700);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//        menuBar
        initializeMenuBar();
        dialog.setJMenuBar(menuBar);

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder());

        tableComponent.initializeComponents(tableModel, messageSource, localeHolder);
//      main panel for table
        mainPanel = tableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(1,5,0,5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);
//        south panel for desktopPanel

        southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);
        southPanel.setBorder(BorderFactory.createEmptyBorder());

//      panel for search operations
        filtersPanel= tableComponent.getArticleChangedFieldFiltersComponent().getFiltersPanel();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        southPanel.add(filtersPanel, constraints);
        desktopPanel.add(southPanel, BorderLayout.SOUTH);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        south panel for buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        cancelButton = new JButton();
        cancelButton.setHorizontalTextPosition(JButton.CENTER);
        cancelButton.setPreferredSize(new Dimension(150, 25));
        cancelButton.setMaximumSize(new Dimension(150, 25));
        cancelButton.setMinimumSize(new Dimension(150, 25));
        cancelButton.setText(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.cancelButtonClicked();
            }
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);

        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeMenuBar() {
        menuBar = new JMenuBar();

        //        selected menu
        selectedMenu = new JMenu();
        selectedMenu.setText(messageSource.getMessage("mainForm.menu.selected", null, localeHolder.getLocale()));

        deselectAllItem = new JMenuItem();
        deselectAllItem.setText(messageSource.getMessage("mainForm.menu.selected.deselect.articles", null, localeHolder.getLocale()));
        deselectAllItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deselectAllItemClicked();
            }
        });

        deleteSelectedItem = new JMenuItem();
        deleteSelectedItem.setText(messageSource.getMessage("dialog.changed.field.article.menu.selected.delete", null, localeHolder.getLocale()));
        deleteSelectedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deleteSelectedItemClicked();
            }
        });

        revertChangesItem = new JMenuItem();
        revertChangesItem.setText(messageSource.getMessage("dialog.changed.field.article.menu.selected.revertChanges", null, localeHolder.getLocale()));
        revertChangesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.revertChangesItemClicked();
            }
        });

        selectedMenu.add(deselectAllItem);
        selectedMenu.add(deleteSelectedItem);
        selectedMenu.add(revertChangesItem);

        menuBar.add(selectedMenu);

    }

    public ArticleChangedFieldTableModel getTableModel() {
        return tableModel;
    }
}
