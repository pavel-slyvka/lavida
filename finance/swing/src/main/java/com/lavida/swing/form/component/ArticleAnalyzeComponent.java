package com.lavida.swing.form.component;

import com.lavida.service.UserService;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * The component for analyze panel
 * Created: 13:30 20.08.13
 *
 * @author Ruslan
 */
public class ArticleAnalyzeComponent {

    private JPanel analyzePanel;
    private JLabel  totalCountField, totalCostEURLabel, totalCostEURField,
            totalPriceLabel, totalPriceField, totalPurchaseCostEURLabel, totalPurchaseCostEURField,
            totalCostUAHLabel, totalCostUAHField, minimalMultiplierLabel, minimalMultiplierField,
            normalMultiplierLabel, normalMultiplierField, totalTransportCostEURLabel, totalTransportCostEURField,
            profitUAHLabel, profitUAHField;

    public void initializeComponents(ArticlesTableModel tableModel, MessageSource messageSource, LocaleHolder localeHolder) {
//        panel for analyzing total cost, price, count of products etc., shown in the table
        Border fieldsBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        analyzePanel = new JPanel(new GridBagLayout());
        analyzePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("component.article.analyze.panel.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 5, 2, 5);

        JLabel totalCountLabel = new JLabel();
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
        totalPurchaseCostEURField.setText(roundTwoDecimalsString(tableModel.getTotalPurchaseCostEUR()));
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
        totalCostEURField.setText(roundTwoDecimalsString(tableModel.getTotalCostEUR()));
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
        totalCostUAHField.setText(roundTwoDecimalsString(tableModel.getTotalCostUAH()));
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

        minimalMultiplierField = new JLabel();
        minimalMultiplierField.setBorder(fieldsBorder);
        minimalMultiplierField.setText(roundTwoDecimalsString(tableModel.getMinimalMultiplier()));

        totalTransportCostEURLabel = new JLabel();
        totalTransportCostEURLabel.setBorder(BorderFactory.createEmptyBorder());
        totalTransportCostEURLabel.setText(messageSource.getMessage("component.article.analyze.label.total.cost.transport.EUR",
                null, localeHolder.getLocale()));
        totalTransportCostEURLabel.setLabelFor(totalTransportCostEURField);

        totalTransportCostEURField = new JLabel();
        totalTransportCostEURField.setBorder(fieldsBorder);
        totalTransportCostEURField.setText(roundTwoDecimalsString(tableModel.getTotalTransportCostEUR()));

        if (tableModel.getQueryName() != null) {
            constraints.fill = GridBagConstraints.NONE;
            constraints.gridwidth = GridBagConstraints.RELATIVE;
            constraints.anchor = GridBagConstraints.EAST;
            constraints.weightx = 0.0;
            analyzePanel.add(minimalMultiplierLabel, constraints);

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.anchor = GridBagConstraints.EAST;
            constraints.weightx = 1.0;
            analyzePanel.add(minimalMultiplierField, constraints);
        } else {
            constraints.fill = GridBagConstraints.NONE;
            constraints.gridwidth = GridBagConstraints.RELATIVE;
            constraints.anchor = GridBagConstraints.EAST;
            constraints.weightx = 0.0;
            analyzePanel.add(totalTransportCostEURLabel, constraints);

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.anchor = GridBagConstraints.EAST;
            constraints.weightx = 1.0;
            analyzePanel.add(totalTransportCostEURField, constraints);

        }

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
        normalMultiplierField.setText(roundTwoDecimalsString(tableModel.getNormalMultiplier()));
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
        totalPriceField.setText(roundTwoDecimalsString(tableModel.getTotalPriceUAH()));
        totalPriceField.setBorder(fieldsBorder);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        analyzePanel.add(totalPriceField, constraints);

        profitUAHLabel = new JLabel();
        profitUAHLabel.setBorder(BorderFactory.createEmptyBorder());
        profitUAHLabel.setText(messageSource.getMessage("component.article.analyze.label.profit.UAH.title",
                null, localeHolder.getLocale()));
        profitUAHLabel.setLabelFor(profitUAHField);

        profitUAHField = new JLabel();
        profitUAHField.setText(roundTwoDecimalsString(tableModel.getProfitUAH()));
        profitUAHField.setBorder(fieldsBorder);

        if (tableModel.getQueryName() != null) {
            constraints.fill = GridBagConstraints.NONE;
            constraints.gridwidth = GridBagConstraints.RELATIVE;
            constraints.anchor = GridBagConstraints.EAST;
            constraints.weightx = 0.0;
            analyzePanel.add(profitUAHLabel, constraints);

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.anchor = GridBagConstraints.EAST;
            constraints.weightx = 1.0;
            analyzePanel.add(profitUAHField, constraints);
        }
    }

    /**
     * Update fields ( totalCountField, totalCostEURField, totalPriceField) with inputted values.
     *
     * @param totalCount   int , total count of articles shown in the totalCountField;
     * @param totalCostEUR double, total original cost of articles shown in the totalCostEURField;
     * @param totalPrice   double, total price of articles shown in the totalPriceField.
     * @param profitUAH double profit UAH
     */
    public void updateFields(int totalCount, double totalPurchaseCostEUR, double totalCostEUR, double totalCostUAH,
                             double minimalMultiplier, double normalMultiplier, double totalPrice, double totalTransportCostEUR, double profitUAH) {
        totalCountField.setText(String.valueOf(totalCount));
        totalPurchaseCostEURField.setText(roundTwoDecimalsString(totalPurchaseCostEUR));
        totalCostEURField.setText(roundTwoDecimalsString(totalCostEUR));
        totalCostUAHField.setText(roundTwoDecimalsString(totalCostUAH));
        minimalMultiplierField.setText(roundTwoDecimalsString(minimalMultiplier));
        normalMultiplierField.setText(roundTwoDecimalsString(normalMultiplier));
        totalPriceField.setText(roundTwoDecimalsString(totalPrice));
        totalTransportCostEURField.setText(roundTwoDecimalsString(totalTransportCostEUR));
        profitUAHField.setText(roundTwoDecimalsString(profitUAH));
    }

    /**
     * Formats double number to rounded String expression.
     *
     * @param number double to be formatted.
     * @return the rounded String expression.
     */
    private String roundTwoDecimalsString(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(number);
    }

    public void filterAnalyzeComponentByRoles(UserService userService) {
        if (userService.hasForbiddenRole()) {
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
            totalTransportCostEURField.setVisible(false);
            totalTransportCostEURLabel.setVisible(false);
            profitUAHLabel.setVisible(false);
            profitUAHField.setVisible(false);
        }

    }

    public JPanel getAnalyzePanel() {
        return analyzePanel;
    }


    public JLabel getTotalPurchaseCostEURField() {
        return totalPurchaseCostEURField;
    }

}
