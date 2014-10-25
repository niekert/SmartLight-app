package krusemost.smartlight.utils;

/**
 * Created by Niek on 10/24/2014.
 *
 * Utility class for general utilities.
 */
public class Utilities {
    public static String formatHHMM(int secondsCount) {
        int seconds = secondsCount % 60;
        secondsCount -= seconds;

        //calculate minutes
        int minutesCount = secondsCount / 60;
        int minutes = minutesCount % 60;
        minutesCount -= minutes;

        int hoursCount = minutesCount / 60;

        return "" + twoDigitString(hoursCount) + ":" + twoDigitString(seconds);
    }

    public static String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}
