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

    private JPanel operationPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel,
            buttonPanel;
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
        southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder());

//      analyze panel for total analyses
        analyzePanel = cardTableComponent.getCardFiltersComponent().getCardAnalyzeComponent().getAnalyzePanel();
        analyzePanel.setPreferredSize(new Dimension(890, 25));
        analyzePanel.setMinimumSize(new Dimension(800, 25));
        analyzePanel.setMaximumSize(new Dimension(1500, 25));

//      panel for search operations
//        filtersPanel= cardTableComponent.getCardFiltersComponent().getFiltersPanel();

        operationPanel = new JPanel();
        operationPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder()));
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


        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.weightx = 1.0;
        operationPanel.add(addRowButton, constraints);

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
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
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

//        constraints.gridx = 1;
//        constraints.gridy = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        constraints.gridwidth = GridBagConstraints.REMAINDER;
//        constraints.gridheight = GridBagConstraints.REMAINDER;
//        constraints.anchor = GridBagConstraints.WEST;
//        constraints.weightx = 1.0;
//        constraints.weighty = 1.0;
//        southPanel.add(filtersPanel, constraints);

        desktopPanel.add(southPanel, BorderLayout.SOUTH);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        south panel for buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

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
        buttonPanel.add(acceptCardsButton);

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

    public DiscountCardsTableModel getTableModel() {
        return tableModel;
    }

    public DiscountCardTableComponent getCardTableComponent() {
        return cardTableComponent;
    }
}
