package com.lavida.swing.form;

import com.lavida.swing.dialog.SoldProductsDialog;
import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.handler.MainFormHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * MainForm
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
@Component
public class MainForm extends AbstractForm {

    @Resource
    private MainFormHandler handler;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private SoldProductsDialog soldProductsDialog;

    private JMenuBar jMenuBar;
    private JSplitPane mainPane, southPane;
    private JPanel operationPanel, refreshPanel, westPanel, desktopPanel;
    private JButton refreshButton, recommitButton, sellButton, showSoldProductsButton;
    private JPanel statusBarPanel, postponedPanel;
    private JLabel postponedOperations, postponedMessage;

    private ArticleTableComponent articleTableComponent = new ArticleTableComponent();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setTitle(messageSource.getMessage("mainForm.form.title", null, localeHolder.getLocale()));
        form.setResizable(true);
        form.setBounds(100, 100, 800, 500);
        form.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());
//      menu bar
        jMenuBar = new JMenuBar();
        jMenuBar.setBackground(Color.lightGray);
        jMenuBar.setPreferredSize(new Dimension(500, 25));
        form.setJMenuBar(jMenuBar);

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setBackground(Color.white);
        desktopPanel.setLayout(new BorderLayout());

        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);

//      main panel for table of goods
        JPanel main = articleTableComponent.getMainPanel();
//      analyze panel for total analyses
        JPanel analyze = articleTableComponent.getArticleAnalyzeComponent().getAnalyzePanel();
//      panel for search operations
        JPanel search = articleTableComponent.getArticleFiltersComponent().getFiltersPanel();

        southPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, analyze, search);
        mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, main, southPane);
        desktopPanel.add(mainPane, BorderLayout.CENTER);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        west panel for buttons
//      panel for refresh and save operations with data
        refreshPanel = new JPanel();
        refreshPanel.setBackground(Color.lightGray);
        refreshPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.refresh.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(25, 5, 25, 5)));
        refreshPanel.setOpaque(true);
        refreshPanel.setAutoscrolls(true);
        refreshPanel.setLayout(new GridBagLayout());

        refreshButton = new JButton(messageSource.getMessage("mainForm.button.refresh.title", null,
                localeHolder.getLocale()));
        refreshButton.setMnemonic(KeyEvent.VK_T);  // Alt+T hot keys
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.refreshButtonClicked();
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        refreshPanel.add(refreshButton, constraints);

        recommitButton = new JButton(messageSource.getMessage("mainForm.button.recommit.title", null, localeHolder.getLocale()));
        recommitButton.setMnemonic(KeyEvent.VK_K); // Alt + K hot keys
        recommitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.recommitButtonClicked();
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        refreshPanel.add(recommitButton, constraints);

//        operation panel for selling and returning goods.
        operationPanel = new JPanel();
        operationPanel.setBackground(Color.lightGray);
        operationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("mainForm.panel.operation.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(15, 5, 15, 5)));
        operationPanel.setOpaque(true);
        operationPanel.setAutoscrolls(true);
        operationPanel.setLayout(new GridBagLayout());

        sellButton = new JButton(messageSource.getMessage("mainForm.button.sell.title", null, localeHolder.getLocale()));
        sellButton.setMnemonic(KeyEvent.VK_S); // Alt + S hot keys
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sellButtonClicked();
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        operationPanel.add(sellButton, constraints);

        showSoldProductsButton = new JButton(messageSource.getMessage("mainForm.button.show.sold.products.title", null, localeHolder.getLocale()));
        showSoldProductsButton.setMnemonic(KeyEvent.VK_X); // Alt + X hot keys
        showSoldProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.showSoldProductsButtonClicked();
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        operationPanel.add(showSoldProductsButton, constraints);

        westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());
        westPanel.setBorder(BorderFactory.createEmptyBorder(25, 5, 25, 5));
        westPanel.setBackground(Color.lightGray);
        constraints.gridx = 0;
        constraints.gridy = 0;
        westPanel.add(refreshPanel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        westPanel.add(operationPanel, constraints);
        rootContainer.add(westPanel, BorderLayout.WEST);


//        Status bar panel
        statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new FlowLayout());
        statusBarPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.DARK_GRAY));
        postponedPanel = new JPanel();
        postponedPanel.setLayout(new FlowLayout());
        postponedOperations = new JLabel();
        postponedOperations.setText(messageSource.getMessage(
                "mainForm.panel.statusBar.postponed.operations.label.title", null, localeHolder.getLocale()));
        postponedOperations.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handler.showPostponedOperationsMessage();
            }
        });

        postponedMessage = new JLabel();
        postponedPanel.add(postponedOperations);
        postponedPanel.add(postponedMessage);
        statusBarPanel.add(postponedPanel);

        rootContainer.add(statusBarPanel, BorderLayout.SOUTH);
    }

    /**
     * Filters the JTable by permissions of roles (ROLE_SELLER). It removes certain columns.
     *
     * @param userRoles
     */
    public void filterTableByRoles(List<String> userRoles) {
        articleTableComponent.filterTableByRoles(userRoles);
    }

    public JLabel getPostponedMessage() {
        return postponedMessage;
    }

    public MainFormHandler getHandler() {
        return handler;
    }

    public SoldProductsDialog getSoldProductsDialog() {
        return soldProductsDialog;
    }
}

