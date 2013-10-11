package com.lavida.swing.dialog;

import com.lavida.swing.form.component.ChangedFieldTableComponent;
import com.lavida.swing.handler.PostponedChangesDialogHandler;
import com.lavida.swing.service.ChangedFieldTableModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ArticlePostponedChangesDialog
 * <p/>
 * Created: 10.10.13 11:54.
 *
 * @author Ruslan.
 */
@Component
public class PostponedChangesDialog extends AbstractDialog {

    @Resource
    private PostponedChangesDialogHandler handler;

    @Resource(name = "articlePostponedChangedFieldTableModel")
    private ChangedFieldTableModel tableModel;

    private JMenuBar menuBar;
//    private JMenu  selectedMenu;
    private JMenuItem deletePostponedItem, recommitPostponedItem, loadPostponedItem, savePostponedItem;
    private ChangedFieldTableComponent tableComponent = new ChangedFieldTableComponent();


    @Override
    protected void initializeForm() {
        super.initializeForm();
        dialog.setTitle(messageSource.getMessage("dialog.changed.field.postponed.title", null, localeHolder.getLocale()));
        dialog.setResizable(true);
        dialog.setBounds(100, 100, 900, 700);
        dialog.setLocationRelativeTo(null);
    }


    @Override
    protected void initializeComponents() {
        JPanel  southPanel, desktopPanel, filtersPanel, mainPanel, buttonPanel;
         JButton  cancelButton;


        rootContainer.setLayout(new BorderLayout());

//        menuBar
        initializeMenuBar();
        dialog.setJMenuBar(menuBar);

//      desktop pane
        desktopPanel = new JPanel();
        desktopPanel.setLayout(new BorderLayout());
        desktopPanel.setBorder(BorderFactory.createEmptyBorder());

        tableComponent.initializeComponents(tableModel, messageSource, localeHolder);
//      main panel for table
        mainPanel = tableComponent.getMainPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(1,5,0,5));
        desktopPanel.add(mainPanel, BorderLayout.CENTER);
//        south panel for desktopPanel

        southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);
        southPanel.setBorder(BorderFactory.createEmptyBorder());

//      panel for search operations
        filtersPanel= tableComponent.getChangedFieldFiltersComponent().getFiltersPanel();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        southPanel.add(filtersPanel, constraints);
        desktopPanel.add(southPanel, BorderLayout.SOUTH);

        rootContainer.add(desktopPanel, BorderLayout.CENTER);

//        south panel for buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        cancelButton = new JButton();
        cancelButton.setHorizontalTextPosition(JButton.CENTER);
        cancelButton.setPreferredSize(new Dimension(150, 25));
        cancelButton.setMaximumSize(new Dimension(150, 25));
        cancelButton.setMinimumSize(new Dimension(150, 25));
        cancelButton.setText(messageSource.getMessage("sellDialog.button.cancel.title", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.cancelButtonClicked();
            }
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);

        rootContainer.add(buttonPanel, BorderLayout.SOUTH);

    }

    private void initializeMenuBar() {
         JMenu  selectedMenu;
         JMenuItem deselectAllItem, deleteSelectedItem, revertChangesItem;

        menuBar = new JMenuBar();

        //        postponed menu
        JMenu postponedMenu = new JMenu();
        postponedMenu.setText(messageSource.getMessage("mainForm.menu.postponed.title", null, localeHolder.getLocale()));

        savePostponedItem = new JMenuItem();
        savePostponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.save.title", null, localeHolder.getLocale()));
        savePostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.savePostponedItemClicked();
            }
        });

        loadPostponedItem = new JMenuItem();
        loadPostponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.load.title", null, localeHolder.getLocale()));
        loadPostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.loadPostponedItemClicked();
            }
        });

        recommitPostponedItem = new JMenuItem();
        recommitPostponedItem.setText(messageSource.getMessage("mainForm.button.recommit.title", null, localeHolder.getLocale()));
        recommitPostponedItem.add(new JSeparator());
        recommitPostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.recommitPostponedItemClicked();
            }
        });

        deletePostponedItem = new JMenuItem();
        deletePostponedItem.setText(messageSource.getMessage("mainForm.menu.postponed.delete.title", null, localeHolder.getLocale()));
        deletePostponedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deletePostponedItemClicked();
            }
        });

        postponedMenu.add(savePostponedItem);
        postponedMenu.add(loadPostponedItem);
        postponedMenu.add(recommitPostponedItem);
        postponedMenu.add(deletePostponedItem);


        //        selected menu
        selectedMenu = new JMenu();
        selectedMenu.setText(messageSource.getMessage("mainForm.menu.selected", null, localeHolder.getLocale()));

        deselectAllItem = new JMenuItem();
        deselectAllItem.setText(messageSource.getMessage("mainForm.menu.selected.deselect.articles", null, localeHolder.getLocale()));
        deselectAllItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deselectAllItemClicked();
            }
        });

        deleteSelectedItem = new JMenuItem();
        deleteSelectedItem.setText(messageSource.getMessage("dialog.changed.field.article.menu.selected.delete", null, localeHolder.getLocale()));
        deleteSelectedItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.deleteSelectedItemClicked();
            }
        });

        selectedMenu.add(deselectAllItem);
        selectedMenu.add(deleteSelectedItem);

        menuBar.add(selectedMenu);
        menuBar.add(postponedMenu);

    }

    public ChangedFieldTableModel getTableModel() {
        return tableModel;
    }

    public JMenuItem getDeletePostponedItem() {
        return deletePostponedItem;
    }

    public JMenuItem getRecommitPostponedItem() {
        return recommitPostponedItem;
    }

    public JMenuItem getLoadPostponedItem() {
        return loadPostponedItem;
    }

    public JMenuItem getSavePostponedItem() {
        return savePostponedItem;
    }
}
