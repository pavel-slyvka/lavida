package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.form.MainForm;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created: 17:48 17.09.13
 *
 * @author Ruslan
 */
public class ChooserDialogComponent  {
    protected JDialog dialog;
    protected Container rootContainer;

    private JPanel inputPanel, buttonPanel;
    private JButton okButton, cancelButton;
    private JLabel label;
    private JComponent component;
    private GridBagConstraints constraints;
    private String value;

    @Resource
    protected MainForm mainForm;

    public ChooserDialogComponent(MessageSource messageSource, LocaleHolder localeHolder) {
        dialog = new JDialog(mainForm.getForm(), true);
        rootContainer = dialog.getContentPane();
        rootContainer.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setBounds(100, 100, 300, 400);
        dialog.setLocationRelativeTo(null);

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        label = new JLabel();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        inputPanel.add(label, constraints);

        rootContainer.add(inputPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

        okButton = new JButton();
        okButton.setText(messageSource.getMessage("component.ChooserDialog.button.ok", null, localeHolder.getLocale()));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField)component;
                    setValue(textField.getText().trim());
                } else if (component instanceof JComboBox) {
                    JComboBox comboBox = (JComboBox)component;
                    setValue((String)comboBox.getSelectedItem());
                }
                dialog.dispose();
            }
        });

        cancelButton = new JButton();
        cancelButton.setText(messageSource.getMessage("component.ChooserDialog.button.cancel", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(null);
                dialog.dispose();
            }
        });
    }

    public  String showDialog( String dialogTitle, String labelText, JComponent component) {
        value = null;
        dialog.setTitle(dialogTitle);
        label.setText(labelText);
        label.setLabelFor(component);
        this.component = component;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        inputPanel.add(this.component, constraints);

        dialog.setVisible(true);
//        dialog.dispose();

        return getValue();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
