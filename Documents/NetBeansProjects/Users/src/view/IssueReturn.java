/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
/**
 *
 * @author raksha
 */
public class IssueReturn extends javax.swing.JPanel {

    /**
     * Creates new form IssueReturn
     */
    public IssueReturn() {
        initComponents();
        int issuedCount = getCount("issued_books");
        int boughtCount = getCount("books_bought");
        createPieChart(issuedCount, boughtCount);
        loadRecentTransactions();
        customizeRecentTable();

    }
    private void customizeRecentTable() {
    recentTransactionsTable.setBackground(new Color(0xCCE5E3));
    recentTransactionsTable.setShowGrid(true);
    recentTransactionsTable.setGridColor(Color.GRAY);
    recentTransactionsTable.setRowHeight(30);

    JTableHeader header = recentTransactionsTable.getTableHeader();
    header.setFont(new Font("SansSerif", Font.BOLD, 14));
    header.setBackground(new Color(0xA0CFCB)); 
    ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < recentTransactionsTable.getColumnCount(); i++) {
        recentTransactionsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}

    
    private void loadRecentTransactions() {
    String query = 
    "SELECT u.name AS username, b.title, 'Bought' AS action, bb.book_bought_date AS action_date " +
    "FROM books_bought bb " +
    "JOIN users u ON bb.user_id = u.id " +
    "JOIN books b ON bb.book_id = b.id " +
    "UNION " +
    "SELECT u.name AS username, b.title, 'Returned' AS action, ib.return_date AS action_date " +
    "FROM issued_books ib " +
    "JOIN users u ON ib.user_id = u.id " +
    "JOIN books b ON ib.book_id = b.id " +
    "WHERE ib.status = 'returned' " +
    "ORDER BY action_date DESC";



    try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagement;encrypt=true;trustServerCertificate=true",
            "raksha2", "1234");
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Username", "Book Title", "Action", "Date"}, 0
        );

        while (rs.next()) {
            String username = rs.getString("username");
            String title = rs.getString("title");
            String action = rs.getString("action");
            Timestamp timestamp = rs.getTimestamp("action_date");
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(timestamp);

            model.addRow(new Object[]{username, title, action, formattedDate});
        }

        recentTransactionsTable.setModel(model);

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading transactions");
    }
}

    
    private void createPieChart(int issuedCount, int boughtCount) {
    DefaultPieDataset dataset = new DefaultPieDataset();
    dataset.setValue("Issued", issuedCount);
    dataset.setValue("Bought", boughtCount);

    JFreeChart chart = ChartFactory.createPieChart(
        "Books Distribution",  
        dataset,               
        true,                  
        true,
        false
    );

    ChartPanel chartPanel = new ChartPanel(chart);
    piechart.removeAll();                           
    piechart.setLayout(new BorderLayout());         
    piechart.add(chartPanel, BorderLayout.CENTER);  
    piechart.revalidate();                      
    piechart.repaint();
}
    
    
    private int getCount(String tableName) {
    int count = 0;
    String query = "SELECT COUNT(*) FROM " + tableName;

    try (Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagement;encrypt=true;trustServerCertificate=true",
            "raksha2", "1234");
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        if (rs.next()) {
            count = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return count;
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        piechart = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recentTransactionsTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(238, 222, 246));
        setPreferredSize(new java.awt.Dimension(950, 583));

        piechart.setBackground(new java.awt.Color(238, 222, 246));
        piechart.setPreferredSize(new java.awt.Dimension(400, 350));
        piechart.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(47, 47, 47));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Issue/Bought Books");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(258, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(329, 329, 329))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        recentTransactionsTable.setBackground(new java.awt.Color(204, 229, 227));
        recentTransactionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(recentTransactionsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(piechart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(piechart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(341, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel piechart;
    private javax.swing.JTable recentTransactionsTable;
    // End of variables declaration//GEN-END:variables
}
