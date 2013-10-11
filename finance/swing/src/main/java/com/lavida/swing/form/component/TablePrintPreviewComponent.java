package com.lavida.swing.form.component;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import org.springframework.context.MessageSource;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.text.MessageFormat;

/**
 * The TablePrintPreviewComponent
 * <p/>
 * Created: 04.10.13 11:05.
 *
 * @author Ruslan.
 */
public class TablePrintPreviewComponent {
    private MessageSource messageSource;
    private LocaleHolder localeHolder;
    private JTable tableToPrint;
    private Component parentComponent;

    private JDialog previewDialog;
    private JPanel previewPanel;
    private CardLayout cardLayout;
    private Page[] previewPages;
    double zoomScale;

    private JComboBox<String>  tableScaleBox;
    private JComboBox<String> pageNumberBox;
    private JCheckBox footerCheckBox;
    private JSlider slider;

    private JButton  forwardButton, backwardButton, cancelButton; //pageSetUpButton, printButton,
    private JTextField headerTextField;
    private boolean pageNumbering;
    private Printable targetPrintable;
    private Pageable targetPageable;
    private PrinterJob printerJob;
    private HashPrintRequestAttributeSet attributeSet;
    private PageFormat pageFormat;
    private boolean printingComplete;
    private boolean reforming;

    public TablePrintPreviewComponent() {
        printingComplete = false;
        String[] scales = {"10 %", "25 %", "50 %", "75 %", "100 %"};
        tableScaleBox = new JComboBox<>(scales);
        pageNumberBox = new JComboBox<>();
        zoomScale = 1.0;
        cardLayout = new CardLayout();
        previewPanel = new JPanel(cardLayout);
        slider = new JSlider();
        printerJob = PrinterJob.getPrinterJob();
        attributeSet = new HashPrintRequestAttributeSet();
        pageFormat = printerJob.getPageFormat(attributeSet);
    }

    public boolean showPrintPreviewDialog(Component parent, JTable table, MessageSource messageSource, LocaleHolder localeHolder) {
        this.messageSource = messageSource;
        this.localeHolder = localeHolder;
        this.tableToPrint = table;
        this.parentComponent = parent;
        reforming = true;
        createPreview();

        return printingComplete;
    }

