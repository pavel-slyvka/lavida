package com.lavida.swing.dialog;

import com.lavida.swing.dialog.AbstractDialog;
import org.springframework.stereotype.*;

import javax.swing.*;
import java.awt.*;

/**
 * The UpdateInfoMessageDialog
 * <p/>
 * Created: 07.10.13 16:23.
 *
 * @author Ruslan.
 */
@org.springframework.stereotype.Component
@Deprecated
public class UpdateInfoMessageDialog extends AbstractDialog{

    private JLabel infoLabel;

    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("mainForm.panel.refresh.message.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setModal(false);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
        rootContainer.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        infoLabel = new JLabel();
        infoLabel.setHorizontalTextPosition(JLabel.CENTER);
        panel.add(infoLabel, BorderLayout.CENTER);
        rootContainer.add(panel, BorderLayout.CENTER);
    }

    public void showUpdateInfoMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int xLocation = getMainForm().getForm().getLocation().x + getMainForm().getForm().getWidth() - 300 ;
                int yLocation = getMainForm().getForm().getLocation().y + getMainForm().getForm().getHeight() - 150;
                dialog.setLocation(xLocation, yLocation);
//                dialog.setLocation(700, 500);
                infoLabel.setText(message);
                infoLabel.setOpaque(true);
                dialog.setVisible(true);
                try {
                    Thread.currentThread().sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                infoLabel.setText("");
                dialog.dispose();

            }
        }).start();
    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>") + "</html>";
    }
}
