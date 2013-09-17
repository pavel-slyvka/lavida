package com.lavida.swing.dialog;

import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.handler.AddNewProductsDialogHandler;
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
    private ArticlesTableModel tableModel;

    @Resource
    private AddNewProductsDialogHandler handler;

    private ArticleTableComponent articleTableComponent = new ArticleTableComponent();

    private JPanel operationPanel, southPanel, desktopPanel, analyzePanel, mainPanel,
            buttonPanel;
    private JButton addRowButton, deleteRowButton, acceptProductsButton, cancelButton, copyRowButton;
    private JLabel errorMessage;

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
        southPanel = new JPanel();
        southPanel.setLayout(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 5, 2, 5);


        //      analyze panel for total analyses
        analyzePanel = articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().getAnalyzePanel();

        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.PAGE_AXIS));
//        operationPanel.setPreferredSize(new Dimension(170, 85));
//        operationPanel.setMaximumSize(new Dimension(170, 85));
//        operationPanel.setMinimumSize(new Dimension(170, 85));
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));

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

        copyRowButton = new JButton();
        copyRowButton.setHorizontalTextPosition(JButton.CENTER);
        copyRowButton.setPreferredSize(new Dimension(150, 25));
        copyRowButton.setMaximumSize(new Dimension(150, 25));
        copyRowButton.setMinimumSize(new Dimension(150, 25));
        copyRowButton.setText(messageSource.getMessage("dialog.add.new.products.button.row.copy", null, localeHolder.getLocale()));
        copyRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.copyRowButtonClicked();
            }
        });

        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(addRowButton);
        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(deleteRowButton);
        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(copyRowButton);
        operationPanel.add(Box.createVerticalGlue());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        constraints.weighty = 1.0;
        southPanel.add(operationPanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        southPanel.add(analyzePanel, constraints);

        desktopPanel.add(southPanel, BorderLayout.SOUTH);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        south panel for buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);
        errorMessage.setHorizontalAlignment(JLabel.LEFT);

        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.LINE_AXIS));
        errorPanel.setPreferredSize(new Dimension(200, 30));
        errorPanel.setMinimumSize(new Dimension(100, 30));
        errorPanel.setMaximumSize(new Dimension(500, 50));
        errorPanel.add(errorMessage);
        buttonPanel.add(errorPanel);
        buttonPanel.add(Box.createHorizontalGlue());

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));


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

        buttons.add(acceptProductsButton);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(cancelButton);
        buttonPanel.add(buttons);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);

        installEscapeCloseOperation(dialog);

    }


    public ArticlesTableModel getTableModel() {
        return tableModel;
    }

    public ArticleTableComponent getArticleTableComponent() {
        return articleTableComponent;
    }

    public JLabel getErrorMessage() {
        return errorMessage;
    }
}
