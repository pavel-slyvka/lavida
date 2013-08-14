package com.lavida.swing.form;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.form.tablemodel.ArticlesTableModel;
import com.lavida.swing.handler.SellFormHandler;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created: 12:03 12.08.13
 * The SellForm represents selling action.
 *
 * @author Ruslan
 */
@Component
public class SellForm extends AbstractForm {

    @Resource
    private SellFormHandler handler;

    @Resource
    private MainForm mainForm;

    @Resource
    protected MessageSource messageSource;

    @Resource
    private ArticlesTableModel tableModel;

    private ArticleJdo articleJdo;

//    private SoldArticleJdo soldArticleJdo;

    private JPanel buttonPanel, inputPanel, oursPanel, commentPanel;
    private JLabel codeLabel, nameLabel, priceLabel, commentLabel;
    private JTextField commentField;
    private JButton sellButton, cancelButton;
    private JCheckBox oursCheckBox, presentCheckBox;
    private ButtonGroup oursButtonGroup;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        form.setTitle(messageSource.getMessage("sellForm.form.title", null, localeHolder.getLocale()));
        form.setResizable(true);
        form.setBounds(100, 100, 800, 500);
        form.setLocationRelativeTo(null);
    }

    public void initWithArticleJdo(ArticleJdo articleJdo) {
        this.articleJdo = articleJdo;
        codeLabel.setText(messageSource.getMessage("sellForm.label.code.title", null, localeHolder.getLocale())
                + articleJdo.getCode());
        nameLabel.setText(messageSource.getMessage("sellForm.label.name.title", null, localeHolder.getLocale())
                + articleJdo.getName());
        priceLabel.setText(messageSource.getMessage("sellForm.label.price.title", null, localeHolder.getLocale())
                + articleJdo.getPriceUAH());
        commentLabel.setText(messageSource.getMessage("sellForm.label.comment.title", null, localeHolder.getLocale()));

        oursCheckBox.setText(messageSource.getMessage("sellForm.checkBox.ours.text", null, localeHolder.getLocale()));
        oursCheckBox.setActionCommand(messageSource.getMessage("sellForm.checkBox.ours.text", null, localeHolder.getLocale()));
        presentCheckBox.setText(messageSource.getMessage("sellForm.checkBox.present.text", null, localeHolder.getLocale()));
        presentCheckBox.setActionCommand(messageSource.getMessage("sellForm.checkBox.present.text", null, localeHolder.getLocale()));
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

        constraints.gridx = 0;
        constraints.gridy = 0;
        codeLabel = new JLabel();
        inputPanel.add(codeLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        nameLabel = new JLabel();
        inputPanel.add(nameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        priceLabel = new JLabel();
        inputPanel.add(priceLabel, constraints);

        oursPanel = new JPanel(new FlowLayout());
        constraints.gridx = 0;
        constraints.gridy = 3;
        oursPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("sellForm.panel.ours.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        oursButtonGroup = new ButtonGroup();
        oursCheckBox = new JCheckBox();
        presentCheckBox = new JCheckBox();
        oursButtonGroup.add(oursCheckBox);
        oursButtonGroup.add(presentCheckBox);
        oursPanel.add(oursCheckBox);
        oursPanel.add(presentCheckBox);
        inputPanel.add(oursPanel, constraints);

        commentPanel = new JPanel(new GridLayout(2, 1));
        constraints.gridx = 0;
        constraints.gridy = 4;
        commentLabel = new JLabel();
        commentPanel.add(commentLabel);
        commentField = new JTextField();
        commentField.setPreferredSize(new Dimension(250, 50));
        commentPanel.add(commentField);
        inputPanel.add(commentPanel, constraints);

        rootContainer.add(inputPanel, BorderLayout.CENTER);

//        button panel
        buttonPanel = new JPanel(new FlowLayout());
        sellButton = new JButton(messageSource.getMessage("sellForm.button.sell.title", null,
                localeHolder.getLocale()));
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.sell(articleJdo, tableModel);
            }
        });

        cancelButton = new JButton(messageSource.getMessage("sellForm.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
                mainForm.form.setVisible(true);
                mainForm.update();
            }
        });
        buttonPanel.add(sellButton);
        buttonPanel.add(cancelButton);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setHandler(SellFormHandler handler) {
        this.handler = handler;
    }

    public void setMainForm(MainForm mainForm) {
        this.mainForm = mainForm;
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

    public JTextField getCommentField() {
        return commentField;
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

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
