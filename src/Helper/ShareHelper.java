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
        File dir = new File("src\\Icons\\Logos",file.getName());

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
        File path = new File("src\\Icons\\Logos", fileName);
        return new ImageIcon(path.getAbsolutePath());
    }

    public static void logoff() {
        ShareHelper.user = null;
    }

    public static boolean authenticated() {
        return ShareHelper.user != null;
    }
}
