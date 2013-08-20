package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created: 13:30 20.08.13
 *
 * @author Ruslan
 */
public class ArticleAnalyzeComponent {
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
        flowLayout.setAlignment(FlowLayout.TRAILING);
        Border fieldsBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        analyzePanel = new JPanel(flowLayout);

        totalCountPanel = new JPanel(flowLayout);
        totalCountLabel = new JLabel();
        totalCountLabel.setText(messageSource.getMessage("component.article.analyze.label.total.count.title",
                null, localeHolder.getLocale()));
        totalCountPanel.add(totalCountLabel);
        totalCountField = new JLabel();
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
        totalOriginalCostField.setBorder(fieldsBorder);
        totalOriginalCostPanel.add(totalOriginalCostField);
        analyzePanel.add(totalOriginalCostPanel);

        totalPricePanel = new JPanel(flowLayout);
        totalPriceLabel = new JLabel();
        totalPriceLabel.setText(messageSource.getMessage("component.article.analyze.label.total.price.title",
                null, localeHolder.getLocale()));
        totalPricePanel.add(totalPriceLabel);
        totalPriceField = new JLabel();
        totalPriceField.setBorder(fieldsBorder);
        totalPricePanel.add(totalPriceField);
        analyzePanel.add(totalPricePanel);
    }

    /**
     * Initializes fields ( totalCountField, totalOriginalCostField, totalPriceField) with inputted values.
     * @param totalCount int , total count of articles shown in the totalCountField;
     * @param totalCost  double, total original cost of articles shown in the totalOriginalCostField;
     * @param totalPrice double, total price of articles shown in the totalPriceField.
     */
    public void initializeFields (int totalCount, double totalCost, double totalPrice) {
        totalCountField.setText(String.valueOf(totalCount));
        totalOriginalCostField.setText(String.valueOf(totalCost));
        totalPriceField.setText(String.valueOf(totalPrice));
    }

    public JPanel getAnalyzePanel() {
        return analyzePanel;
    }

}
