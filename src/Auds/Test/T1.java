package Auds.Test;

import Lab4.P1.LocalDateTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class T1 {
    public static void main(String[] args) throws ParseException {
        Date time1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").parse("10.12.2013 21:30:15");
        Date time2 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").parse("10.12.2013 21:32:45");
//                LocalDateTime.parse("10.12.2013 21:30:15", DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss"));
//        LocalDateTime time2 = LocalDateTime.parse("10.12.2013 21:32:45", DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss"));

//        System.out.println(time2.getTime() - time1.getTime());


        System.out.println(Math.max(0, 9 - 40));
    }
}
