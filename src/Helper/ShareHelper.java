/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import Model.Employee;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

/**
 *
 * @author Quang
 */
public class ShareHelper {

    public static Employee user = null;

    public static Image appIcon() {
        URL url = ShareHelper.class.getResource("/Icons/Hinh/fpt.png");
        return new ImageIcon(url).getImage();
    }

    public static boolean saveLogo(File file) {
        File dir = new File("C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\logos",file.getName());

//        if (!dir.exists()) {
//            dir.mkdirs();
//        }

        File newFile = new File("C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\logos", file.getName());
//        System.out.println(dir.getAbsolutePath());
//        System.out.println(newFile.getAbsolutePath());

        try {
            Path source = Paths.get(file.getAbsolutePath());
            Path destination = Paths.get(newFile.getAbsolutePath());
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
//            System.out.println(source);
//            System.out.println(destination);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static ImageIcon readLogo(String fileName) {
        File path = new File("C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\logos", fileName);
        return new ImageIcon(path.getAbsolutePath());
    }

    public static void logoff() {
        ShareHelper.user = null;
    }

    public static boolean authenticated() {
        return ShareHelper.user != null;
    }
    
    public static boolean isManager() {
        return ShareHelper.authenticated() && !user.isVaiTro();
    }
}
