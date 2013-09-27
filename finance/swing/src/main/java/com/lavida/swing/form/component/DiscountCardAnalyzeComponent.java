package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.service.DiscountCardsTableModel;
import org.springframework.context.MessageSource;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The component for analyze panel
 * Created: 16:59 06.09.13
 *
 * @author Ruslan
 */
public class DiscountCardAnalyzeComponent {

    private static final List<String> FORBIDDEN_ROLES = new ArrayList<String>();
    static {
        FORBIDDEN_ROLES.add("ROLE_SELLER_LA_VIDA");
        FORBIDDEN_ROLES.add("ROLE_SELLER_SLAVYANKA");
        FORBIDDEN_ROLES.add("ROLE_SELLER_NOVOMOSKOVSK");
        FORBIDDEN_ROLES.add("ROLE_SELLER_ALEXANDRIA");
    }

    private DiscountCardsTableModel tableModel;
    private MessageSource messageSource;
    private LocaleHolder localeHolder;

    private JPanel analyzePanel, totalCountPanel, totalSumPanel;
    private JLabel totalCountLabel, totalCountField, totalSumLabel, totalSumField;

    public void initializeComponents(DiscountCardsTableModel tableModel, MessageSource messageSource, LocaleHolder localeHolder) {
        this.tableModel = tableModel;
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
        totalCountLabel.setText(messageSource.getMessage("component.discounts.analyze.label.total.count.title",
                null, localeHolder.getLocale()));
        totalCountLabel.setLabelFor(totalCountField);
        totalCountPanel.add(totalCountLabel);
        totalCountField = new JLabel();
        totalCountField.setBorder(BorderFactory.createCompoundBorder(fieldsBorder,BorderFactory.createEmptyBorder(-2,0,0,0)));
        totalCountField.setText(String.valueOf(tableModel.getTotalCountCards()));
        totalCountPanel.add(totalCountField);
        analyzePanel.add(totalCountPanel);


        totalSumPanel = new JPanel(flowLayout);
        totalSumPanel.setPreferredSize(new Dimension(250, 25));
        totalSumPanel.setMinimumSize(new Dimension(200, 25));
        totalSumPanel.setMaximumSize(new Dimension(300, 25));
        totalSumPanel.setBorder(BorderFactory.createEmptyBorder());
        totalSumLabel = new JLabel();
        totalSumLabel.setBorder(BorderFactory.createEmptyBorder(-2, 0, 0, 0));
        totalSumLabel.setText(messageSource.getMessage("component.discounts.analyze.label.total.sum",
                null, localeHolder.getLocale()));
        totalSumLabel.setLabelFor(totalSumField);
        totalSumPanel.add(totalSumLabel);
        totalSumField = new JLabel();
        totalSumField.setText(roundTwoDecimals(tableModel.getTotalSumUAH()));
        totalSumField.setBorder(BorderFactory.createCompoundBorder(fieldsBorder, BorderFactory.createEmptyBorder(-2, 0, 0, 0)));
        totalSumPanel.add(totalSumField);
        analyzePanel.add(totalSumPanel);
    }

    /**
     * Update fields ( totalCountField, totalSumField) with inputted values.
     * @param totalCount int , total count of discount cards shown ;
     * @param totalSum  double, total sum of discount cards shown ;
     */
    public void updateFields (int totalCount, double totalSum) {
        totalCountField.setText(String.valueOf(totalCount));
        totalSumField.setText(roundTwoDecimals(totalSum));
    }

    /**
     * Formats double number to rounded String expression.
     * @param number double to be formatted.
     * @return
     */
    private String roundTwoDecimals (double number) {
        number = BigDecimal.valueOf(number).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//        return decimalFormat.format(number);
        return String.valueOf(number);
    }

    public void filterAnalyzeComponentByRoles(java.util.List<String> userRoles) {
        if (hasForbiddenRole(userRoles)) {
            totalSumPanel.setVisible(false);
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
