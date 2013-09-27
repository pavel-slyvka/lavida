package com.lavida.swing.dialog;

import com.lavida.service.SellerService;
import com.lavida.service.TagService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.SellerJdo;
import com.lavida.service.entity.TagJdo;
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

    @Resource
    private SellerService sellerService;

    private JPanel buttonPanel, inputPanel, oursPanel, commentPanel, tagsPanel;
    private JLabel codeLabel, nameLabel, brandLabel, sizeLabel, priceLabel, commentLabel, codeField, nameField,
            brandField, sizeField, priceField, shopLabel, discountLabel, totalCostLabel, saleDateLabel, sellerNameLabel,
            discountCardNumberLabel;
    private JTextField  discountTextField, totalCostTextField, commentTextField, saleDateTextField,
            discountCardNumberTextField;
    private JButton sellButton, cancelButton;
    private ButtonGroup buttonGroup;
    private JCheckBox oursCheckBox, presentCheckBox, clientCheckBox;
    private JComboBox sellerNames, shopComboBox ;
    private List<JCheckBox> tagCheckBoxes = new ArrayList<>();
    private JLabel errorMessage;

    private String defaultShop;
    private String[] shopArray = {"СКЛАД", "LA VIDA", "СЛАВЯНСКИЙ", "НОВОМОСКОВСК", "АЛЕКСАНДРИЯ"};

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("sellDialog.dialog.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setSize(400, 600);
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
        shopComboBox.setSelectedItem(defaultShop);
        clientCheckBox.setSelected(true);
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

        discountCardNumberLabel = new JLabel();
        discountCardNumberLabel.setText(messageSource.getMessage("sellDialog.label.discount.card.number", null, localeHolder.getLocale()));
        discountCardNumberLabel.setLabelFor(discountCardNumberTextField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(discountCardNumberLabel, constraints);

        discountCardNumberTextField = new JTextField(50);
        discountCardNumberTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    handler.discountCardNumberTextEntered();
                    discountTextField.requestFocusInWindow();
                }
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(discountCardNumberTextField, constraints);

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
        totalCostLabel.setForeground(Color.BLUE);
        totalCostLabel.setText(messageSource.getMessage("sellDialog.label.totalCost.title", null, localeHolder.getLocale()));
        totalCostLabel.setLabelFor(totalCostTextField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(totalCostLabel, constraints);

        totalCostTextField = new JTextField(50);
        totalCostTextField.setForeground(Color.BLUE);
        Font boldFont = new Font(totalCostTextField.getFont().getName(), Font.BOLD, totalCostTextField.getFont().getSize());
        totalCostTextField.setFont(boldFont);
        totalCostTextField.setEditable(false);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(totalCostTextField, constraints);

        shopLabel = new JLabel();
        shopLabel.setText(messageSource.getMessage("sellDialog.label.shop.title", null, localeHolder.getLocale()));
        shopLabel.setLabelFor(shopComboBox);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(shopLabel, constraints);

        shopComboBox = new JComboBox(shopArray);
        shopComboBox.setEditable(false);
        shopComboBox.setSelectedItem("LA VIDA");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(shopComboBox, constraints);

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
        constraints.gridy = 10;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1.0;
        oursPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(messageSource.
                getMessage("sellDialog.panel.ours.title", null, localeHolder.getLocale())),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));

        clientCheckBox = new JCheckBox();
        oursCheckBox = new JCheckBox();
        presentCheckBox = new JCheckBox();
        clientCheckBox.setText(messageSource.getMessage("sellDialog.checkBox.client.text", null, localeHolder.getLocale()));
        clientCheckBox.setSelected(true);
        oursCheckBox.setText(messageSource.getMessage("sellDialog.checkBox.ours.text", null, localeHolder.getLocale()));
        oursCheckBox.setActionCommand(messageSource.getMessage("sellDialog.checkBox.ours.text", null, localeHolder.getLocale()));
        presentCheckBox.setText(messageSource.getMessage("sellDialog.checkBox.present.text", null, localeHolder.getLocale()));
        presentCheckBox.setActionCommand(messageSource.getMessage("sellDialog.checkBox.present.text", null, localeHolder.getLocale()));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(clientCheckBox);
        buttonGroup.add(oursCheckBox);
        buttonGroup.add(presentCheckBox);

        clientCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    handler.clientCheckBoxSelected();
                    handler.discountTextEntered();
                } else if (state == ItemEvent.DESELECTED) {
                    handler.checkBoxDeSelected();
                    handler.discountTextEntered();
                }
            }
        });

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

        oursPanel.add(clientCheckBox);
        oursPanel.add(oursCheckBox);
        oursPanel.add(presentCheckBox);
        inputPanel.add(oursPanel, constraints);

        commentPanel = new JPanel(new GridLayout(2, 1));
        constraints.gridx = 0;
        constraints.gridy = 11;
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
        constraints.gridy = 12;
        inputPanel.add(tagsPanel, constraints);

