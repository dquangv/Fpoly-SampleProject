/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import Model.Employee;
import java.awt.Image;
import java.io.File;
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

    public static final Image appIcon;
    public static Employee user = null;

    static {
        String file = "C:\\Users\\Quang\\OneDrive - FPT Polytechnic\\Desktop\\fpl\\hk4\\duanmau\\official\\lab\\Polypro\\src\\Icons\\Hinh\\fpt.png";
        appIcon = new ImageIcon(ShareHelper.class.getResource(file)).getImage();
    }

    public static boolean saveLogo(File file) {
        File dir = new File("Logos");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File newFile = new File(dir, file.getName());

        try {
            Path source = Paths.get(file.getAbsolutePath());
            Path destination = Paths.get(newFile.getAbsolutePath());
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static ImageIcon readLogo(String fileName) {
        File path = new File("logos", fileName);
        return new ImageIcon(path.getAbsolutePath());
    }

    public static void logoff() {
        ShareHelper.user = null;
    }

    public static boolean authenticated() {
        return ShareHelper.user != null;
    }
}
