package com.lavida.swing;

import com.lavida.service.entity.UserJdo;

import javax.swing.*;
import java.awt.*;

/**
 * MainApplicationWindow
 * Created: 20:09 03.08.13
 *
 * @author Ruslan
 */
public class MainApplicationWindow extends JFrame {
    private static final String WINDOW_NAME = "La Vida";
    private static final String CLEAR_BUTTON_NAME_RU = "Сброс";
    private static final String SEARCH_BY_NAME_RU = "Наименование:";
    private static final String SEARCH_BY_CODE_RU = "Код:";
    private static final String SEARCH_BY_PRICE_RU = "Цена:";
    private static final String SEARCH_PANEL_NAME_RU = "Поиск";

    JMenuBar menuBar;
    JDesktopPane desktopPane;
    JPanel mainPanel, buttonPanel,  searchPanel, refreshPanel;
    JLabel searchByNameLabel, searchByCodeLabel, searchByPriceLabel;
    JTextField searchByNameField, searchByCodeField, searchByPriceField;
    JButton clearNameButton, clearCodeButton, clearPriceButton;

    UserJdo currentUser;

    public MainApplicationWindow() {
        super(WINDOW_NAME);
        setResizable(true);
        setBounds(100, 100, 500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        menuBar = new JMenuBar();
        menuBar.setBackground(Color.lightGray);
        menuBar.setPreferredSize(new Dimension(500, 25));

        setJMenuBar(menuBar);

        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.white);
        desktopPane.setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setBackground(Color.white);

//        loginForm = new LoginForm();
//        loginForm.setVisible(true);
//        mainPanel.add(loginForm);
//        currentUser = loginForm.getCurrentUser();

        desktopPane.add(mainPanel, BorderLayout.CENTER);

        searchPanel = new JPanel();
        searchPanel.setBackground(Color.lightGray);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(SEARCH_PANEL_NAME_RU),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchPanel.setOpaque(true);
        searchPanel.setAutoscrolls(true);
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;


        searchByNameLabel = new JLabel(SEARCH_BY_NAME_RU);
        constraints.gridx = 0;
        constraints.gridy = 0;
        searchPanel.add(searchByNameLabel, constraints);

        searchByNameField = new JTextField(25);
        constraints.gridx = 1;
        constraints.gridy = 0;
        searchPanel.add(searchByNameField, constraints);

        clearNameButton = new JButton(CLEAR_BUTTON_NAME_RU);
        constraints.gridx = 2;
        constraints.gridy = 0;
        searchPanel.add(clearNameButton, constraints);

        searchByCodeLabel = new JLabel(SEARCH_BY_CODE_RU);
        constraints.gridx = 0;
        constraints.gridy = 1;
        searchPanel.add(searchByCodeLabel, constraints);

        searchByCodeField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        searchPanel.add(searchByCodeField, constraints);

        clearCodeButton = new JButton(CLEAR_BUTTON_NAME_RU);
        constraints.gridx = 2;
        constraints.gridy = 1;
        searchPanel.add(clearCodeButton, constraints);

        searchByPriceLabel = new JLabel(SEARCH_BY_PRICE_RU);
        searchByPriceField = new JTextField();
        clearPriceButton = new JButton(CLEAR_BUTTON_NAME_RU);

        desktopPane.add(searchPanel, BorderLayout.SOUTH);
        getContentPane().add(desktopPane);
        setVisible(true);


    }

    public UserJdo getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserJdo currentUser) {
        this.currentUser = currentUser;
    }

    public static void main(String[] args) {
        MainApplicationWindow mainApplicationWindow = new MainApplicationWindow();
    }
}