//        seller choosing
        sellerNameLabel = new JLabel();
        sellerNameLabel.setText(messageSource.getMessage("sellDialog.label.sellerName.title", null, localeHolder.getLocale()));
        sellerNameLabel.setLabelFor(sellerNames);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        constraints.gridx = 0;
        constraints.gridy = 13;
        inputPanel.add(sellerNameLabel, constraints);

        List<SellerJdo> sellers = sellerService.getAll();
        String[] sellerNamesArray = new String[sellers.size() + 1];
        for (int i = 0; i < sellers.size(); ++i) {
            sellerNamesArray[i] = sellers.get(i).getName();
        }
        sellerNames = new JComboBox(sellerNamesArray);
        sellerNames.setSelectedItem(tableModel.getSellerName());
        sellerNames.setEditable(false);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        constraints.gridy = 13;
        inputPanel.add(sellerNames, constraints);

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 14;
        inputPanel.add(errorMessage, constraints);


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
                handler.cancelButtonClicked();
            }
        });
        buttonPanel.add(sellButton);
        buttonPanel.add(cancelButton);
        rootContainer.add(buttonPanel, BorderLayout.SOUTH);


    }

    /**
     * Sets the value to the defaultShop field according to the user's first role.
     * @param userRoles the list of user's roles.
     */
    public void filterByRoles(List<String> userRoles) {
        for (String role : userRoles) {
            if ("ROLE_SELLER_LA_VIDA".equals(role)) {
                this.setDefaultShop(messageSource.getMessage("sellDialog.text.field.shop.LaVida", null,
                        localeHolder.getLocale()));
                shopComboBox.setEnabled(false);
                return;
            } else if ("ROLE_SELLER_SLAVYANKA".equals(role)) {
                this.setDefaultShop(messageSource.getMessage("sellDialog.text.field.shop.Slavyanka", null,
                        localeHolder.getLocale()));
                shopComboBox.setEnabled(false);
                return;
            } else if ("ROLE_SELLER_NOVOMOSKOVSK".equals(role)) {
                this.setDefaultShop(messageSource.getMessage("sellDialog.text.field.shop.Novomoskovsk", null,
                        localeHolder.getLocale()));
                shopComboBox.setEnabled(false);
                return;
            } else if ("ROLE_MANAGER".equals(role)) {
                this.setDefaultShop(messageSource.getMessage("sellDialog.text.field.shop.LaVida", null,
                        localeHolder.getLocale()));
                shopComboBox.setEnabled(true);
                return;
            } else if ("ROLE_SELLER_ALEXANDRIA".equals(role)) {
                this.setDefaultShop(messageSource.getMessage("sellDialog.text.field.shop.Alexandria", null,
                        localeHolder.getLocale()));
                shopComboBox.setEnabled(false);
                return;
            }

        }
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

    public JComboBox getShopComboBox() {
        return shopComboBox;
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

    public JComboBox getSellerNames() {
        return sellerNames;
    }

    public JTextField getDiscountCardNumberTextField() {
        return discountCardNumberTextField;
    }

    public JCheckBox getClientCheckBox() {
        return clientCheckBox;
    }

    public JLabel getErrorMessage() {
        return errorMessage;
    }

    public String getDefaultShop() {
        return defaultShop;
    }

    public void setDefaultShop(String defaultShop) {
        this.defaultShop = defaultShop;
    }
}
