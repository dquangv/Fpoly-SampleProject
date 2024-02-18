/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package View;

import Controller.CourseDAO;
import Controller.LearnerDAO;
import Controller.StudentDAO;
import Controller.ThematicDAO;
import Helper.DateHelper;
import Helper.DialogHelper;
import Helper.JdbcHelper;
import Helper.ShareHelper;
import static View.ForgotPassword.generatePassword;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Quang
 */
public class Student extends javax.swing.JDialog {

    int indexLearner = -1;
    int indexStudent = -1;

    public Integer maKH;
    ThematicDAO cddao = new ThematicDAO();
    StudentDAO dao = new StudentDAO();
    LearnerDAO nhdao = new LearnerDAO();
    CourseDAO khdao = new CourseDAO();

    /**
     * Creates new form Student1
     */
    public Student(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    public void init() {
        setIconImage(ShareHelper.appIcon());
        setLocationRelativeTo(null);
    }

    public void fillComboBoxChuyenDe() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboThematic.getModel();

        model.removeAllElements();

        try {
            List<Model.Thematic> list = cddao.select();

            for (Model.Thematic cd : list) {
                model.addElement(cd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fillComboBoxKhoaHoc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboCourse.getModel();

        model.removeAllElements();

        Model.Thematic cd = (Model.Thematic) cboThematic.getSelectedItem();

        if (cd != null) {
            List<Model.Course> list = khdao.findByChuyenDe(cd.getMaCD());

            for (Model.Course kh : list) {
                model.addElement(kh);
            }

            this.fillTableHocVien();
        }
    }

    public void fillTableHocVien() {
        DefaultTableModel model = (DefaultTableModel) tblStudent.getModel();

        model.setRowCount(0);

        Model.Course kh = (Model.Course) cboCourse.getSelectedItem();

        if (kh != null) {
            maKH = kh.getMaKH();

            try {
                String sql = "select hv.*, nh.hovaten from hocvien hv join nguoihoc nh on hv.manguoihoc = nh.manguoihoc where makhoahoc = ?;";
                ResultSet rs = JdbcHelper.executeQuery(sql, maKH);
                int stt = 1;

                while (rs.next()) {
                    double diem = rs.getDouble("diemtrungbinh");
                    Object[] row = {stt, rs.getInt("mahocvien"), rs.getString("manguoihoc"), rs.getString("hovaten"), diem};

                    model.addRow(row);
                    stt++;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void fillTableNguoiHoc() {
        DefaultTableModel model = (DefaultTableModel) tblLearner.getModel();

        model.setRowCount(0);

        try {
            String keyword = txtSearch.getText();
            Model.Course kh = (Model.Course) cboCourse.getSelectedItem();

            List<Model.Learner> list = nhdao.selectByCourse(kh.getMaKH(), keyword);

            for (Model.Learner nh : list) {
                Object[] row = {nh.getMaNH(), nh.getHoTen(), nh.isGioiTinh() ? "Nam" : "Nữ", DateHelper.toString(nh.getNgaySinh()), nh.getDienThoai(), nh.getEmail()};
                model.addRow(row);
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

//    public void insert() {
//        Model.Learner learner = (Model.Learner) cboThematic.getSelectedItem();
//        Model.Student model = new Model.Student();
//
//        model.setMaKH(maKH);
//        model.setMaNH(learner.getMaNH());
////        model.setDiem(Double.parseDouble(txtDiem.getText()));
//
//        try {
//            dao.insert(model);
//            this.fillComboBox();
//            this.fillTable();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    public void update() {
//        for (int i = 0; i < tblStudent.getRowCount() - 1; i++) {
//            Integer maHV = (Integer) tblStudent.getValueAt(i, 0);
//            String maNH = (String) tblStudent.getValueAt(i, 1);
//            Double diem = (Double) tblStudent.getValueAt(i, 3);
//            Boolean isDelete = (Boolean) tblStudent.getValueAt(i, 4);
//
//            try {
//                if (isDelete) {
//                    dao.delete(maHV);
//                } else {
//                    Model.Student model = new Model.Student();
//                    model.setMaHV(maHV);
//                    model.setMaKH(maKH);
//                    model.setMaNH(maNH);
//                    model.setDiem(diem);
//
//                    dao.update(model);
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        this.fillComboBox();
//        this.fillTable();
//
//        DialogHelper.alert(this, "Cập nhật thành công!");
    }

    public void addHocVien() {
//        Course kh =  (Course) cboCourse.getSelectedItem();
//        int[] rows = tblLearner.getSelectedRows();
//        for (int row : rows) {
//            String manh = (String) tblLearner.getValueAt(row, 0);
//            Model.Student hv = new Model.Student();
//            hv.setMaKH(k());
//            hv.setDiem(0);
//            hv.setMaNH(manh);
//            hvDAO.insert(hv);
//        }
//        this.fillTableHocVien();
//        this.fillTableNguoiHoc();

        if (indexLearner == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học viên");
        } else {
            List<Model.Student> list = dao.select();
            int maHVCuoiCung = list.get(list.size() - 1).getMaHV();

            Model.Course kh = (Model.Course) cboCourse.getSelectedItem();
            int maKH = kh.getMaKH();
            String maNH = (String) tblLearner.getValueAt(indexLearner, 0);

            Model.Student student = new Model.Student(maHVCuoiCung + 1, maKH, maNH);

            try {
                dao.insert(student);
                fillTableHocVien();
                fillTableNguoiHoc();
                tabs.setSelectedIndex(0);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void selectLearner() {
        indexLearner = tblLearner.getSelectedRow();
    }

    public void selectStudent() {
        indexStudent = tblStudent.getSelectedRow();
    }

    public void deleteStudent() {
        if (indexStudent == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên");
        } else {
            int maHV = (int) tblStudent.getValueAt(indexStudent, 1);

            try {
                int confirm = JOptionPane.showConfirmDialog(this, "Chắc chắn xoá?");

                if (confirm == JOptionPane.YES_OPTION) {
                    dao.delete(maHV);
                    fillTableHocVien();
                    fillTableNguoiHoc();
                    JOptionPane.showMessageDialog(this, "Đã xoá sinh viên khỏi khoá học");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateMark() {
        if (indexStudent == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên");
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, "Chắc chắn cập nhật?");
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = 0; i < tblStudent.getRowCount(); i++) {
                    int maHV = (int) tblStudent.getValueAt(i, 1);
                    Model.Student student = dao.findById(maHV);
//                double diem = (double) tblStudent.getValueAt(i, 4);
//                System.out.println(diem);
                    student.setDiem((double) tblStudent.getValueAt(i, 4));

                    try {
                        dao.update(student);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Đã cập nhật điểm thành công");
        }
    }

    public void guiKQ() {
        int check = 0;

        for (int i = 0; i < tblStudent.getRowCount(); i++) {
            int maHV = (int) tblStudent.getValueAt(i, 1);
            Model.Student student = dao.findById(maHV);
            double diem = student.getDiem();

            if (diem == -1) {
                check++;
                JOptionPane.showMessageDialog(this, "Vui lòng cập nhật điểm của tất cả sinh viên trong khoá học trước khi gửi kết quả");
                break;
            }
        }

        if (check == 0) {
            Model.Course kh = (Model.Course) cboCourse.getSelectedItem();
            List<Model.Student> list = dao.findByKhoaHoc(kh.getMaKH());

            for (Model.Student student : list) {
                sendMail(student);
            }

            JOptionPane.showMessageDialog(this, "Đã gửi kết quả thành công");
        }
    }

    public void sendMail(Model.Student student) {
//        String taiKhoan = txtTenDN.getText();
//        String toEmail = txtEmail.getText();
//        String ccEmail = txtCCEmail.getText();
//        String bccEmail = txtBCCEmail.getText();
//        String subject = txtSubject.getText();

        if (student.getDiem() >= 5) {
            Model.Learner nguoiHoc = nhdao.findById(student.getMaNH());
            String toEmail = nguoiHoc.getEmail();

            String message = "Kính gửi " + nguoiHoc.getHoTen() + ",\n"
                    + "\n"
                    + "Chúc mừng bạn đã hoàn thành khoá học " + cboThematic.getSelectedItem() + " tại Trung tâm Tin Học Văn Phòng Polypro! Chúng tôi rất vui mừng và tự hào về sự nỗ lực và cam kết của bạn trong suốt thời gian học.\n"
                    + "\n"
                    + "Bạn đã thể hiện sự chuyên nghiệp và sự quyết tâm trong việc hoàn thành các bài tập và dự án, và chúng tôi tin rằng những kiến thức và kỹ năng mà bạn đã học sẽ giúp bạn thành công không chỉ trong lĩnh vực học tập mà còn trong sự nghiệp và cuộc sống cá nhân.\n"
                    + "\n"
                    + "Chúng tôi hy vọng rằng bạn đã tận hưởng và học hỏi được nhiều điều từ khoá học của chúng tôi. Đồng thời, chúng tôi cũng mong muốn được gặp lại bạn trong các khoá học tương lai hoặc trong bất kỳ dịp nào khác tại Trung tâm Tin Học Văn Phòng.\n"
                    + "\n"
                    + "Nếu bạn có bất kỳ câu hỏi hoặc yêu cầu hỗ trợ nào, xin đừng ngần ngại liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.\n"
                    + "\n"
                    + "Một lần nữa, chúc mừng và cảm ơn bạn đã là một phần của cộng đồng học viên của chúng tôi.\n"
                    + "\n"
                    + "Trân trọng,\n"
                    + "Trung tâm Tin Học Văn Phòng Polypro.";

//        if (dao.findById(taiKhoan) == null) {
//            JOptionPane.showMessageDialog(this, "Tên đăng nhập không tồn tại!");
//            return;
//        }
//
//        if (dao.selectPasswordByEmail(toEmail, taiKhoan) == null) {
//            JOptionPane.showMessageDialog(this, "Email không khớp với tài khoản đã đăng ký!");
//            return;
//        }
            Properties p = new Properties();
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.smtp.ssl.protocols", "TLSv1.2");
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.port", 587);
            p.put("mail.debug", "true");

//        String accountName = txtUsername.getText();
//        String accountPass = new String(txtPassword.getPassword());
            String accountName = "vudangquang7799@gmail.com";
            String accountPass = "fgzbcgkaumowrjuw";

            Session session = Session.getInstance(p, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(accountName, accountPass);
//                  rvdcqfzztbbniwsp (bobvu7799)
//                  puzqujlhitmyceos (vudangquang7799) - pass sai
//                  fgzbcgkaumowrjuw (vudangquang7799)
                }
            });

            try {
//            String newPassword = generatePassword();
//            String message = "Mật khẩu mới của bạn là: " + newPassword;
//            Model.Employee emp = dao.select("select * from nhanvien where manhanvien = ?", taiKhoan).get(0);
//            System.out.println(emp);
//            emp.setMatKhau(newPassword);
//            dao.update(emp);

                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(accountName));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));
//            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccEmail));
                msg.setSubject("POLYPRO - TRUNG TÂM TIN HỌC VĂN PHÒNG");

                MimeBodyPart textPart = new MimeBodyPart();

                textPart.setText(message);

                MimeBodyPart attachmentPart = new MimeBodyPart();

                String filePath = certification(student).getAbsolutePath();
                FileDataSource fileDataSource = new FileDataSource(filePath);
                attachmentPart.setDataHandler(new DataHandler(fileDataSource));
                attachmentPart.setFileName(fileDataSource.getName());

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

                msg.setContent(multipart);

                Transport.send(msg);

//            JOptionPane.showMessageDialog(this, "Kết quả đã được gửi đi thành công");
//            JOptionPane.showMessageDialog(this, "Mật khẩu mới đã được gửi đi. Vui lòng kiểm tra email!");
//            this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Model.Learner nguoiHoc = nhdao.findById(student.getMaNH());
            String toEmail = nguoiHoc.getEmail();

            String message = "Kính gửi " + nguoiHoc.getHoTen() + ",\n"
                    + "\n"
                    + "Chúng tôi xin thông báo với bạn về kết quả cuối cùng của khoá học " + cboThematic.getSelectedItem() + " mà bạn đã tham gia tại Trung tâm Tin Học Văn Phòng. Đầu tiên, chúng tôi muốn gửi lời cảm ơn sâu sắc đến bạn về sự nỗ lực và cam kết mà bạn đã dành cho khoá học này.\n"
                    + "\n"
                    + "Tuy nhiên, sau quá trình đánh giá kỹ lưỡng, chúng tôi nhận thấy rằng điểm số của bạn (" + student.getDiem() + " điểm) không đạt yêu cầu để nhận được chứng chỉ từ trung tâm. Đây không phải là một tin tức dễ chịu, và chúng tôi hiểu rằng có thể đây là một thách thức đối với bạn.\n"
                    + "\n"
                    + "Xin đừng để điều này làm mất đi niềm đam mê và quyết tâm của bạn. Khoá học đã cung cấp cho bạn cơ hội để học hỏi và phát triển, và những bài học bạn đã học sẽ tiếp tục là một phần quan trọng trong sự phát triển cá nhân và sự nghiệp của bạn trong tương lai.\n"
                    + "\n"
                    + "Chúng tôi muốn khuyến khích bạn tiếp tục học hỏi và phát triển bản thân. Đôi khi, thất bại không phải là điều cuối cùng, mà chỉ là một bước tiếp theo trên con đường của sự thành công. Hãy sử dụng kinh nghiệm này như một động lực để nỗ lực và cố gắng hơn trong những cơ hội tiếp theo.\n"
                    + "\n"
                    + "Nếu bạn có bất kỳ câu hỏi hoặc cần sự hỗ trợ từ chúng tôi, xin đừng ngần ngại liên hệ. Chúng tôi luôn sẵn lòng hỗ trợ bạn trong bất kỳ cách nào mà chúng tôi có thể.\n"
                    + "\n"
                    + "Chúng tôi hy vọng sẽ có cơ hội được hợp tác với bạn trong tương lai.\n"
                    + "\n"
                    + "Trân trọng,\n"
                    + "Nhân viên Trung tâm Tin Học Văn Phòng";

            Properties p = new Properties();
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.smtp.ssl.protocols", "TLSv1.2");
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.port", 587);
            p.put("mail.debug", "true");

            String accountName = "vudangquang7799@gmail.com";
            String accountPass = "fgzbcgkaumowrjuw";

            Session session = Session.getInstance(p, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(accountName, accountPass);
//                  rvdcqfzztbbniwsp (bobvu7799)
//                  puzqujlhitmyceos (vudangquang7799) - pass sai
//                  fgzbcgkaumowrjuw (vudangquang7799)
                }
            });

            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(accountName));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                msg.setSubject("POLYPRO - TRUNG TÂM TIN HỌC VĂN PHÒNG");
                msg.setText(message);

                Transport.send(msg);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public File certification(Model.Student student) {
        File pdfFile = null;
        Document document = new Document(PageSize.A6.rotate(), 10, 10, 0, 10);

        try {
            Model.Course khoaHoc = (Model.Course) cboCourse.getSelectedItem();
            String tenKH = khoaHoc.getMaCD();
            String maNH = student.getMaNH();
            Model.Learner nguoiHoc = nhdao.findById(maNH);
            String hoTen = nguoiHoc.getHoTen();
            Model.Thematic chuyenDe = (Model.Thematic) cboThematic.getSelectedItem();
            String tenCD = chuyenDe.getTenCD();
            Model.Course kh = (Model.Course) cboCourse.getSelectedItem();
            String ngayKG = String.valueOf(kh.getNgayKG());

            // Thay đổi cách tạo PdfWriter để thay vì ghi trực tiếp vào FileOutputStream,
            // chúng ta sẽ sử dụng ByteArrayOutputStream để ghi vào bộ nhớ đệm.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //Tạo đối tượng PDFWriter
//            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\certifications\\" + khoaHoc + "\\" + maNH + ".pdf"));
            PdfWriter.getInstance(document, baos);

            //Mở file để thực hiện ghi
            document.open();

            //Font Chữ
            BaseFont baseFont = BaseFont.createFont("E:\\Full Font\\SVN-Bariol.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font font = new Font(baseFont, 15, Font.BOLD);
//
//            //Tên công ty
//            Paragraph CompName = new Paragraph("REDFOX Cinema",
//                    FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, Font.BOLD, new CMYKColor(0, 255, 30, 0)));
//            
//            //CompName.setSpacingAfter(14f);
//            CompName.setSpacingAfter(14f);
//            document.add(CompName);
//
//            //Decor
//            Paragraph decor = new Paragraph("___________________________________");
//            decor.setSpacingAfter(14f);
//            document.add(decor);
//            
//            //Tên phim
//            Paragraph phimname = new Paragraph("Film:", font);
//            document.add(phimname);
//
//            //Tên phim 2
//            Paragraph tenphim = new Paragraph("Tôi có một khỏe Tôi có một khỏTôi có một khỏTôi có một khỏTôi có một khỏTôi có một khỏ", font);
//            tenphim.setSpacingAfter(8f);
//            document.add(tenphim);
//
//            //Giá tiền
//            Paragraph giave = new Paragraph("Price: " + "200000" + " Vnđ", font);
//            giave.setSpacingAfter(8f);
//            document.add(giave);
//
//            //Định dạng + Phòng chiếu
//            Paragraph dinhdang = new Paragraph("Format: " + "IMDX ", font);
//            dinhdang.add("                   ");
//            dinhdang.add("Screen: " + "1");
//            dinhdang.setSpacingAfter(8f);
//            document.add(dinhdang);
//            //Title Ngày, giờ , Ghế
//            Paragraph title = new Paragraph("Date", font);
//            title.add("                                      ");
//            title.add("Time");
//            title.add("                                  ");
//            title.add("Seat");
//            document.add(title);
//            //Value Ngày, giờ ,ghế
//            Paragraph values = new Paragraph("02-07-2023", font);
//            values.add("                          ");
//            values.add("18:00" + "-" + "20:00");
//            values.add("                       ");
//            values.add("A1");
//            document.add(values);
//
//            document.close();

            Image logo = Image.getInstance("C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\src\\Icons\\Hinh\\fpt.png");
            logo.setAlignment(1);

            Paragraph blank = new Paragraph(" ");
            blank.setSpacingAfter(10f);

            Paragraph schoolName = new Paragraph("Polypro", new Font(baseFont, 15, Font.NORMAL, new CMYKColor(0, 60, 100, 0)));
            schoolName.setAlignment("CENTER");

            Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION", new Font(baseFont, 15, Font.BOLD, new CMYKColor(0, 60, 100, 0)));
            title.setAlignment("CENTER");

            Paragraph us = new Paragraph("WE HEREBY RECOGNIZE", new Font(baseFont, 10, Font.NORMAL));
            us.setAlignment("CENTER");

            Paragraph studentName = new Paragraph(hoTen, new Font(baseFont, 30, Font.BOLD, new CMYKColor(0, 60, 100, 0)));
            studentName.setAlignment("CENTER");
//            studentName.setSpacingBefore(10f);

            Paragraph archievement = new Paragraph("for successfully completing", new Font(baseFont, 10, Font.NORMAL));
            archievement.setAlignment("CENTER");
            archievement.setSpacingBefore(10f);

            Paragraph subject = new Paragraph(tenCD, new Font(baseFont, 15, Font.BOLD, new CMYKColor(0, 60, 100, 0)));
            subject.setAlignment("CENTER");
            subject.setSpacingAfter(10f);

//            Paragraph time = new Paragraph("Given on the <b>02/2024</b>, at the FPT Polytech Ho Chi Minh City College", new Font(baseFont, 10, Font.NORMAL));
//            time.setAlignment("CENTER");
            Chunk timeChunk = new Chunk("Given on ", new Font(baseFont, 10, Font.NORMAL));
            Chunk boldChunk = new Chunk(ngayKG, new Font(baseFont, 10, Font.BOLD));
            Chunk place = new Chunk(", at the FPT Polytech Ho Chi Minh City College", new Font(baseFont, 10, Font.NORMAL));
            Paragraph time = new Paragraph();
            time.setAlignment(1);
            time.add(timeChunk);
            time.add(boldChunk);
            time.add(place);

            document.add(blank);
            document.add(logo);
            document.add(schoolName);
            document.add(title);
            document.add(us);
            document.add(studentName);
            document.add(archievement);
            document.add(subject);
            document.add(time);

            document.close();

            // Sau khi tạo PDF trong bộ nhớ đệm, chúng ta có thể chuyển nó thành một mảng byte
            byte[] pdfBytes = baos.toByteArray();

            // Tạo đường dẫn cho thư mục certifications
            String directoryPath = "C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\certifications\\" + tenKH;

            // Tạo đối tượng File cho thư mục certifications
            File directory = new File(directoryPath);

            // Nếu thư mục không tồn tại, tạo mới
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục và tất cả các thư mục cha cần thiết
            }

            // Tạo đường dẫn cho tệp PDF
            String pdfPath = directoryPath + "\\" + maNH + ".pdf";

            // Tạo một File từ mảng byte này
            pdfFile = new File(pdfPath);

            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(pdfBytes);
            fos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return pdfFile;
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
        jPanel1 = new javax.swing.JPanel();
        cboThematic = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cboCourse = new javax.swing.JComboBox<>();
        tabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStudent = new javax.swing.JTable();
        btnUpdate = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnGuiKQ = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLearner = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("CHUYÊN ĐỀ");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(0, 0, 0)));

        cboThematic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboThematicActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboThematic, 0, 354, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(cboThematic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("KHOÁ HỌC");

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        cboCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCourseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboCourse, 0, 354, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(cboCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblStudent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "MÃ HV", "MÃ NH", "HỌ TÊN", "ĐIỂM"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStudent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStudentMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblStudent);

        btnUpdate.setText("Cập nhật điểm");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnRemove.setText("Xoá khỏi khoá học");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnGuiKQ.setText("Gửi kết quả");
        btnGuiKQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiKQActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuiKQ)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate)
                    .addComponent(btnRemove)
                    .addComponent(btnGuiKQ))
                .addContainerGap())
        );

        tabs.addTab("HỌC VIÊN", jPanel4);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Tìm kiếm");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearch)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tblLearner.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NH", "HỌ VÀ TÊN", "GIỚI TÍNH", "NGÀY SINH", "ĐIỆN THOẠI", "EMAIL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLearner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLearnerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLearner);

        btnAdd.setText("Thêm vào khoá học");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAdd)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAdd)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        tabs.addTab("NGƯỜI HỌC", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tabs)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(298, 298, 298))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        fillComboBoxChuyenDe();
        fillComboBoxKhoaHoc();
        fillTableHocVien();
        fillTableNguoiHoc();
    }//GEN-LAST:event_formWindowOpened

    private void cboThematicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboThematicActionPerformed
        fillComboBoxKhoaHoc();
        fillTableHocVien();
        fillTableNguoiHoc();
    }//GEN-LAST:event_cboThematicActionPerformed

    private void cboCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCourseActionPerformed
        fillTableHocVien();
        fillTableNguoiHoc();
    }//GEN-LAST:event_cboCourseActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        fillTableNguoiHoc();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        addHocVien();
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblLearnerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLearnerMouseClicked
        selectLearner();
    }//GEN-LAST:event_tblLearnerMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        deleteStudent();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void tblStudentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStudentMouseClicked
        selectStudent();
    }//GEN-LAST:event_tblStudentMouseClicked

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        updateMark();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnGuiKQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiKQActionPerformed
        guiKQ();
    }//GEN-LAST:event_btnGuiKQActionPerformed

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
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Student dialog = new Student(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnGuiKQ;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboCourse;
    private javax.swing.JComboBox<String> cboThematic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblLearner;
    private javax.swing.JTable tblStudent;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
