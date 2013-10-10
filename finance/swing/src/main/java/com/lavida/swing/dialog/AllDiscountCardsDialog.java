package com.lavida.swing.dialog;

import com.lavida.swing.dialog.settings.AllDiscountCardsTableViewSettingsDialog;
import com.lavida.swing.form.component.DiscountCardTableComponent;
import com.lavida.swing.handler.AllDiscountCardsDialogHandler;
import com.lavida.swing.service.DiscountCardsTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The dialog for viewing, searching, editing all discount cards.
 * Created: 11:23 06.09.13
 *
 * @author Ruslan
 */
@Component
public class AllDiscountCardsDialog extends AbstractDialog {

    @Resource
    private AllDiscountCardsDialogHandler handler;

    @Resource(name = "allDiscountCardsTableModel")
    private DiscountCardsTableModel tableModel;

    @Resource
    private AllDiscountCardsTableViewSettingsDialog allDiscountCardsTableViewSettingsDialog;

    private JPanel operationPanel, southPanel, desktopPanel, filtersPanel, analyzePanel, mainPanel,
            buttonPanel;
    private JButton activateButton, disableButton, cancelButton;
    private DiscountCardTableComponent cardTableComponent = new DiscountCardTableComponent();
    private JLabel errorMessage;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem printItem, discountCardsTableViewItem;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.discounts.card.all.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 900, 700);
        dialog.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

        initializeMenuBar();
        dialog.setJMenuBar(menuBar);

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder());

        cardTableComponent.initializeComponents(tableModel, messageSource, localeHolder, usersSettingsHolder);


//      panel for search operations

//      main panel for table of goods
        mainPanel = cardTableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(1,5,0,5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);


//        south panel for desktopPanel
        southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);
        southPanel.setBorder(BorderFactory.createEmptyBorder());

//      analyze panel for total analyses
        analyzePanel = cardTableComponent.getCardFiltersComponent().getCardAnalyzeComponent().getAnalyzePanel();
        analyzePanel.setPreferredSize(new Dimension(890, 25));
        analyzePanel.setMinimumSize(new Dimension(800, 25));
        analyzePanel.setMaximumSize(new Dimension(1500, 25));

//      panel for search operations
        filtersPanel= cardTableComponent.getCardFiltersComponent().getFiltersPanel();

        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.PAGE_AXIS));
        operationPanel.setPreferredSize(new Dimension(170, 85));
        operationPanel.setMaximumSize(new Dimension(170, 85));
        operationPanel.setMinimumSize(new Dimension(170, 85));
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));

        activateButton = new JButton();
        activateButton.setHorizontalTextPosition(JButton.CENTER);
        activateButton.setPreferredSize(new Dimension(150, 25));
        activateButton.setMaximumSize(new Dimension(150, 25));
        activateButton.setMinimumSize(new Dimension(150, 25));
        activateButton.setText(messageSource.getMessage("dialog.discounts.card.all.button.activate", null, localeHolder.getLocale()));
        activateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.activateButtonClicked();
            }
        });

        disableButton = new JButton();
        disableButton.setText(messageSource.getMessage("dialog.discounts.card.all.button.disable", null, localeHolder.getLocale()));
        disableButton.setHorizontalTextPosition(JButton.CENTER);
        disableButton.setPreferredSize(new Dimension(150, 25));
        disableButton.setMaximumSize(new Dimension(150, 25));
        disableButton.setMinimumSize(new Dimension(150, 25));
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.disableButtonClicked();
            }
        });
        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(activateButton);
        operationPanel.add(Box.createVerticalGlue());
        operationPanel.add(disableButton);
        operationPanel.add(Box.createVerticalGlue());

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

        constraints.gridx = 1;
        constraints.gridy = 1;
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

        buttons.add(cancelButton);
        buttonPanel.add(cancelButton);

        rootContainer.add(buttonPanel, BorderLayout.SOUTH);

    }

    private void initializeMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu();
        fileMenu.setText(messageSource.getMessage("mainForm.menu.table", null, localeHolder.getLocale()));

        printItem = new JMenuItem();
        printItem.setText(messageSource.getMessage("mainForm.menu.table.print", null, localeHolder.getLocale()));
        printItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.printItemClicked();
            }
        });

        fileMenu.add(printItem);

        JMenu settingsMenu = new JMenu();
        settingsMenu.setText(messageSource.getMessage("mainForm.menu.settings.title", null, localeHolder.getLocale()));

        discountCardsTableViewItem = new JMenuItem();
        discountCardsTableViewItem.setText(messageSource.getMessage("mainForm.menu.settings.item.view.tables", null, localeHolder.getLocale()));
        discountCardsTableViewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allDiscountCardsTableViewSettingsDialog.show();
            }
        });
        settingsMenu.add(discountCardsTableViewItem);

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);


    }

    public DiscountCardsTableModel getTableModel() {
        return tableModel;
    }

    public JLabel getErrorMessage() {
        return errorMessage;
    }

    public DiscountCardTableComponent getCardTableComponent() {
        return cardTableComponent;
    }

    public JMenuItem getPrintItem() {
        return printItem;
    }
}
