package com.lavida.swing.dialog;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.handler.RefundDialogHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
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

    @Resource
    private SoldProductsDialog soldProductsDialog;

    @Resource(name = "soldArticleTableModel")
    private ArticlesTableModel tableModel;


    private JPanel buttonPanel, inputPanel, commentPanel;
    private JLabel codeLabel, nameLabel, brandLabel, sizeLabel, priceLabel, commentLabel,
            codeField, nameField, brandField, sizeField, priceField;
    private JTextField commentTextField;
    private JButton refundButton, cancelButton;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.refund.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 300, 250);
        dialog.setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        rootContainer.setBackground(Color.LIGHT_GRAY);
        rootContainer.setLayout(new BorderLayout());
//      input panel
        inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        codeLabel = new JLabel();
        codeLabel.setText(messageSource.getMessage("sellDialog.label.code.title", null, localeHolder.getLocale()));
        codeLabel.setLabelFor(codeField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(codeLabel, constraints);

        codeField = new JLabel();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(codeField, constraints);

        nameLabel = new JLabel();
        nameLabel.setText(messageSource.getMessage("sellDialog.label.name.title", null, localeHolder.getLocale()));
        nameLabel.setLabelFor(nameField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(nameLabel, constraints);

        nameField = new JLabel();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(nameField, constraints);

        brandLabel = new JLabel();
        brandLabel.setText(messageSource.getMessage("sellDialog.label.brand.title", null, localeHolder.getLocale()));
        brandLabel.setLabelFor(brandField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(brandLabel, constraints);

        brandField = new JLabel();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(brandField, constraints);

        sizeLabel = new JLabel();
        sizeLabel.setText(messageSource.getMessage("sellDialog.label.size.title", null, localeHolder.getLocale()));
        sizeLabel.setLabelFor(sizeField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(sizeLabel, constraints);

        sizeField = new JLabel();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(sizeField, constraints);

        priceLabel = new JLabel();
        priceLabel.setText(messageSource.getMessage("sellDialog.label.price.title", null, localeHolder.getLocale()));
        priceLabel.setLabelFor(priceField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(priceLabel, constraints);

        priceField = new JLabel();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(priceField, constraints);

        commentPanel = new JPanel(new GridLayout(2, 1));
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1.0;
        commentLabel = new JLabel();
        commentLabel.setText(messageSource.getMessage("sellDialog.label.comment.title", null, localeHolder.getLocale()));
        commentPanel.add(commentLabel);

        commentTextField = new JTextField();
        commentTextField.setPreferredSize(new Dimension(200, 50));
        commentPanel.add(commentTextField);

        inputPanel.add(commentPanel, constraints);

        rootContainer.add(inputPanel, BorderLayout.CENTER);

//        button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        refundButton = new JButton(messageSource.getMessage("dialog.sold.products.button.refund.title", null,
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
                commentTextField.setText("");
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
        commentTextField.setText(articleJdo.getComment());
    }

    public JTextField getCommentTextField() {
        return commentTextField;
    }

    public SoldProductsDialog getSoldProductsDialog() {
        return soldProductsDialog;
    }
}
