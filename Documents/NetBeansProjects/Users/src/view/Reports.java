/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.*;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author raksha
 */
public class Reports extends javax.swing.JPanel {

    /**
     * Creates new form Reports
     */
    public Reports() {
        initComponents();
        loadIssuedBooksReport("Daily");
        loadPopularBooksChart();
          }
    
    
    
   private void loadIssuedBooksReport(String type) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String query = "";

    // Choose SQL based on type
    switch (type) {
        case "Daily":
            query = "SELECT CAST(issue_date AS DATE) AS issued_day, COUNT(*) AS total " +
                    "FROM issued_books WHERE status = 'issued' " +
                    "GROUP BY CAST(issue_date AS DATE) ORDER BY issued_day DESC";
            break;
        case "Weekly":
            query = "SELECT DATENAME(WEEKDAY, issue_date) AS day_name, COUNT(*) AS total " +
                    "FROM issued_books " +
                    "WHERE status = 'issued' " +
                    "AND DATEPART(WEEK, issue_date) = DATEPART(WEEK, GETDATE()) " +
                    "AND YEAR(issue_date) = YEAR(GETDATE()) " +
                    "GROUP BY DATENAME(WEEKDAY, issue_date), DATEPART(WEEKDAY, issue_date) " +
                    "ORDER BY DATEPART(WEEKDAY, issue_date)";
            break;
        case "Monthly":
            query = "SELECT FORMAT(issue_date, 'yyyy-MM') AS month_year, COUNT(*) AS total " +
                    "FROM issued_books WHERE status = 'issued' " +
                    "GROUP BY FORMAT(issue_date, 'yyyy-MM') ORDER BY month_year DESC";
            break;
    }

    try (Connection conn = DriverManager.getConnection(
             "jdbc:sqlserver://localhost:1433;databaseName=LibraryManagement;encrypt=true;trustServerCertificate=true",
             "raksha2", "1234");
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        if (type.equals("Weekly")) {
            // Show all 7 days even if not present in DB
            Map<String, Integer> weeklyMap = new LinkedHashMap<>();
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            for (String day : days) {
                weeklyMap.put(day, 0);  // Default count is 0
            }

            while (rs.next()) {
                String day = rs.getString("day_name").trim();
                int count = rs.getInt("total");
                weeklyMap.put(day, count);  // Overwrite actual counts
            }

            for (Map.Entry<String, Integer> entry : weeklyMap.entrySet()) {
                dataset.addValue(entry.getValue(), "Issued Books", entry.getKey());
            }

        } else {
            while (rs.next()) {
                String label = rs.getString(1).trim(); // For daily: issued_day, monthly: month_year
                int count = rs.getInt("total");
                dataset.addValue(count, "Issued Books", label);
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load report data.");
        return;
    }

    // Chart rendering
    JFreeChart chart = ChartFactory.createBarChart(
        "Books Issued - " + type,
        type.equals("Daily") ? "Date" : (type.equals("Weekly") ? "Day of Week" : "Month"),
        "Number of Books",
        dataset,
        PlotOrientation.VERTICAL,
        false, true, false
    );

    ChartPanel cp = new ChartPanel(chart);
    cp.setPreferredSize(new Dimension(300, 300));

    chartPanel.removeAll();
    chartPanel.setLayout(new BorderLayout());
    chartPanel.add(cp, BorderLayout.CENTER);
    chartPanel.validate();
}

    
    private void loadPopularBooksChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String query = "SELECT b.title, COUNT(*) AS times_issued " +
                   "FROM issued_books ib " +
                   "JOIN books b ON ib.book_id = b.id " +
                   "WHERE ib.status = 'issued' " +
                   "GROUP BY b.title " +
                   "ORDER BY times_issued DESC";

    try (Connection conn = DriverManager.getConnection(
             "jdbc:sqlserver://localhost:1433;databaseName=LibraryManagement;encrypt=true;trustServerCertificate=true",
             "raksha2", "1234");
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String title = rs.getString("title");
            int count = rs.getInt("times_issued");
            dataset.addValue(count, "Times Issued", title);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load popular books data.");
        return;
    }

    JFreeChart chart = ChartFactory.createBarChart(
        "Most Popular Books",
        "Book Title",
        "Times Issued",
        dataset,
        PlotOrientation.VERTICAL,
        false, true, false
    );

    ChartPanel cp = new ChartPanel(chart);
    cp.setPreferredSize(new Dimension(300, 300));

    pop.removeAll();
    pop.setLayout(new BorderLayout());
    pop.add(cp, BorderLayout.CENTER);
    pop.validate();
}


    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        reportTypeComboBox = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        hello = new javax.swing.JPanel();
        chartPanel = new javax.swing.JPanel();
        pop = new javax.swing.JPanel();

        setBackground(new java.awt.Color(238, 222, 246));
        setPreferredSize(new java.awt.Dimension(950, 583));

        jPanel1.setBackground(new java.awt.Color(47, 47, 47));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Reports");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(356, 356, 356)
                .addComponent(jLabel1)
                .addContainerGap(528, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        reportTypeComboBox.setBackground(new java.awt.Color(204, 229, 227));
        reportTypeComboBox.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        reportTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", " " }));
        reportTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                reportTypeComboBoxItemStateChanged(evt);
            }
        });
        reportTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportTypeComboBoxActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        hello.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout popLayout = new javax.swing.GroupLayout(pop);
        pop.setLayout(popLayout);
        popLayout.setHorizontalGroup(
            popLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        popLayout.setVerticalGroup(
            popLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(reportTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(hello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(551, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(72, 72, 72))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(reportTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(pop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(chartPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(120, 120, 120)
                .addComponent(hello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void reportTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_reportTypeComboBoxItemStateChanged
String selectedType = reportTypeComboBox.getSelectedItem().toString();
loadIssuedBooksReport(selectedType);

    }//GEN-LAST:event_reportTypeComboBoxItemStateChanged

    private void reportTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportTypeComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chartPanel;
    private javax.swing.JPanel hello;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel pop;
    private javax.swing.JComboBox<String> reportTypeComboBox;
    // End of variables declaration//GEN-END:variables
}
