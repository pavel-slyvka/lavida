package com.lavida.service.google;

import java.text.DateFormat;
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

        public static Calendar convertStringDateToCalendar(String strDate) {
            Calendar cal = null;

            if (strDate != null) {
                try {
                    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = formatter.parse(strDate);
                    cal = Calendar.getInstance();
                    cal.setTime(date );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return cal;
        }

    }
