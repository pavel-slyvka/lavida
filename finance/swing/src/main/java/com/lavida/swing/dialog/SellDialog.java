package com.lavida.swing.dialog;

import com.lavida.service.TagService;
import com.lavida.service.ViewColumn;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.TagJdo;
import com.lavida.service.utils.CalendarConverter;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.handler.SellDialogHandler;
import com.lavida.swing.service.ArticlesTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created: 10:39 15.08.13
 * the dialog for selling operation.
 * @author Ruslan
 */
@Component
public class SellDialog extends AbstractDialog {

    @Resource
    private SellDialogHandler handler;

    @Resource(name = "notSoldArticleTableModel")
    private ArticlesTableModel tableModel;

    @Resource
    private TagService tagService;

    private JPanel buttonPanel, inputPanel, oursPanel, commentPanel, tagsPanel;
    private JLabel codeLabel, nameLabel, brandLabel, sizeLabel, priceLabel, commentLabel, codeField, nameField,
            brandField, sizeField, priceField, shopLabel, discountLabel, totalCostLabel, saleDateLabel;
    private JTextField shopTextField, discountTextField, totalCostTextField, commentTextField, saleDateTextField;
    private JButton sellButton, cancelButton;
    private JCheckBox oursCheckBox, presentCheckBox;
    private List<JCheckBox> tagCheckBoxes = new ArrayList<JCheckBox>();

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("sellDialog.dialog.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setSize(400, 550);
        dialog.setLocationRelativeTo(null);
    }

    public void initWithArticleJdo(ArticleJdo articleJdo) {
        codeField.setText(articleJdo.getCode());
        nameField.setText(articleJdo.getName());
        brandField.setText(articleJdo.getBrand());
        sizeField.setText(articleJdo.getSize());
        priceField.setText(String.valueOf(articleJdo.getSalePrice()));
        commentTextField.setText(articleJdo.getComment());
        discountTextField.setText("0.0");
        handler.discountTextEntered();
        saleDateTextField.setText(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));
        shopTextField.setText(messageSource.getMessage("sellDialog.text.field.shop.text", null, localeHolder.getLocale()));
        oursCheckBox.setSelected(false);
        presentCheckBox.setSelected(false);
        for (JCheckBox checkbox : tagCheckBoxes) {
            checkbox.setSelected(false);
        }
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());

