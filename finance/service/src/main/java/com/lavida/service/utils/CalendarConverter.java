package com.lavida.service.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created: 18:14 05.08.13
 *
 * @author Ruslan
 */
public class CalendarConverter {

    public static Calendar convertStringToCalendar(String strDate) {
        Calendar cal = null;

        if (strDate != null) {
            try {
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date = formatter.parse(strDate);
                cal = Calendar.getInstance();
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("Не верный формат даты!");  // todo exception
            }
        }

        return cal;
    }

    public static String convertCalendarToString (Calendar calendar) {
        String date = null;
        if (calendar != null) {
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                date = formatter.format(calendar.getTime());
        }

        return date;
    }
}
