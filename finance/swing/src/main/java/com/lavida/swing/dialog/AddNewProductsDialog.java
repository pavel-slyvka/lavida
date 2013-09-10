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
    private JButton addRowButton, deleteRowButton, acceptProductsButton, cancelButton;
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
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        //      analyze panel for total analyses
        analyzePanel = articleTableComponent.getArticleFiltersComponent().getArticleAnalyzeComponent().getAnalyzePanel();
        analyzePanel.setLayout(new BoxLayout(analyzePanel, BoxLayout.LINE_AXIS));
        analyzePanel.setPreferredSize(new Dimension(890, 25));
        analyzePanel.setMinimumSize(new Dimension(800, 25));
        analyzePanel.setMaximumSize(new Dimension(1500, 25));

        JPanel operations = new JPanel();
        operations.setLayout(new BoxLayout(operations, BoxLayout.LINE_AXIS));

        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.PAGE_AXIS));
        operationPanel.setPreferredSize(new Dimension(170, 85));
        operationPanel.setMaximumSize(new Dimension(170, 85));
        operationPanel.setMinimumSize(new Dimension(170, 85));
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

        operationPanel.add(addRowButton);
        operationPanel.add(Box.createVerticalStrut(5));
        operationPanel.add(deleteRowButton);
        operations.add(operationPanel);
        operations.add(Box.createHorizontalGlue());

        southPanel.add(analyzePanel);
        southPanel.add(operations);

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
