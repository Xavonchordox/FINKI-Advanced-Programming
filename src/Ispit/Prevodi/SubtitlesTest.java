package Ispit.Prevodi;

import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Subtitle{
    private int number;
    private String start;
    private String end;
    private List<String> text;

    public Subtitle(int number, String start, String end, List<String> text) {
        this.number = number;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public List<String> getText() {
        return text;
    }

    public void shift(int ms){
        int startInMs = timeToMs(start);
        int endInMs = timeToMs(end);
        this.start = timeToString(startInMs + ms);
        this.end = timeToString(endInMs + ms);
    }

    private int timeToMs(String time) {
        String[] parts1 = time.split(":");
        int hours = Integer.parseInt(parts1[0]) * 60 * 60 * 1000;
        int min = Integer.parseInt(parts1[1]) * 60 * 1000;
        String[] parts2 = parts1[2].split(",");
        int sec = Integer.parseInt(parts2[0]) * 1000;
        int ms = Integer.parseInt(parts2[1]);

        return hours + min + sec + ms;
    }

    private String timeToString(int time) {
        int hours = time / 60 / 60 / 1000;
        time %= (60*60*1000);
        int min = time / 60 / 1000;
        time %= (60*1000);
        int sec = time / 1000;
        time %= 1000;
        int ms = time;

        return String.format("%02d:%02d:%02d,%03d", hours, min, sec, ms);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(number).append("\n").append(start).append(" --> ").append(end).append("\n");
        text.forEach(t -> sb.append(t).append("\n"));
        return sb.toString();
    }
}

//1
//00:00:43,700 --> 00:00:47,973
//Come on ladies, we're pushing pennies
//around like a lot of old 'tards here.

class Subtitles {
    List<Subtitle> subtitles;

    public Subtitles() {
        subtitles = new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream) {
        Scanner in = new Scanner(System.in);

        while (in.hasNextLine()){
            int num = Integer.parseInt(in.nextLine());

            String line = in.nextLine();
            String [] parts = line.split("\\s+");
            String startTime = parts[0];
            String endTime = parts[2];

            List<String> text = new ArrayList<>();
            while(in.hasNextLine()){
                String t = in.nextLine();
                if (t.equals(""))
                    break;

                text.add(t);
            }

            subtitles.add(new Subtitle(num, startTime, endTime, text));
        }
        return subtitles.size();
    }

    public void print() {
        subtitles.forEach(System.out::println);
    }

    public void shift(int ms) {
        subtitles.forEach(i -> i.shift(ms));
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