//      input panel
        inputPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
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

        discountLabel = new JLabel();
        discountLabel.setText(messageSource.getMessage("sellDialog.label.discount.title", null, localeHolder.getLocale()));
        discountLabel.setLabelFor(discountTextField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(discountLabel, constraints);

        discountTextField = new JTextField("0.0", 50);
        discountTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                int id = e.getID();
                if (id == KeyEvent.KEY_RELEASED) {
                    handler.discountTextEntered();
                }
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(discountTextField, constraints);

        totalCostLabel = new JLabel();
        totalCostLabel.setText(messageSource.getMessage("sellDialog.label.totalCost.title", null, localeHolder.getLocale()));
        totalCostLabel.setLabelFor(totalCostTextField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(totalCostLabel, constraints);

        totalCostTextField = new JTextField(50);
        totalCostTextField.setEditable(false);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(totalCostTextField, constraints);

        shopLabel = new JLabel();
        shopLabel.setText(messageSource.getMessage("sellDialog.label.shop.title", null, localeHolder.getLocale()));
        shopLabel.setLabelFor(shopTextField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(shopLabel, constraints);

        shopTextField = new JTextField();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(shopTextField, constraints);

        saleDateLabel = new JLabel();
        saleDateLabel.setText(messageSource.getMessage("sellDialog.label.saleDate.title", null, localeHolder.getLocale()));
        saleDateLabel.setLabelFor(saleDateTextField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(saleDateLabel, constraints);

        saleDateTextField = new JTextField();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(saleDateTextField, constraints);

        oursPanel = new JPanel(new FlowLayout());
        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1.0;
        oursPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("sellDialog.panel.ours.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        oursCheckBox = new JCheckBox();
        presentCheckBox = new JCheckBox();
        oursCheckBox.setText(messageSource.getMessage("sellDialog.checkBox.ours.text", null, localeHolder.getLocale()));
        oursCheckBox.setActionCommand(messageSource.getMessage("sellDialog.checkBox.ours.text", null, localeHolder.getLocale()));
        presentCheckBox.setText(messageSource.getMessage("sellDialog.checkBox.present.text", null, localeHolder.getLocale()));
        presentCheckBox.setActionCommand(messageSource.getMessage("sellDialog.checkBox.present.text", null, localeHolder.getLocale()));

        oursCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    handler.oursCheckBoxSelected();
                    handler.discountTextEntered();
                } else if (state == ItemEvent.DESELECTED) {
                    handler.checkBoxDeSelected();
                    handler.discountTextEntered();
                }
            }
        });

        presentCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    handler.presentCheckBoxSelected();
                    handler.discountTextEntered();
                } else if (state == ItemEvent.DESELECTED) {
                    handler.checkBoxDeSelected();
                    handler.discountTextEntered();
                }
            }
        });

        oursPanel.add(oursCheckBox);
        oursPanel.add(presentCheckBox);
        inputPanel.add(oursPanel, constraints);

        commentPanel = new JPanel(new GridLayout(2, 1));
        constraints.gridx = 0;
        constraints.gridy = 10;
        commentLabel = new JLabel();
        commentLabel.setText(messageSource.getMessage("sellDialog.label.comment.title", null, localeHolder.getLocale()));
        commentPanel.add(commentLabel);
        commentTextField = new JTextField(200);
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        commentPanel.add(commentTextField);

        inputPanel.add(commentPanel, constraints);

//        tags panel
        tagsPanel = new JPanel();
        FlowLayout tagsLayout = new FlowLayout();
        tagsPanel.setLayout(tagsLayout);
        tagsLayout.setAlignment(FlowLayout.TRAILING);
        List<TagJdo> tags = tagService.getAll();
        for (TagJdo tagJdo : tags) {
            JCheckBox tagCheckBox = new JCheckBox(tagJdo.getTitle());
            tagCheckBox.setActionCommand(tagJdo.getName());
            tagCheckBoxes.add(tagCheckBox);
        }
        for (JCheckBox checkBox : tagCheckBoxes) {
            tagsPanel.add(checkBox);
        }
        constraints.gridx = 0;
        constraints.gridy = 11;
        inputPanel.add(tagsPanel, constraints);


        rootContainer.add(inputPanel, BorderLayout.CENTER);

//        button panel
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        buttonPanel = new JPanel(flowLayout);
        sellButton = new JButton(messageSource.getMessage("sellDialog.button.sell.title", null,
                localeHolder.getLocale()));
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArticleJdo articleJdo = tableModel.getSelectedArticle();
                handler.sellButtonClicked(articleJdo);
            }
        });

        cancelButton = new JButton(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
                mainForm.getTableModel().setSelectedArticle(null);
                mainForm.getForm().setVisible(true);
                mainForm.update();
            }
        });
        buttonPanel.add(sellButton);
        buttonPanel.add(cancelButton);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);


    }

    public JLabel getPriceField() {
        return priceField;
    }

    public JTextField getCommentTextField() {
        return commentTextField;
    }

    public JCheckBox getOursCheckBox() {
        return oursCheckBox;
    }

    public JCheckBox getPresentCheckBox() {
        return presentCheckBox;
    }

    public List<JCheckBox> getTagCheckBoxes() {
        return tagCheckBoxes;
    }

    public JTextField getShopTextField() {
        return shopTextField;
    }

    public JTextField getDiscountTextField() {
        return discountTextField;
    }

    public JTextField getTotalCostTextField() {
        return totalCostTextField;
    }

    public JTextField getSaleDateTextField() {
        return saleDateTextField;
    }
}
