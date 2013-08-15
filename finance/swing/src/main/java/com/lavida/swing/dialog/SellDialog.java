package com.lavida.swing.dialog;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.ExchangerHolder;
import com.lavida.swing.handler.SellDialogHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created: 10:39 15.08.13
 *
 * @author Ruslan
 */
@Component
public class SellDialog extends AbstractDialog{

    @Resource
    private SellDialogHandler handler;
    @Resource
    private ExchangerHolder exchangerHolder;

    private double sellingPrice;
    private double startPrice;

    private ArticleJdo articleJdo;

//    private SoldArticleJdo soldArticleJdo;

    private JPanel buttonPanel, inputPanel, oursPanel, commentPanel;
    private JLabel codeLabel, nameLabel, brandLabel, sizeLabel, priceLabel, commentLabel,
            codeField,nameField, brandField, sizeField, priceField;
//    private JTextField commentField;
    private JTextArea commentTextArea;
    private JButton sellButton, cancelButton;
    private JCheckBox oursCheckBox, presentCheckBox;
    private ButtonGroup oursButtonGroup;
    private Border fieldsBorder;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("sellDialog.dialog.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 800, 500);
        dialog.setLocationRelativeTo(null);
    }

    public void initWithArticleJdo(ArticleJdo articleJdo) {
        this.articleJdo = articleJdo;
        startPrice = articleJdo.getPriceUAH();
        codeField.setText(this.articleJdo.getCode());
        nameField.setText(this.articleJdo.getName());
         brandField.setText(this.articleJdo.getBrand());
        sizeField.setText(this.articleJdo.getSize());
        priceField.setText(String.valueOf(this.articleJdo.getPriceUAH()));
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setBackground(Color.LIGHT_GRAY);
        rootContainer.setLayout(new BorderLayout());
//      input panel
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        fieldsBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        constraints.gridx = 0;
        constraints.gridy = 0;
        codeLabel = new JLabel();
        codeLabel.setText(messageSource.getMessage("sellDialog.label.code.title", null, localeHolder.getLocale()));
        inputPanel.add(codeLabel, constraints);

        codeField = new JLabel();
        codeField.setBorder(fieldsBorder);
        constraints.gridx = 1;
        constraints.gridy = 0;
        inputPanel.add(codeField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        nameLabel = new JLabel();
        nameLabel.setText(messageSource.getMessage("sellDialog.label.name.title", null, localeHolder.getLocale()));
        inputPanel.add(nameLabel, constraints);

        nameField = new JLabel();
        nameField.setBorder(fieldsBorder);
        constraints.gridx = 1;
        constraints.gridy = 1;
        inputPanel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        brandLabel = new JLabel();
        brandLabel.setText(messageSource.getMessage("sellDialog.label.brand.title", null, localeHolder.getLocale()));
        inputPanel.add(brandLabel, constraints);

        brandField = new JLabel();
        brandField.setBorder(fieldsBorder);
        constraints.gridx = 1;
        constraints.gridy = 2;
        inputPanel.add(brandField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        sizeLabel = new JLabel();
        sizeLabel.setText(messageSource.getMessage("sellDialog.label.size.title", null, localeHolder.getLocale()));
        inputPanel.add(sizeLabel, constraints);

        sizeField = new JLabel();
        sizeField.setBorder(fieldsBorder);
        constraints.gridx = 1;
        constraints.gridy = 3;
        inputPanel.add(sizeField, constraints);


        constraints.gridx = 0;
        constraints.gridy = 4;
        priceLabel = new JLabel();
        priceLabel.setText(messageSource.getMessage("sellDialog.label.price.title", null, localeHolder.getLocale()));
        inputPanel.add(priceLabel, constraints);

        priceField = new JLabel();
        priceField.setBorder(fieldsBorder);
        constraints.gridx = 1;
        constraints.gridy = 4;
        inputPanel.add(priceField, constraints);

        oursPanel = new JPanel(new FlowLayout());
        constraints.gridx = 0;
        constraints.gridy = 5;
        oursPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("sellDialog.panel.ours.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        oursButtonGroup = new ButtonGroup();
        oursCheckBox = new JCheckBox();
        presentCheckBox = new JCheckBox();
        oursCheckBox.setText(messageSource.getMessage("sellDialog.checkBox.ours.text", null, localeHolder.getLocale()));
        oursCheckBox.setActionCommand(messageSource.getMessage("sellDialog.checkBox.ours.text", null, localeHolder.getLocale()));
        presentCheckBox.setText(messageSource.getMessage("sellDialog.checkBox.present.text", null, localeHolder.getLocale()));
        presentCheckBox.setActionCommand(messageSource.getMessage("sellDialog.checkBox.present.text", null, localeHolder.getLocale()));

        oursCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox source = (JCheckBox)e.getSource();
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED){
                    articleJdo.setOurs(source.getActionCommand());
                    oursCheckBoxSelected();
                } else if (state == ItemEvent.DESELECTED) {
                    articleJdo.setOurs("");
                   checkBoxDeSelected();
                }

            }
        });

        presentCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox source = (JCheckBox)e.getSource();
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED){
                    articleJdo.setOurs(source.getActionCommand());
                    presentCheckBoxSelected();
                } else if (state == ItemEvent.DESELECTED) {
                    articleJdo.setOurs("");
                    checkBoxDeSelected();
                }
            }
        });
//        oursButtonGroup.add(oursCheckBox);
//        oursButtonGroup.add(presentCheckBox);
        oursPanel.add(oursCheckBox);
        oursPanel.add(presentCheckBox);
        inputPanel.add(oursPanel, constraints);

        commentPanel = new JPanel(new GridLayout(2, 1));
        constraints.gridx = 0;
        constraints.gridy = 6;
        commentLabel = new JLabel();
        commentLabel.setText(messageSource.getMessage("sellDialog.label.comment.title", null, localeHolder.getLocale()));
        commentPanel.add(commentLabel);

        commentTextArea = new JTextArea();
        commentTextArea.setPreferredSize(new Dimension(300, 50));
        commentPanel.add(commentTextArea);

        inputPanel.add(commentPanel, constraints);

        rootContainer.add(inputPanel, BorderLayout.CENTER);

//        button panel
        buttonPanel = new JPanel(new FlowLayout());
        sellButton = new JButton(messageSource.getMessage("sellDialog.button.sell.title", null,
                localeHolder.getLocale()));
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sell(articleJdo);
            }
        });

        cancelButton = new JButton(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commentTextArea.setText("");
//                oursButtonGroup.clearSelection();
                oursCheckBox.setSelected(false);
                presentCheckBox.setSelected(false);
                hide();
                mainForm.getForm().setVisible(true);
                mainForm.update();
            }
        });
        buttonPanel.add(sellButton);
        buttonPanel.add(cancelButton);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);


    }


    private double exchangeEurToUah(double priceEur) {
        double priceUah = priceEur * exchangerHolder.getSellRateEUR();
        return priceUah;
    }

    public void oursCheckBoxSelected() {
        sellingPrice = (exchangeEurToUah(articleJdo.getPurchasingPriceEUR()));
        getPriceField().setText(String.valueOf(sellingPrice));
        articleJdo.setPriceUAH(sellingPrice);
    }
    public void checkBoxDeSelected() {
        getPriceField().setText(String.valueOf(startPrice));
        articleJdo.setPriceUAH(sellingPrice);
    }

    public void presentCheckBoxSelected () {
        sellingPrice = 0;
        getPriceField().setText(String.valueOf(sellingPrice));
        articleJdo.setPriceUAH(sellingPrice);
    }

    public void setHandler(SellDialogHandler handler) {
        this.handler = handler;
    }

    public ArticleJdo getArticleJdo() {
        return articleJdo;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public JPanel getInputPanel() {
        return inputPanel;
    }

    public JPanel getOursPanel() {
        return oursPanel;
    }

    public JPanel getCommentPanel() {
        return commentPanel;
    }

    public JLabel getCodeLabel() {
        return codeLabel;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getPriceLabel() {
        return priceLabel;
    }

    public JLabel getCommentLabel() {
        return commentLabel;
    }

    public JLabel getBrandLabel() {
        return brandLabel;
    }

    public JLabel getSizeLabel() {
        return sizeLabel;
    }

    public JLabel getCodeField() {
        return codeField;
    }

    public JLabel getNameField() {
        return nameField;
    }

    public JLabel getBrandField() {
        return brandField;
    }

    public JLabel getSizeField() {
        return sizeField;
    }

    public JLabel getPriceField() {
        return priceField;
    }

    public JTextArea getCommentTextArea() {
        return commentTextArea;
    }

    public JButton getSellButton() {
        return sellButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JCheckBox getOursCheckBox() {
        return oursCheckBox;
    }

    public JCheckBox getPresentCheckBox() {
        return presentCheckBox;
    }

    public ButtonGroup getOursButtonGroup() {
        return oursButtonGroup;
    }

    public void setExchangerHolder(ExchangerHolder exchangerHolder) {
        this.exchangerHolder = exchangerHolder;
    }
}
