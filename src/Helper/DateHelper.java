/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Quang
 */
public class DateHelper {

    static final SimpleDateFormat date_formater = new SimpleDateFormat("dd/MM/yyyy");

    public static Date now() {
        return new Date();
    }

    //String to Date
    public static Date toDate(String date, String... pattern) {
        try {
            if (pattern.length > 0) {
                date_formater.applyPattern(pattern[0]);
            }

            if (date == null) {
                return DateHelper.now();
            }

            return date_formater.parse(date);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //Date to String
    public static String toString(Date date, String... pattern) {
        if (pattern.length > 0) {
            date_formater.applyPattern(pattern[0]);
        }

        if (date == null) {
            date = DateHelper.now();
        }

        return date_formater.format(date);
    }

    //show the day after Date n days
    public static Date addDays(Date date, int days) {
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        return date;
    }

    //show the day after now n days
    public static Date add(int days) {
        Date now = DateHelper.now();
        now.setTime(now.getTime() + days * 24 * 60 * 60 * 1000);
        return now;
    }
}
