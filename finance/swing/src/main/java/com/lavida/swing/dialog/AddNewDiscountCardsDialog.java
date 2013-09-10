package com.lavida.swing.dialog;

import com.lavida.swing.form.component.DiscountCardTableComponent;
import com.lavida.swing.handler.AddNewDiscountCardsDialogHandler;
import com.lavida.swing.service.DiscountCardsTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The dialog for adding new discount cards to the database.
 * Created: 17:04 07.09.13
 *
 * @author Ruslan
 */
@Component
public class AddNewDiscountCardsDialog extends AbstractDialog {

    @Resource
    private AddNewDiscountCardsDialogHandler handler;

    @Resource(name = "addNewDiscountCardsTableModel")
    private DiscountCardsTableModel tableModel;

    private JPanel operationPanel, southPanel, desktopPanel, analyzePanel, mainPanel,
            buttonPanel;
    private JLabel errorMessage;
    private JButton addRowButton, deleteRowButton, acceptCardsButton, cancelButton;
    private DiscountCardTableComponent cardTableComponent = new DiscountCardTableComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.discounts.card.addNew.title", null, localeHolder.getLocale()));
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

        cardTableComponent.initializeComponents(tableModel, messageSource, localeHolder);


//      panel for search operations

//      main panel for table of goods
        mainPanel = cardTableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(1,5,0,5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);


//        south panel for desktopPanel
        southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

//      analyze panel for total analyses
        analyzePanel = cardTableComponent.getCardFiltersComponent().getCardAnalyzeComponent().getAnalyzePanel();
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
        addRowButton.setText(messageSource.getMessage("dialog.discounts.card.addNew.button.addRow", null, localeHolder.getLocale()));
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.addRowButtonClicked();
            }
        });

        deleteRowButton = new JButton();
        deleteRowButton.setText(messageSource.getMessage("dialog.discounts.card.addNew.button.deleteRow", null, localeHolder.getLocale()));
        deleteRowButton.setHorizontalTextPosition(JButton.CENTER);
        deleteRowButton.setPreferredSize(new Dimension(150, 25));
        deleteRowButton.setMaximumSize(new Dimension(150, 25));
        deleteRowButton.setMinimumSize(new Dimension(150, 25));
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

        acceptCardsButton = new JButton();
        acceptCardsButton.setHorizontalTextPosition(JButton.CENTER);
        acceptCardsButton.setPreferredSize(new Dimension(150, 25));
        acceptCardsButton.setMaximumSize(new Dimension(150, 25));
        acceptCardsButton.setMinimumSize(new Dimension(150, 25));
        acceptCardsButton.setText(messageSource.getMessage("dialog.discounts.card.button.acceptCard", null, localeHolder.getLocale()));
        acceptCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.acceptCardsButtonClicked();
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

        buttons.add(acceptCardsButton);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(cancelButton);
        buttonPanel.add(buttons);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    public DiscountCardsTableModel getTableModel() {
        return tableModel;
    }

    public DiscountCardTableComponent getCardTableComponent() {
        return cardTableComponent;
    }

    public JLabel getErrorMessage() {
        return errorMessage;
    }
}
