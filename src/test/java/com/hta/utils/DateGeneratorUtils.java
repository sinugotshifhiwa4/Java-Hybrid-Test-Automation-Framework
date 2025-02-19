package com.hta.utils;

import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateGeneratorUtils {

    private static final Logger logger = LoggerUtils.getLogger(DateGeneratorUtils.class);
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    // ThreadLocal to ensure thread safety for parallel execution
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_FORMAT));

    public static String getCurrentDate() {
        return SIMPLE_DATE_FORMAT.get().format(new Date());
    }

    public static String getTwoDaysBeforeCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -2); // Subtract 2 days
        return SIMPLE_DATE_FORMAT.get().format(calendar.getTime());
    }

    public static String getDateAfterOneDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Add 1 days to the current date
        return SIMPLE_DATE_FORMAT.get().format(calendar.getTime());
    }

    public static String getDateAfterThreeDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3); // Add 3 days to the current date
        return SIMPLE_DATE_FORMAT.get().format(calendar.getTime());
    }

    public static String getDateAfterTenDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 10); // Add 10 days to the current date
        return SIMPLE_DATE_FORMAT.get().format(calendar.getTime());
    }

    public static String getDateAfterSixDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 6); // Add 6 days to the current date
        return SIMPLE_DATE_FORMAT.get().format(calendar.getTime());
    }

    public static void validateCheckInDate(String checkInDate, String currentDate) {
        try {
            Date checkIn = SIMPLE_DATE_FORMAT.get().parse(checkInDate);
            Date today = SIMPLE_DATE_FORMAT.get().parse(currentDate);

            if (checkIn.before(today)) {
                logger.error("Check-in date {} is in the past! Check-in date cannot be in the past.", checkInDate);
                throw new AssertionError("Check-in date " + checkInDate + " is in the past!");
            }
        } catch (ParseException error) {
            throw new RuntimeException("Error parsing dates: " + error.getMessage());
        }
    }

}
