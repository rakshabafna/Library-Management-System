/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import view.BrowseBooks.Book;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import model.DBconnection;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

/**
 *
 * @author raksha
 */
public class booksbought extends javax.swing.JPanel {
    int userId;
    int bookId;
    JPanel mainPanel;

    /**
     * Creates new form
     */
    public booksbought(Book book,int userId) {
        initComponents();
        this.userId=userId;
        this.bookId=book.id;
        this.mainPanel=mainPanel;
        txtDescription.setText(book.description);
        lblTitle.setText(book.title);
        lblAuthor.setText(book.author);
        Pricelbl.setText("₹"+String.format("%.2f", book.price));
        coverImagelbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        coverImagelbl.setOpaque(true);
        coverImagelbl.setPreferredSize(new Dimension(150, 200));
        coverImagelbl.setIcon(getBookIconFromBase64(book.imagebase64));
        coverImagelbl.setBackground(Color.decode("#EEDEF6"));
       
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
    private String extractOrderId(String json) {
    int indexStart = json.indexOf("\"id\":\"") + 6;
    int indexEnd = json.indexOf("\"", indexStart);
    return json.substring(indexStart, indexEnd);
}

    private void launchRazorpayCheckout(String orderId) {
    try {
        File template = new File("src/view/razorpay_template.html");
        BufferedReader reader = new BufferedReader(new FileReader(template));

        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        reader.close();

        String finalHtml = builder.toString().replace("{{ORDER_ID}}", orderId);

        File checkoutFile = new File("src/view/razorpay_checkout.html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(checkoutFile));
        writer.write(finalHtml);
        writer.close();

        RazorpayHttpServer.start();

        Desktop.getDesktop().browse(new URI("http://localhost:8080/razorpay_checkout"));

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error launching Razorpay checkout");
    }
}

    
private String createRazorpayOrder(int amountInPaise) {
    try {
        OkHttpClient client = new OkHttpClient();

        String keyId = "rzp_test_9IpRByFWWERBxo";
        String keySecret = "zl5QX9QGpuN5jkwzRHoKGcb0";
        String auth = keyId + ":" + keySecret;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());

        String json = "{"
            + "\"amount\": " + amountInPaise + ","
            + "\"currency\": \"INR\","
            + "\"receipt\": \"order_rcptid_" + System.currentTimeMillis() + "\""
            + "}";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
            .url("https://api.razorpay.com/v1/orders")
            .post(body)
            .addHeader("Authorization", basicAuth)
            .addHeader("Content-Type", "application/json")
            .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return extractOrderId(responseBody);

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    
    
    private void Pay(int bookId, int userId) {
    try (Connection conn = DBconnection.getConnection()) {

        String checkQuery = "SELECT quantity, price FROM books WHERE id = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
        checkStmt.setInt(1, bookId);
        ResultSet rs = checkStmt.executeQuery();

        int quantity = 0;
        float priceAtPurchase = 0;

        if (rs.next()) {
            quantity = rs.getInt("quantity");
            priceAtPurchase = rs.getFloat("price");

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Out of stock. Cannot buy.");
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        String orderId = createRazorpayOrder((int) (priceAtPurchase * 100));  
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "Failed to create payment order.");
            return;
        }

        launchRazorpayCheckout(orderId);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Have you completed the payment?",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String insertSQL = "INSERT INTO books_bought (user_id, book_id, price_at_purchase, book_bought_date) " +
                               "VALUES (?, ?, ?, GETDATE())";
            PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, bookId);
            insertStmt.setFloat(3, priceAtPurchase);
            insertStmt.executeUpdate();

            PreparedStatement updateQty = conn.prepareStatement(
                "UPDATE books SET quantity = quantity - 1 WHERE id = ?"
            );
            updateQty.setInt(1, bookId);
            updateQty.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book purchased successfully for ₹" + priceAtPurchase);
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error during purchase.");
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

        jLabel1 = new javax.swing.JLabel();
        btnPay = new javax.swing.JButton();
        coverImagelbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        lblTitle = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Pricelbl = new javax.swing.JLabel();

        setBackground(new java.awt.Color(238, 222, 246));
        setPreferredSize(new java.awt.Dimension(881, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel1.setText("I'm Buying...");

        btnPay.setBackground(new java.awt.Color(47, 47, 47));
        btnPay.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setText("Pay");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        coverImagelbl.setPreferredSize(new java.awt.Dimension(120, 160));

        txtDescription.setEditable(false);
        txtDescription.setBackground(new java.awt.Color(238, 222, 246));
        txtDescription.setColumns(20);
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setText("Title");

        lblAuthor.setText("author");

        jLabel3.setText("Price to be paid:");

        Pricelbl.setText("Rs.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(coverImagelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTitle)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAuthor)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Pricelbl)))
                        .addGap(86, 86, 86))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPay)
                .addGap(61, 61, 61))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(21, 21, 21)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAuthor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(coverImagelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(btnPay)
                        .addGap(36, 36, 36))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(Pricelbl))
                        .addGap(147, 147, 147))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        Pay(bookId,userId);
    }//GEN-LAST:event_btnPayActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Pricelbl;
    private javax.swing.JButton btnPay;
    private javax.swing.JLabel coverImagelbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAuthor;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextArea txtDescription;
    // End of variables declaration//GEN-END:variables
}
