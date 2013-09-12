package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * The component for analyze panel
 * Created: 13:30 20.08.13
 *
 * @author Ruslan
 */
public class ArticleAnalyzeComponent {
    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();

    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER_LA_VIDA");
        FORBIDDEN_ROLES.add("ROLE_SELLER_SLAVYANKA");
        FORBIDDEN_ROLES.add("ROLE_SELLER_NOVOMOSKOVSK");
    }

    private ArticlesTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel analyzePanel, totalCountPanel, totalOriginalCostPanel, totalPricePanel;
    private JLabel totalCountLabel, totalCountField, totalCostEURLabel, totalCostEURField,
            totalPriceLabel, totalPriceField, totalPurchaseCostEURLabel, totalPurchaseCostEURField,
            totalCostUAHLabel, totalCostUAHField, minimalMultiplierLabel, minimalMultiplierField,
            normalMultiplierLabel, normalMultiplierField;

    public void initializeComponents(ArticlesTableModel articlesTableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = articlesTableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;


//        panel for analyzing total cost, price, count of products etc., shown in the table
        Border fieldsBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        analyzePanel = new JPanel(new GridBagLayout());
        analyzePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("component.article.analyze.panel.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 5, 2, 5);

        totalCountLabel = new JLabel();
        totalCountLabel.setBorder(BorderFactory.createEmptyBorder());
        totalCountLabel.setText(messageSource.getMessage("component.article.analyze.label.total.count.title",
                null, localeHolder.getLocale()));
        totalCountLabel.setLabelFor(totalCountField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        analyzePanel.add(totalCountLabel, constraints);

        totalCountField = new JLabel();
        totalCountField.setBorder(fieldsBorder);
        totalCountField.setText(String.valueOf(tableModel.getTotalCountArticles()));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(totalCountField, constraints);

        totalPurchaseCostEURLabel = new JLabel();
        totalPurchaseCostEURLabel.setText(messageSource.getMessage("component.article.analyze.label.total.cost.purchase.EUR",
                null, localeHolder.getLocale()));
        totalPurchaseCostEURLabel.setBorder(BorderFactory.createEmptyBorder());
        totalPurchaseCostEURLabel.setLabelFor(totalPurchaseCostEURField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        analyzePanel.add(totalPurchaseCostEURLabel, constraints);

        totalPurchaseCostEURField = new JLabel();
        totalPurchaseCostEURField.setBorder(fieldsBorder);
        totalPurchaseCostEURField.setText(roundTwoDecimals(tableModel.getTotalPurchaseCostEUR()));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(totalPurchaseCostEURField, constraints);

        totalCostEURLabel = new JLabel(messageSource.getMessage("component.article.analyze.label.total.cost.EUR",
                null, localeHolder.getLocale()));
        totalCostEURLabel.setBorder(BorderFactory.createEmptyBorder());
        totalCostEURLabel.setText(messageSource.getMessage("component.article.analyze.label.total.cost.EUR",
                null, localeHolder.getLocale()));
        totalCostEURLabel.setLabelFor(totalCostEURField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        analyzePanel.add(totalCostEURLabel, constraints);

        totalCostEURField = new JLabel();
        totalCostEURField.setBorder(fieldsBorder);
        totalCostEURField.setText(roundTwoDecimals(tableModel.getTotalCostEUR()));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(totalCostEURField, constraints);

        totalCostUAHLabel = new JLabel();
        totalCostUAHLabel.setBorder(BorderFactory.createEmptyBorder());
        totalCostUAHLabel.setText(messageSource.getMessage("component.article.analyze.label.total.cost.UAH",
                null, localeHolder.getLocale()));
        totalCostUAHLabel.setLabelFor(totalCostUAHField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        analyzePanel.add(totalCostUAHLabel, constraints);

        totalCostUAHField = new JLabel();
        totalCostUAHField.setBorder(fieldsBorder);
        totalCostUAHField.setText(roundTwoDecimals(tableModel.getTotalCostUAH()));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(totalCostUAHField, constraints);

        minimalMultiplierLabel = new JLabel();
        minimalMultiplierLabel.setBorder(BorderFactory.createEmptyBorder());
        minimalMultiplierLabel.setText(messageSource.getMessage("component.article.analyze.label.multiplier.minimal",
                 null, localeHolder.getLocale()));
        minimalMultiplierLabel.setLabelFor(minimalMultiplierField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        analyzePanel.add(minimalMultiplierLabel, constraints);

        minimalMultiplierField = new JLabel();
        minimalMultiplierField.setBorder(fieldsBorder);
        minimalMultiplierField.setText(roundTwoDecimals(tableModel.getMinimalMultiplier()));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(minimalMultiplierField, constraints);

        normalMultiplierLabel = new JLabel();
        normalMultiplierLabel.setBorder(BorderFactory.createEmptyBorder());
        normalMultiplierLabel.setText(messageSource.getMessage("component.article.analyze.label.multiplier.normal",
                null, localeHolder.getLocale()));
        normalMultiplierLabel.setLabelFor(normalMultiplierField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        analyzePanel.add(normalMultiplierLabel, constraints);

        normalMultiplierField = new JLabel();
        normalMultiplierField.setBorder(fieldsBorder);
        normalMultiplierField.setText(roundTwoDecimals(tableModel.getNormalMultiplier()));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(normalMultiplierField, constraints);

        totalPriceLabel = new JLabel();
        totalPriceLabel.setBorder(BorderFactory.createEmptyBorder());
        totalPriceLabel.setText(messageSource.getMessage("component.article.analyze.label.total.price.title",
                null, localeHolder.getLocale()));
        totalPriceLabel.setLabelFor(totalPriceField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        analyzePanel.add(totalPriceLabel, constraints);

        totalPriceField = new JLabel();
        totalPriceField.setText(roundTwoDecimals(tableModel.getTotalPriceUAH()));
        totalPriceField.setBorder(fieldsBorder);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(totalPriceField, constraints);

    }

    /**
     * Update fields ( totalCountField, totalCostEURField, totalPriceField) with inputted values.
     *
     * @param totalCount int , total count of articles shown in the totalCountField;
     * @param totalCostEUR  double, total original cost of articles shown in the totalCostEURField;
     * @param totalPrice double, total price of articles shown in the totalPriceField.
     */
    public void updateFields(int totalCount, double totalPurchaseCostEUR, double totalCostEUR, double totalCostUAH,
                             double minimalMultiplier, double normalMultiplier, double totalPrice) {
        totalCountField.setText(String.valueOf(totalCount));
        totalPurchaseCostEURField.setText(roundTwoDecimals(totalPurchaseCostEUR));
        totalCostEURField.setText(roundTwoDecimals(totalCostEUR));
        totalCostUAHField.setText(roundTwoDecimals(totalCostUAH));
        minimalMultiplierField.setText(roundTwoDecimals(minimalMultiplier));
        normalMultiplierField.setText(roundTwoDecimals(normalMultiplier));
        totalPriceField.setText(roundTwoDecimals(totalPrice));
    }

    /**
     * Formats double number to rounded String expression.
     *
     * @param number double to be formatted.
     * @return the rounded String expression.
     */
    private String roundTwoDecimals(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(number);
    }

    public void filterAnalyzeComponentByRoles(java.util.List<String> userRoles) {
        if (hasForbiddenRole(userRoles)) {
            totalCostEURLabel.setVisible(false);
            totalCostEURField.setVisible(false);
            totalPriceLabel.setVisible(false);
            totalPriceField.setVisible(false);
            totalPurchaseCostEURLabel.setVisible(false);
            totalPurchaseCostEURField.setVisible(false);
            totalCostUAHLabel.setVisible(false);
            totalCostUAHField.setVisible(false);
            minimalMultiplierLabel.setVisible(false);
            minimalMultiplierField.setVisible(false);
            normalMultiplierLabel.setVisible(false);
            normalMultiplierField.setVisible(false);

        }

    }

    private boolean hasForbiddenRole(java.util.List<String> userRoles) {
        for (String role : userRoles) {
            if (FORBIDDEN_ROLES.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public JPanel getAnalyzePanel() {
        return analyzePanel;
    }

}
