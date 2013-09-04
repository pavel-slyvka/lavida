package com.lavida.swing.dialog;

import com.lavida.swing.form.component.AddNewArticleTableComponent;
import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.handler.AddNewProductsDialogHandler;
import com.lavida.swing.service.AddNewArticleTableModel;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AddNewProductsDialog is a dialog for adding new products to the database and the spreadsheet.
 * Created: 19:54 02.09.13
 *
 * @author Ruslan
 */
@Component
public class AddNewProductsDialog extends AbstractDialog {

    @Resource(name = "addNewArticleTableModel")
    private AddNewArticleTableModel tableModel;

    @Resource
    private AddNewProductsDialogHandler handler;

    private AddNewArticleTableComponent articleTableComponent = new AddNewArticleTableComponent();

    private JPanel operationPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel,
            buttonPanel;
    private JButton addRowButton, deleteRowButton, acceptProductsButton, cancelButton;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.add.new.products.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 900, 700);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder());

        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);

//      main panel for table of goods
        mainPanel = articleTableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(1,5,0,5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);


//        south panel for desktopPanel
        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder());

        //      analyze panel for total analyses
        analyzePanel = articleTableComponent.getAnalyzeComponent().getAnalyzePanel();
        analyzePanel.setPreferredSize(new Dimension(890, 25));
        analyzePanel.setMinimumSize(new Dimension(800, 25));
        analyzePanel.setMaximumSize(new Dimension(1500, 25));


        operationPanel = new JPanel();
        operationPanel.setLayout(new GridBagLayout());
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder()));

        addRowButton = new JButton();
        addRowButton.setHorizontalTextPosition(JButton.CENTER);
        addRowButton.setPreferredSize(new Dimension(150, 25));
        addRowButton.setMaximumSize(new Dimension(150, 25));
        addRowButton.setMinimumSize(new Dimension(150, 25));
        addRowButton.setText(messageSource.getMessage("dialog.add.new.products.button.row.add", null, localeHolder.getLocale()));
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.addRowButtonClicked();
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.weightx = 1.0;
        operationPanel.add(addRowButton, constraints);

        deleteRowButton = new JButton();
        deleteRowButton.setHorizontalTextPosition(JButton.CENTER);
        deleteRowButton.setPreferredSize(new Dimension(150, 25));
        deleteRowButton.setMaximumSize(new Dimension(150, 25));
        deleteRowButton.setMinimumSize(new Dimension(150, 25));
        deleteRowButton.setText(messageSource.getMessage("dialog.add.new.products.button.row.delete", null, localeHolder.getLocale()));
        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deleteRowButtonClicked();
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.weightx = 1.0;
        operationPanel.add(deleteRowButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.REMAINDER;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        southPanel.add(analyzePanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 1.0;
        southPanel.add(operationPanel, constraints);

        desktopPanel.add(southPanel, BorderLayout.SOUTH);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        south panel for buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        acceptProductsButton = new JButton();
        acceptProductsButton.setHorizontalTextPosition(JButton.CENTER);
        acceptProductsButton.setPreferredSize(new Dimension(150, 25));
        acceptProductsButton.setMaximumSize(new Dimension(150, 25));
        acceptProductsButton.setMinimumSize(new Dimension(150, 25));
        acceptProductsButton.setText(messageSource.getMessage("dialog.add.new.products.button.acceptAll", null, localeHolder.getLocale()));
        acceptProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.acceptProductsButtonClicked();
            }
        });
        buttonPanel.add(acceptProductsButton);

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
        buttonPanel.add(cancelButton);

        rootContainer.add(buttonPanel, BorderLayout.SOUTH);

    }

    public AddNewArticleTableModel getTableModel() {
        return tableModel;
    }

    public AddNewArticleTableComponent getArticleTableComponent() {
        return articleTableComponent;
    }
}
