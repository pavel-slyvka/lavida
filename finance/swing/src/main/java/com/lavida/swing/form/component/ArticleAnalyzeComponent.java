package com.lavida.swing.form.component;

import com.lavida.service.entity.ArticleJdo;
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
 * Created: 13:30 20.08.13
 *
 * @author Ruslan
 */
public class ArticleAnalyzeComponent {
    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();
    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    private ArticlesTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel analyzePanel, totalCountPanel, totalOriginalCostPanel, totalPricePanel;
    private JLabel totalCountLabel, totalCountField, totalOriginalCostLabel, totalOriginalCostField,
            totalPriceLabel, totalPriceField;

    public void initializeComponents(ArticlesTableModel articlesTableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = articlesTableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;


//        panel for analyzing total cost, price, count of products , shown in the table
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEADING);
        Border fieldsBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        analyzePanel = new JPanel(flowLayout);

        totalCountPanel = new JPanel(flowLayout);
        totalCountLabel = new JLabel();
        totalCountLabel.setText(messageSource.getMessage("component.article.analyze.label.total.count.title",
                null, localeHolder.getLocale()));
        totalCountPanel.add(totalCountLabel);
        totalCountField = new JLabel();
        totalCountField.setText(String.valueOf(tableModel.getTotalCountArticles()));
        totalCountField.setBorder(fieldsBorder);
        totalCountPanel.add(totalCountField);
        analyzePanel.add(totalCountPanel);

        totalOriginalCostPanel = new JPanel(flowLayout);
        totalOriginalCostLabel = new JLabel(messageSource.getMessage("component.article.analyze.label.total.cost.original.title",
                null, localeHolder.getLocale()));
        totalOriginalCostLabel.setText(messageSource.getMessage("component.article.analyze.label.total.cost.original.title",
                null, localeHolder.getLocale()));
        totalOriginalCostPanel.add(totalOriginalCostLabel);
        totalOriginalCostField = new JLabel();
        totalOriginalCostField.setText(roundTwoDecimals(tableModel.getTotalOriginalCostEUR()));
        totalOriginalCostField.setBorder(fieldsBorder);
        totalOriginalCostPanel.add(totalOriginalCostField);
        analyzePanel.add(totalOriginalCostPanel);

        totalPricePanel = new JPanel(flowLayout);
        totalPriceLabel = new JLabel();
        totalPriceLabel.setText(messageSource.getMessage("component.article.analyze.label.total.price.title",
                null, localeHolder.getLocale()));
        totalPricePanel.add(totalPriceLabel);
        totalPriceField = new JLabel();
        totalPriceField.setText(roundTwoDecimals(tableModel.getTotalPriceUAH()));
        totalPriceField.setBorder(fieldsBorder);
        totalPricePanel.add(totalPriceField);
        analyzePanel.add(totalPricePanel);
    }

    /**
     * Update fields ( totalCountField, totalOriginalCostField, totalPriceField) with inputted values.
     * @param totalCount int , total count of articles shown in the totalCountField;
     * @param totalCost  double, total original cost of articles shown in the totalOriginalCostField;
     * @param totalPrice double, total price of articles shown in the totalPriceField.
     */
    public void updateFields (int totalCount, double totalCost, double totalPrice) {
        totalCountField.setText(String.valueOf(totalCount));
        totalOriginalCostField.setText(roundTwoDecimals(totalCost));
        totalPriceField.setText(roundTwoDecimals(totalPrice));
    }

    /**
     * Formats double number to rounded String expression.
     * @param number double to be formatted.
     * @return
     */
    private String roundTwoDecimals (double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(number);
    }

    public void filterAnalyzeComponentByRoles(java.util.List<String> userRoles) {
         if (hasForbiddenRole(userRoles)) {
             totalOriginalCostPanel.setVisible(false);
             totalPricePanel.setVisible(false);
         }

    }

    private boolean hasForbiddenRole (java.util.List<String> userRoles) {
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
