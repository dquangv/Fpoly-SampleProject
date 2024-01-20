/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package View;

import Controller.EmployeeDAO;
import Helper.ShareHelper;
import java.security.SecureRandom;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import javax.activation.DataHandler;

/**
 *
 * @author Quang
 */
public class ForgotPassword extends javax.swing.JDialog {

    EmployeeDAO dao = new EmployeeDAO();
    public static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";

    /**
     * Creates new form ForgotPassword
     */
    public ForgotPassword(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(ShareHelper.appIcon());
    }

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Chọn một ký tự ngẫu nhiên từ mỗi loại ký tự
        password.append(LOWERCASE_CHARS.charAt(random.nextInt(LOWERCASE_CHARS.length())));
        password.append(UPPERCASE_CHARS.charAt(random.nextInt(UPPERCASE_CHARS.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Chọn 2 ký tự ngẫu nhiên từ tất cả các loại ký tự để tổng cộng 6 ký tự
        for (int i = 0; i < 2; i++) {
            String allChars = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGITS + SPECIAL_CHARS;
            int randomIndex = random.nextInt(allChars.length());
            password.append(allChars.charAt(randomIndex));
        }

        // Trộn ngẫu nhiên các ký tự để đảm bảo thứ tự không định
        String shuffledPassword = shuffleString(password.toString());
        return shuffledPassword;
    }

    // Hàm trộn ngẫu nhiên chuỗi
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    public void getMatKhau() {
        String taiKhoan = txtTenDN.getText();
        String toEmail = txtEmail.getText();
//        String ccEmail = txtCCEmail.getText();
//        String bccEmail = txtBCCEmail.getText();
//        String subject = txtSubject.getText();
//        String message = txtMessage.getText();

        if (dao.findById(taiKhoan) == null) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không tồn tại!");
            return;
        }

        if (dao.selectPasswordByEmail(toEmail, taiKhoan) == null) {
            JOptionPane.showMessageDialog(this, "Email không khớp với tài khoản đã đăng ký!");
            return;
        }

        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.ssl.protocols", "TLSv1.2");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", 587);
        p.put("mail.debug", "true");

//        String accountName = txtUsername.getText();
//        String accountPass = new String(txtPassword.getPassword());
        String accountName = "bobvu7799@gmail.com";
        String accountPass = "rvdcqfzztbbniwsp";

        Session session = Session.getInstance(p, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(accountName, accountPass);
//                  rvdcqfzztbbniwsp (bobvu7799)
//                  puzqujlhitmyceos (vudangquang7799)
            }
        });

        try {
            String newPassword = generatePassword();
            String message = "Mật khẩu mới của bạn là: " + newPassword;
            Model.Employee emp = dao.select("select * from nhanvien where manhanvien = ?", taiKhoan).get(0);
            System.out.println(emp);
            emp.setMatKhau(newPassword);
            dao.update(emp);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(accountName));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));
//            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccEmail));
            msg.setSubject("EDUSYS - LẤY LẠI MẬT KHẨU");

            MimeBodyPart textPart = new MimeBodyPart();

            textPart.setText(message);

//            MimeBodyPart attachmentPart = new MimeBodyPart();
//            String filePath = selectedFile.getAbsolutePath();
//            FileDataSource fileDataSource = new FileDataSource(filePath);
//            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
//            attachmentPart.setFileName(fileDataSource.getName());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
//            multipart.addBodyPart(attachmentPart);

            msg.setContent(multipart);

            Transport.send(msg);

            JOptionPane.showMessageDialog(this, "Mật khẩu mới đã được gửi đi. Vui lòng kiểm tra email!");
        } catch (Exception ex) {
            ex.printStackTrace();
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
        jLabel2 = new javax.swing.JLabel();
        txtTenDN = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        btnLayMK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUÊN MẬT KHẨU");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("QUÊN MẬT KHẨU");

        jLabel2.setText("Tên đăng nhập");

        jLabel3.setText("Email");

        btnLayMK.setText("Lấy mật khẩu");
        btnLayMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayMKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(txtTenDN)
                    .addComponent(jLabel3)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnLayMK)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTenDN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLayMK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLayMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayMKActionPerformed
        getMatKhau();
    }//GEN-LAST:event_btnLayMKActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ForgotPassword dialog = new ForgotPassword(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLayMK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtTenDN;
    // End of variables declaration//GEN-END:variables
}
