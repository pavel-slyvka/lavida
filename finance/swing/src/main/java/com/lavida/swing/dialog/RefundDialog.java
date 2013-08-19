package com.lavida.swing.dialog;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.handler.RefundDialogHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created: 21:48 18.08.13
 * The RefundDialog is a dialog for refunding selected product.
 * @author Ruslan
 */
@Component
public class RefundDialog extends AbstractDialog {

    @Resource
    private RefundDialogHandler handler;

    @Resource(name = "soldArticleTableModel")
    private ArticlesTableModel tableModel;


    private JPanel buttonPanel, inputPanel, commentPanel;
    private JLabel codeLabel, nameLabel, brandLabel, sizeLabel, priceLabel, commentLabel,
            codeField, nameField, brandField, sizeField, priceField;
    private JTextArea commentTextArea;
    private JButton refundButton, cancelButton;
    private Border fieldsBorder;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.refund.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 800, 500);
        dialog.setLocationRelativeTo(null);
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

        commentPanel = new JPanel(new GridLayout(2, 1));
        constraints.gridx = 0;
        constraints.gridy = 5;
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
        refundButton = new JButton(messageSource.getMessage("sellDialog.button.sell.title", null,
                localeHolder.getLocale()));
        refundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArticleJdo articleJdo = tableModel.getSelectedArticle();
                handler.refundButtonClicked(articleJdo);
            }
        });

        cancelButton = new JButton(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commentTextArea.setText("");
                hide();
                mainForm.getForm().setVisible(true);
                mainForm.update();
            }
        });

        buttonPanel.add(refundButton);
        buttonPanel.add(cancelButton);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets text in labels from the selected articleJdo.
     * @param articleJdo the selected product.
     */
    public void initWithArticleJdo(ArticleJdo articleJdo) {
        codeField.setText(articleJdo.getCode());
        nameField.setText(articleJdo.getName());
        brandField.setText(articleJdo.getBrand());
        sizeField.setText(articleJdo.getSize());
        priceField.setText(String.valueOf(articleJdo.getPriceUAH()));

    }

    public JTextArea getCommentTextArea() {
        return commentTextArea;
    }
}
