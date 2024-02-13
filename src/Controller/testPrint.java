/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.text.Element;

/**
 *
 * @author Quang
 */
public class testPrint {

    public static void main(String[] args) throws IOException {

        Document document = new Document(PageSize.A6.rotate(), 10, 10, 0, 10);
        try {
            //Tạo đối tượng PDFWriter
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\certifications\\TestPRint.pdf"));

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

            Paragraph studentName = new Paragraph("Vũ Đăng Quang", new Font(baseFont, 30, Font.BOLD, new CMYKColor(0, 60, 100, 0)));
            studentName.setAlignment("CENTER");
//            studentName.setSpacingBefore(10f);

            Paragraph archievement = new Paragraph("for successfully completing", new Font(baseFont, 10, Font.NORMAL));
            archievement.setAlignment("CENTER");
            archievement.setSpacingBefore(10f);

            Paragraph subject = new Paragraph("DATA SCIENCE", new Font(baseFont, 15, Font.BOLD, new CMYKColor(0, 60, 100, 0)));
            subject.setAlignment("CENTER");
            subject.setSpacingAfter(10f);

//            Paragraph time = new Paragraph("Given on the <b>02/2024</b>, at the FPT Polytech Ho Chi Minh City College", new Font(baseFont, 10, Font.NORMAL));
//            time.setAlignment("CENTER");
            Chunk timeChunk = new Chunk("Given on ", new Font(baseFont, 10, Font.NORMAL));
            Chunk boldChunk = new Chunk("02/2024", new Font(baseFont, 10, Font.BOLD));
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
