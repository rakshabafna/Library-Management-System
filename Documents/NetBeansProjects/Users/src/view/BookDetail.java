/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.DBconnection;
import view.BrowseBooks.Book;
import java.sql.PreparedStatement;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
        
/**
 *
 * @author raksha
 */
public class BookDetail extends javax.swing.JPanel {
    int userId;
    int bookId;
    JPanel mainPanel;
    Book book;
    /**
     * Creates new form BookDetail
     */
    public BookDetail(Book book,int userId,JPanel mainPanel) {
        this.userId=userId;
        this.bookId=book.id;
        this.mainPanel=mainPanel;
        this.book=book;
        initComponents();
        txtTitle.setText(book.title);
        txtAuthor.setText("By: " + book.author);
        txtPrice.setText("₹"+String.format("%.2f", book.price));
        Color outOfStockColor = Color.RED;
        Color availableColor = new Color(34, 139, 34);
        if (book.quantity > 0) {
    txtAvailable.setForeground(availableColor);
} else {
    txtAvailable.setText("Out of Stock");
    txtAvailable.setForeground(outOfStockColor);
}
        ImageIcon icon=getBookIconFromBase64(book.imagebase64);
        txtDescription.setText(book.description);
        Font titleFont = new Font("SansSerif", Font.BOLD, 20);
        Font authorFont = new Font("SansSerif", Font.ITALIC, 14);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        Font descFont = new Font("SansSerif", Font.BOLD, 13);
        txtTitle.setFont(titleFont);
        txtAuthor.setFont(authorFont);
        txtAvailable.setFont(labelFont);
        txtDescription.setFont(descFont);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setEditable(false);
        txtDescription.setBackground(Color.decode("#EEDEF6"));


        coverImagelbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        coverImagelbl.setOpaque(true);
        //coverImagelbl.setBackground(Color.WHITE);
        coverImagelbl.setPreferredSize(new Dimension(150, 200));
        coverImagelbl.setIcon(getBookIconFromBase64(book.imagebase64));
        coverImagelbl.setBackground(Color.decode("#EEDEF6"));
    
    }





    /*private void buybooks(int bookId,int userId) throws SQLException, Exception{
        try(Connection conn=DBconnection.getConnection()){
            PreparedStatement checkStmt=conn.prepareStatement("SELECT quantity,price from books where id=?");
            //checkStmt.setInt(1,bookId);
            //checkStmt.setInt(2,userId);
            ResultSet rs=checkStmt.executeQuery();
            
            if(rs.next() && rs.getInt("quantity")>0){
                
        }
    }
    */
    


    private void issueBookToUser(int bookId, int userId) {
    try (Connection conn = DBconnection.getConnection()) {

        PreparedStatement checkStmt = conn.prepareStatement("SELECT quantity FROM books WHERE id = ?");
        checkStmt.setInt(1, bookId);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getInt("quantity") > 0) {
            String insertSQL = "INSERT INTO issued_books (user_id, book_id, status, issue_date, return_date) " +
                               "VALUES (?, ?, ?, GETDATE(), NULL)";
            PreparedStatement issueStmt = conn.prepareStatement(insertSQL);
            issueStmt.setInt(1, userId);
            issueStmt.setInt(2, bookId);
            issueStmt.setString(3, "issued");

            int rowsInserted = issueStmt.executeUpdate(); 
            if (rowsInserted > 0) {
                PreparedStatement updateQty = conn.prepareStatement(
                        "UPDATE books SET quantity = quantity - 1 WHERE id = ?");
                updateQty.setInt(1, bookId);
                updateQty.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book issued successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to issue book. No rows inserted.");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Book is currently out of stock.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Unexpected Error: " + e.getMessage());
    }
}


    
    public ImageIcon getBookIconFromBase64(String base64) {
    try {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decodedBytes));

        if (bufferedImage == null) {
            System.err.println("Failed to decode image — bufferedImage is null");
            return new ImageIcon();
        }

