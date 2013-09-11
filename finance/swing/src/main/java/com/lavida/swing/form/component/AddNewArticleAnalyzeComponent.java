package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.AddNewArticleTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 19:01 03.09.13
 *
 * @author Ruslan
 */
@Deprecated
public class AddNewArticleAnalyzeComponent {
    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();
    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER");
    }

    private AddNewArticleTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel analyzePanel, totalCountPanel, totalOriginalCostPanel, totalPricePanel;
    private JLabel totalCountLabel, totalCountField, totalOriginalCostLabel, totalOriginalCostField,
            totalPriceLabel, totalPriceField;

    public void initializeComponents(AddNewArticleTableModel articlesTableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = articlesTableModel;
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;


//        panel for analyzing total cost, price, count of products , shown in the table
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEADING);
        Border fieldsBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        analyzePanel = new JPanel(flowLayout);
        analyzePanel.setBorder(BorderFactory.createEmptyBorder());

        totalCountPanel = new JPanel(flowLayout);
        totalCountPanel.setPreferredSize(new Dimension(200, 25));
        totalCountPanel.setMinimumSize(new Dimension(200, 25));
        totalCountPanel.setMaximumSize(new Dimension(300, 25));
        totalCountPanel.setBorder(BorderFactory.createEmptyBorder());
        totalCountLabel = new JLabel();
        totalCountLabel.setBorder(BorderFactory.createEmptyBorder(-2,0,0,0));
        totalCountLabel.setText(messageSource.getMessage("component.article.analyze.label.total.count.title",
                null, localeHolder.getLocale()));
        totalCountLabel.setLabelFor(totalCountField);
        totalCountPanel.add(totalCountLabel);
        totalCountField = new JLabel();
        totalCountField.setBorder(BorderFactory.createCompoundBorder(fieldsBorder,BorderFactory.createEmptyBorder(-2,0,0,0)));
        totalCountField.setText(String.valueOf(tableModel.getTotalCountArticles()));
        totalCountPanel.add(totalCountField);
        analyzePanel.add(totalCountPanel);

        totalOriginalCostPanel = new JPanel(flowLayout);
        totalOriginalCostPanel.setPreferredSize(new Dimension(250, 25));
        totalOriginalCostPanel.setMinimumSize(new Dimension(200, 25));
        totalOriginalCostPanel.setMaximumSize(new Dimension(300, 25));
        totalOriginalCostPanel.setBorder(BorderFactory.createEmptyBorder());
        totalOriginalCostLabel = new JLabel(messageSource.getMessage("component.article.analyze.label.total.cost.EUR",
                null, localeHolder.getLocale()));
        totalOriginalCostLabel.setBorder(BorderFactory.createEmptyBorder(-2,0,0,0));
        totalOriginalCostLabel.setText(messageSource.getMessage("component.article.analyze.label.total.cost.EUR",
                null, localeHolder.getLocale()));
        totalOriginalCostLabel.setLabelFor(totalOriginalCostField);
        totalOriginalCostPanel.add(totalOriginalCostLabel);
        totalOriginalCostField = new JLabel();
        totalOriginalCostField.setBorder(BorderFactory.createCompoundBorder(fieldsBorder,BorderFactory.createEmptyBorder(-2,0,0,0)));
        totalOriginalCostField.setText(roundTwoDecimals(tableModel.getTotalOriginalCostEUR()));
        totalOriginalCostPanel.add(totalOriginalCostField);
        analyzePanel.add(totalOriginalCostPanel);

        totalPricePanel = new JPanel(flowLayout);
        totalPricePanel.setPreferredSize(new Dimension(250, 25));
        totalPricePanel.setMinimumSize(new Dimension(200, 25));
        totalPricePanel.setMaximumSize(new Dimension(300, 25));
        totalPricePanel.setBorder(BorderFactory.createEmptyBorder());
        totalPriceLabel = new JLabel();
        totalPriceLabel.setBorder(BorderFactory.createEmptyBorder(-2,0,0,0));
        totalPriceLabel.setText(messageSource.getMessage("component.article.analyze.label.total.price.title",
                null, localeHolder.getLocale()));
        totalPriceLabel.setLabelFor(totalPriceField);
        totalPricePanel.add(totalPriceLabel);
        totalPriceField = new JLabel();
        totalPriceField.setText(roundTwoDecimals(tableModel.getTotalPriceUAH()));
        totalPriceField.setBorder(BorderFactory.createCompoundBorder(fieldsBorder,BorderFactory.createEmptyBorder(-2,0,0,0)));
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
