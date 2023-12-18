package mytunes.BLL.util;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateFormat {
    private Timestamp oldTime;
    private Timestamp currentTime;

    public DateFormat(Timestamp date) {
        this.oldTime = date;
    }

    public String getDate() {
        LocalDateTime timeAdded = oldTime.toLocalDateTime();

        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(timeAdded, currentDateTime);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;

        String added = "for ";
        String ago = " siden";

        if (months > 0)
            return added + months + (months == 1 ? " månede" : " måneder") + ago;
         else if (days > 0)
            return added + days + (days == 1 ? " dag" : " dage") + ago;
         else if (hours > 0)
            return added + hours + (hours == 1 ? " time" : " timer") + ago;
         else if (minutes > 0)
            return added + minutes + (minutes == 1 ? " minut" : " minutter") + ago;
         else
            return "for et minut siden";

    }
}
