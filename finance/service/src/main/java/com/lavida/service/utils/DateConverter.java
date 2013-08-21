package com.lavida.service.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created: 20:51 15.08.13
 * The converter for Date to String and  vice versa.
 * @author Ruslan
 */
public class DateConverter {

    public static String convertDateToString (Date date) {
        String strDate = null;
        if (date != null) {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            strDate = formatter.format(date);
        }
        return strDate;
    }

    public static Date convertStringToDate (String strDate) throws ParseException {
        Date date = null;
        if (strDate != null) {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            date = formatter.parse(strDate);
        }
        return date;
    }
}
