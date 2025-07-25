/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.JDialog;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.Date;

/**
 *
 * @author raksha
 */
public class DateSelectionDialog extends JDialog {
    private boolean confirmed = false;
    private LocalDate selectedDate;
    private JSpinner dateSpinner;

    public DateSelectionDialog(JFrame parent, String actionType) {
        super(parent, "Select Date for " + actionType, true); 

        setSize(350, 200);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent); 

        JLabel lbl = new JLabel("Choose date:");
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        JPanel centerPanel = new JPanel();
        centerPanel.add(dateSpinner);

        JButton btnConfirm = new JButton("Confirm");
        JButton btnCancel = new JButton("Cancel");

        btnConfirm.addActionListener(e -> {
            confirmed = true;
            Date date = (Date) dateSpinner.getValue();
            selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dispose();
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JPanel southPanel = new JPanel();
        southPanel.add(btnConfirm);
        southPanel.add(btnCancel);

        add(lbl, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
}

