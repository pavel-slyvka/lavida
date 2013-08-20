package com.lavida.swing.dialog;

import com.lavida.service.TagService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.TagJdo;
import com.lavida.swing.handler.SellDialogHandler;
import com.lavida.swing.service.ArticlesTableModel;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 10:39 15.08.13
 *
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

    private JPanel buttonPanel, inputPanel, oursPanel, commentPanel, tagsPanel, shopPanel;
    private JLabel codeLabel, nameLabel, brandLabel, sizeLabel, priceLabel, commentLabel,
            codeField, nameField, brandField, sizeField, priceField, shopLabel;
    private JTextArea commentTextArea;
    private JTextField shopTextField;
    private JButton sellButton, cancelButton;
    private JCheckBox oursCheckBox, presentCheckBox;
    private Border fieldsBorder;
    private List<JCheckBox> tagCheckBoxes = new ArrayList<JCheckBox>();
    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("sellDialog.dialog.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 800, 500);
        dialog.setLocationRelativeTo(null);
    }

    public void initWithArticleJdo(ArticleJdo articleJdo) {
        codeField.setText(articleJdo.getCode());
        nameField.setText(articleJdo.getName());
        brandField.setText(articleJdo.getBrand());
        sizeField.setText(articleJdo.getSize());
        priceField.setText(String.valueOf(articleJdo.getPriceUAH()));
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
                } else if (state == ItemEvent.DESELECTED) {
                    handler.checkBoxDeSelected();
                }
            }
        });

        presentCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    handler.presentCheckBoxSelected();
                } else if (state == ItemEvent.DESELECTED) {
                    handler.checkBoxDeSelected();
                }
            }
        });

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

//        tags panel
        tagsPanel = new JPanel();
        FlowLayout tagsLayout = new FlowLayout();
        tagsPanel.setLayout(tagsLayout);
        tagsLayout.setAlignment(FlowLayout.TRAILING);
        List<TagJdo> tags = tagService.getAll();
        for (TagJdo tagJdo : tags) {
            JCheckBox tagCheckBox = new JCheckBox(tagJdo.getTitle());
            tagCheckBox.setActionCommand(tagJdo.getName());
//            tagCheckBox.addItemListener(new ItemListener() {
//                @Override
//                public void itemStateChanged(ItemEvent e) {
//                    int state = e.getStateChange();
//                    if (state == ItemEvent.SELECTED) {
//                        handler.tagCheckBoxSelected();
//                    } else if (state == ItemEvent.DESELECTED) {
//                        handler.tagCheckBoxDeSelected();
//                    }
//                }
//            });
            tagCheckBoxes.add(tagCheckBox);
        }
        for (JCheckBox checkBox : tagCheckBoxes) {
            tagsPanel.add(checkBox);
        }
        constraints.gridx = 0;
        constraints.gridy = 7;
        inputPanel.add(tagsPanel, constraints);

//        shop panel
        shopPanel = new JPanel(new GridLayout(2, 1));
        constraints.gridx = 0;
        constraints.gridy = 8;
        shopLabel = new JLabel();
        shopLabel.setText(messageSource.getMessage("sellDialog.label.shop.title", null, localeHolder.getLocale()));
        shopPanel.add(shopLabel);
        shopTextField = new JTextField();
        shopTextField.setText(messageSource.getMessage("sellDialog.text.field.shop.text", null, localeHolder.getLocale()));
        shopPanel.add(shopTextField);
        inputPanel.add(shopPanel, constraints);

        rootContainer.add(inputPanel, BorderLayout.CENTER);

//        button panel
        buttonPanel = new JPanel(new FlowLayout());
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
                commentTextArea.setText("");
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
    public JLabel getPriceField() {
        return priceField;
    }

    public JTextArea getCommentTextArea() {
        return commentTextArea;
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
}