    private void createPreview() {
        if (parentComponent instanceof JFrame) {
            previewDialog = new JDialog((JFrame) parentComponent, true);
        } else if (parentComponent instanceof JDialog) {
            previewDialog = new JDialog((JDialog) parentComponent, true);
        }
        previewDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        previewDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                cancelButton.doClick();
            }
        });
        previewDialog.setResizable(true);
        previewDialog.getContentPane().setLayout(new BorderLayout());

        setTableHeaderFooterPanel();
        updatePageable();
        updatePreviewPanel();
        setToolBar();
        previewDialog.getContentPane().add(previewPanel, BorderLayout.CENTER);


        Dimension d = previewDialog.getToolkit().getScreenSize();
        previewDialog.setSize(d.width, d.height - 60);
        slider.setSize(previewDialog.getWidth() / 2, slider.getPreferredSize().height / 2);
        previewPages[pageNumberBox.getSelectedIndex()].refreshZoomScale();
        reforming = false;
        previewDialog.setVisible(true);
    }

    private void setTableHeaderFooterPanel() {
        JPanel headerFooterPanel = new JPanel();
        headerFooterPanel.setBorder(BorderFactory.createEmptyBorder());
        headerFooterPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel headerLabel = new JLabel();
        headerLabel.setText(messageSource.getMessage("component.print.preview.table.label.header", null, localeHolder.getLocale()));
        headerLabel.setLabelFor(headerTextField);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        headerFooterPanel.add(headerLabel, constraints);

        headerTextField = new JTextField(20);
        headerTextField.setText(messageSource.getMessage("component.print.preview.table.textField.header", null, localeHolder.getLocale()));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        headerFooterPanel.add(headerTextField, constraints);

        JLabel footerLabel = new JLabel();
        footerLabel.setText(messageSource.getMessage("component.print.preview.table.label.footer", null, localeHolder.getLocale()));
        footerLabel.setLabelFor(footerCheckBox);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        headerFooterPanel.add(footerLabel, constraints);

        footerCheckBox = new JCheckBox();
        footerCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    pageNumbering = true;
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    pageNumbering = false;
                }
            }
        });
        footerCheckBox.setSelected(true);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1.0;
        headerFooterPanel.add(footerCheckBox, constraints);

        previewDialog.getContentPane().add(headerFooterPanel, BorderLayout.SOUTH);
    }

    private void setToolBar() {
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.LINE_AXIS));

        JButton pageSetUpButton = new JButton();
        pageSetUpButton.setText(messageSource.getMessage("component.print.preview.table.button.pageSetUp", null, localeHolder.getLocale()));
        pageSetUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageFormat = printerJob.pageDialog(attributeSet);
                updatePreviewDialog();
            }
        });
        toolBar.add(pageSetUpButton);

        JButton printButton = new JButton();
        printButton.setText(messageSource.getMessage("component.print.preview.table.button.print", null, localeHolder.getLocale()));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printPageable();
                tableScaleBox.setSelectedItem("100 %");
                footerCheckBox.setSelected(true);
                previewDialog.dispose();
            }
        });
        toolBar.add(printButton);

        cancelButton = new JButton();
        cancelButton.setText(messageSource.getMessage("component.print.preview.table.button.cancel", null, localeHolder.getLocale()));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printingComplete = false;
                tableScaleBox.setSelectedItem("100 %");
                footerCheckBox.setSelected(true);
                previewDialog.dispose();
            }
        });
        toolBar.add(cancelButton);

        backwardButton = new JButton("<<");
        backwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNumberBox.setSelectedIndex(pageNumberBox.getSelectedIndex() == 0 ? 0 : pageNumberBox.getSelectedIndex() - 1);
                if (pageNumberBox.getSelectedIndex() == 0) {
                    backwardButton.setEnabled(false);
                }
            }
        });
        toolBar.add(backwardButton);

        pageNumberBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!reforming) {
                        cardLayout.show(previewPanel, (String) pageNumberBox.getSelectedItem());
                        previewPages[pageNumberBox.getSelectedIndex()].refreshZoomScale();
                        backwardButton.setEnabled(pageNumberBox.getSelectedIndex() != 0);
                        forwardButton.setEnabled(pageNumberBox.getSelectedIndex() != pageNumberBox.getItemCount() - 1);
                    }
                }
            }
        });
        toolBar.add(pageNumberBox);

        forwardButton = new JButton(">>");
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNumberBox.setSelectedIndex(pageNumberBox.getSelectedIndex() == pageNumberBox.getItemCount() - 1 ?
                        pageNumberBox.getSelectedIndex() : pageNumberBox.getSelectedIndex() + 1);
                if (pageNumberBox.getSelectedIndex() == pageNumberBox.getItemCount() - 1) {
                    forwardButton.setEnabled(false);
                }
            }
        });
        toolBar.add(forwardButton);

        toolBar.add(Box.createHorizontalGlue());

        slider.setBorder(new TitledBorder("Zoom"));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMinimum(0);
        slider.setMaximum(500);
        slider.setValue(100);
        slider.setMinorTickSpacing(20);
        slider.setMajorTickSpacing(100);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double temp = (double) slider.getValue() / 100.0;
                if (temp == zoomScale)
                    return;
                if (temp == 0) temp = 0.01;
                zoomScale = temp;
                previewPages[pageNumberBox.getSelectedIndex()].refreshZoomScale();
                previewDialog.validate();

            }
        });
        toolBar.add(slider);

        tableScaleBox.setEditable(true);
        tableScaleBox.setSelectedItem("100 %");
        tableScaleBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String scaleString = (String) tableScaleBox.getSelectedItem();
                    if (scaleString.endsWith("%")) {
                        scaleString = scaleString.substring(0, scaleString.length() - 1);
                    }
                    scaleString = scaleString.trim().replaceAll("[^0-9]", "");
                    int tableScale;
                    try {
                        tableScale = Integer.parseInt(scaleString);
                    } catch (NumberFormatException ne) {
                        return;
                    }
                    if (tableToPrint instanceof TableComponent) {
                        TableComponent tableComponent = ((TableComponent) tableToPrint);
                        tableComponent.setTableScale(tableScale);
                        updatePreviewDialog();
                    }
                }
            }
        });
        toolBar.add(tableScaleBox);

        previewDialog.getContentPane().add(toolBar, BorderLayout.NORTH);

    }

    private void updatePreviewDialog() {
        reforming = true;
        updatePageable();
        updatePreviewPanel();
        cardLayout.show(previewPanel, (String) pageNumberBox.getSelectedItem());
        previewPages[pageNumberBox.getSelectedIndex()].refreshZoomScale();
        previewPages[pageNumberBox.getSelectedIndex()].revalidate();
        previewPages[pageNumberBox.getSelectedIndex()].repaint();
        backwardButton.setEnabled(pageNumberBox.getSelectedIndex() != 0);
        forwardButton.setEnabled(pageNumberBox.getSelectedIndex() != pageNumberBox.getItemCount() - 1);

        previewDialog.validate();
        reforming = false;
    }

    private void updatePreviewPanel() {
        previewPages = new Page[targetPageable.getNumberOfPages()];
        System.gc();
        pageNumberBox.removeAllItems();
        PageFormat pf = pageFormat;
        Dimension size = new Dimension((int) pf.getPaper().getWidth(), (int) pf.getPaper().getHeight());
        if (pf.getOrientation() != PageFormat.PORTRAIT)
            size = new Dimension(size.height, size.width);
        updatePrintable();
        for (int i = 0; i < previewPages.length; i++) {
            pageNumberBox.addItem("" + (i + 1));
            previewPages[i] = new Page(i, size);
            previewPanel.add("" + (i + 1), new JScrollPane(previewPages[i]));
        }
    }

    private void updatePageable() {
        updatePrintable();
        this.targetPageable = new Pageable() {
            public int getNumberOfPages() {
                Graphics g = new java.awt.image.BufferedImage(2, 2, java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
                int n = 0;
                try {
                    while (targetPrintable.print(g, pageFormat, n) ==
                            Printable.PAGE_EXISTS) n++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return n;
            }

            public PageFormat getPageFormat(int x) {
                return pageFormat;
            }

            public Printable getPrintable(int x) {
                return targetPrintable;
            }
        };

    }

    private void updatePrintable() {
        String header = headerTextField != null ? headerTextField.getText().trim() : null;
        final MessageFormat headerFormat = header != null ? new MessageFormat(header) : null;
        final MessageFormat footerFormat = pageNumbering ? new MessageFormat("{0}"): null;
        this.targetPrintable = tableToPrint.getPrintable(JTable.PrintMode.NORMAL, headerFormat, footerFormat);
    }

    private void printPageable() {
        updatePrintable();
        printerJob.defaultPage(pageFormat);
        printerJob.setPrintable(targetPrintable);
        if (printerJob.printDialog()) {
            try {
            printerJob.print();
            printingComplete = true;
            } catch (PrinterException e) {
                printingComplete = false;
                throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.PRINTER_EXCEPTION, e);
            }
        }
    }


    // Page
    class Page extends JLabel {
        final int n;
        final PageFormat pf;
        java.awt.image.BufferedImage bi = null;
        Dimension size = null;

        public Page(int x, Dimension size) {
            this.size = size;
            bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);   // todo java.lang.OutOfMemoryError: Java heap space
            n = x;
            pf = pageFormat;
            Graphics g = bi.getGraphics();
            Color c = g.getColor();
            g.setColor(Color.white);
            g.fillRect(0, 0, (int) pf.getWidth(), (int) pf.getHeight());
            g.setColor(c);
            try {
                g.clipRect(0, 0, (int)pf.getWidth(), (int)pf.getHeight());
                targetPrintable.print(g, pf, n);
            } catch (Exception ex) {
                throw new RuntimeException("Can't draw an image!", ex);
            }
            this.setIcon(new ImageIcon(bi));
            this.repaint();
        }

        public void refreshZoomScale() {
            if (zoomScale != 1.0) {
                this.setIcon(new ImageIcon(bi.getScaledInstance((int) (size.width * zoomScale), (int) (size.height * zoomScale), BufferedImage.SCALE_SMOOTH)));
            }else{
                this.setIcon(new ImageIcon(bi));
            }
            this.validate();
            this.repaint();
        }
    }

}



