package com.lavida.swing.form;

import com.lavida.swing.form.component.ArticleTableComponent;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import com.lavida.swing.handler.MainFormHandler;

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
@org.springframework.stereotype.Component
public class MainForm extends AbstractForm {

    @Resource
    private MainFormHandler handler;

    private ArticlesTableModel tableModel;

    private JMenuBar jMenuBar;
    private JDesktopPane desktopPane;
    private JPanel operationPanel, refreshPanel, westPanel;
    private JButton refreshButton, recommitButton, sellButton, returnButton;
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
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.white);
        desktopPane.setLayout(new BorderLayout());

//      main panel for table of goods
        tableModel = new ArticlesTableModel();
        tableModel.initHeaderFieldAndTitles(messageSource, localeHolder.getLocale());
        handler.initTableModelWithData(tableModel);

        articleTableComponent.initializeComponents(tableModel, messageSource, localeHolder);
        desktopPane.add(articleTableComponent.getMainPanel(), BorderLayout.CENTER);

//      panel for search operations
        desktopPane.add(articleTableComponent.getArticleFiltersComponent().getSearchPanel(), BorderLayout.SOUTH);

        rootContainer.add(desktopPane, BorderLayout.CENTER);

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
                handler.refreshButtonClicked(tableModel);
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

        returnButton = new JButton(messageSource.getMessage("mainForm.button.return.title", null, localeHolder.getLocale()));
        returnButton.setMnemonic(KeyEvent.VK_R); // Alt + R hot keys
        constraints.gridx = 0;
        constraints.gridy = 1;
        operationPanel.add(returnButton, constraints);

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

    public ArticlesTableModel getTableModel() {
        return tableModel;
    }

    public JLabel getPostponedMessage() {
        return postponedMessage;
    }

    public MainFormHandler getHandler() {
        return handler;
    }
}