        Image scaledImage = bufferedImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    } catch (Exception e) {
        e.printStackTrace();
        return new ImageIcon();
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        txtTitle = new javax.swing.JLabel();
        txtAuthor = new javax.swing.JLabel();
        txtAvailable = new javax.swing.JLabel();
        btnIssue = new javax.swing.JButton();
        coverImagelbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        txtPrice = new javax.swing.JLabel();
        btnback = new javax.swing.JButton();
        btnBuy = new javax.swing.JButton();
        btnReturn = new javax.swing.JButton();
        wishlistIconLabel = new javax.swing.JLabel();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(238, 222, 246));
        setPreferredSize(new java.awt.Dimension(950, 583));

        txtTitle.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        txtTitle.setText("Title");

        txtAuthor.setText("author");

        txtAvailable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtAvailable.setText("available");

        btnIssue.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnIssue.setText("Issue Book");
        btnIssue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssueActionPerformed(evt);
            }
        });

        coverImagelbl.setBackground(new java.awt.Color(255, 153, 255));
        coverImagelbl.setPreferredSize(new java.awt.Dimension(120, 160));

        txtDescription.setEditable(false);
        txtDescription.setBackground(new java.awt.Color(238, 222, 246));
        txtDescription.setColumns(20);
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        txtDescription.setBorder(null);
        jScrollPane1.setViewportView(txtDescription);

        txtPrice.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtPrice.setText("Price");

        btnback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/assets/back (1).png"))); // NOI18N
        btnback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbackActionPerformed(evt);
            }
        });

        btnBuy.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnBuy.setText("Buy");
        btnBuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyActionPerformed(evt);
            }
        });

        btnReturn.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnReturn.setText("Return Book");
        btnReturn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });

        wishlistIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/assets/heart (1) (1).png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReturn)
                .addGap(18, 18, 18)
                .addComponent(btnBuy, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnIssue)
                .addGap(51, 51, 51))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnback))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(coverImagelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 198, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrice)
                            .addComponent(txtAuthor)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAvailable, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(126, 126, 126))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(wishlistIconLabel)
                        .addGap(42, 42, 42))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnBuy, btnIssue, btnReturn});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTitle)
                    .addComponent(btnback)
                    .addComponent(wishlistIconLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAuthor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coverImagelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAvailable)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIssue)
                    .addComponent(btnBuy)
                    .addComponent(btnReturn))
                .addGap(24, 24, 24))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBuy, btnIssue, btnReturn});

    }// </editor-fold>//GEN-END:initComponents

    private void btnIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueActionPerformed
        //System.out.println("Trying to issue bookId: " + bookId + " to userId: " + userId);
        btnIssue.addActionListener(e -> {
    DateSelectionDialog dialog = new DateSelectionDialog(
        (JFrame) SwingUtilities.getWindowAncestor(this),
        "Select Issue Date"
    );

    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
        LocalDate selectedDate = dialog.getSelectedDate();
       JOptionPane.showMessageDialog(this, 
            "Book will be issued on: " + selectedDate);
    }
});

        issueBookToUser(bookId,userId);
    }//GEN-LAST:event_btnIssueActionPerformed

    private void btnbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbackActionPerformed
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new OverviewU(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }//GEN-LAST:event_btnbackActionPerformed

    private void btnBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyActionPerformed
        btnBuy.addActionListener(e -> {
    booksbought boughtPanel = new booksbought(book, userId); 

    JDialog paymentDialog = new JDialog(
        (JFrame) SwingUtilities.getWindowAncestor(this),
        "Confirm Purchase",
        true
    );
    paymentDialog.setSize(600, 600);
    paymentDialog.setLocationRelativeTo(null);
    paymentDialog.getContentPane().add(boughtPanel);
    paymentDialog.setVisible(true);
});



    }//GEN-LAST:event_btnBuyActionPerformed

    private void returnBookInDatabase(int userId, int bookId) {
    String query = "UPDATE issued_books SET return_date = GETDATE(), status = 'returned' " +
                   "WHERE user_id = ? AND book_id = ? AND status = 'issued'";
    String updateQuantityQuery = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";


    try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagement;encrypt=true;trustServerCertificate=true",
            "raksha2", "1234");
         PreparedStatement pst = conn.prepareStatement(query)) {

        pst.setInt(1, userId);
        pst.setInt(2, bookId);
        int rowsUpdated = pst.executeUpdate();

        if (rowsUpdated > 0) {
            try (PreparedStatement pst2 = conn.prepareStatement(updateQuantityQuery)) {
                    pst2.setInt(1, bookId);
                    pst2.executeUpdate();
                }
            JOptionPane.showMessageDialog(null, "Book returned successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Return failed: Already returned");
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database error occurred.");
    }
}

    
    
    
    
    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        int confirm = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to return this book?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            returnBookInDatabase(userId, book.id); 
        }

    }//GEN-LAST:event_btnReturnActionPerformed
     
    
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuy;
    private javax.swing.JButton btnIssue;
    private javax.swing.JButton btnReturn;
    private javax.swing.JButton btnback;
    private javax.swing.JLabel coverImagelbl;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel txtAuthor;
    private javax.swing.JLabel txtAvailable;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JLabel txtPrice;
    private javax.swing.JLabel txtTitle;
    private javax.swing.JLabel wishlistIconLabel;
    // End of variables declaration//GEN-END:variables
}
