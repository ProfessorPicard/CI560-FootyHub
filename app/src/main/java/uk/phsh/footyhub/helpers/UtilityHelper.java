package uk.phsh.footyhub.helpers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import uk.phsh.footyhub.enums.DateTimeType;

/**
 * Singleton helper class for general utilities
 * @author Peter Blackburn
 */
public class UtilityHelper {

    private static UtilityHelper _instance;

    private UtilityHelper() { }

    /**
     * @return The current instance of UtilityHelper
     */
    public static UtilityHelper getInstance() {
        if(_instance == null)
            _instance = new UtilityHelper();
        return _instance;
    }

    /**
     * Formats a given UTC date time string from the rest responses
     * @param dateTimeStr UTC formatted datetime string from rest response
     * @return String Formatted date time string into MMMM dd YYYY hh:mm
     */
    public String DateTimeString(String dateTimeStr, DateTimeType type) {
        DateTimeFormatter dtf = null;

        switch (type) {
            case DATE:
                dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
                break;
            case TIME:
                dtf = DateTimeFormatter.ofPattern("hh:mm a");
                break;
            case DATETIME:
                dtf = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
                break;
        }

        Instant instant = Instant.parse(dateTimeStr);
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, zoneId);

        return dateTime.format(dtf);
    }

    /**
     * Formats a given Epoch time string
     * @param epochTime Epoch time in seconds
     * @return String Formatted date time string into MMMM dd YYYY hh:mm
     */
    public String epochToDateTimeString(String epochTime) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(Long.parseLong(epochTime), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
        return dateTime.format(dtf);
    }
}
