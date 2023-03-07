package Kol1.DailyTemperatureTest;

import java.io.*;
import java.util.*;

class Temperatures implements Comparable<Temperatures> {
    private int days;
    private char scale;
    private List<Double> temps;

    public Temperatures(int days, char scale, List<Double> temps) {
        this.days = days;
        this.scale = scale;
        this.temps = temps;
    }

    public int getDays() {
        return days;
    }

    public char getScale() {
        return scale;
    }

    public List<Double> getTemps() {
        return temps;
    }

    public int getCount() {
        return temps.size();
    }

    public Double getMin() {
        return temps.stream().min(Double::compareTo).get();
    }

    public Double getMax() {
        return temps.stream().max(Double::compareTo).get();
    }

    public Double getAvg() {
        return temps.stream().mapToDouble(i -> i).sum() / (temps.size() * 1.0);
    }

    public void convertC(){
        for (int i=0; i<temps.size(); i++){
            temps.set(i, ((temps.get(i)-32)*5)/9);
        }
        this.scale = 'C';
    }

    public void convertF(){
        for (int i=0; i<temps.size(); i++){
            temps.set(i, ((temps.get(i)*9)/5)+32);
        }
        this.scale = 'F';
    }


    @Override
    public int compareTo(Temperatures o) {
        return Integer.compare(days, o.days);
    }
}

class DailyTemperatures {
    Set<Temperatures> temperatures;

    public DailyTemperatures() {
        temperatures = new TreeSet<>();
    }

    public void readTemperatures(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            int day = Integer.parseInt(parts[0]);
            char scale = parts[1].charAt(parts[1].length() - 1);
            List<Double> list = new ArrayList<>();

            for (int i = 1; i < parts.length; i++) {
                list.add(Double.parseDouble(parts[i].substring(0, parts[i].length() - 1)));
            }

            temperatures.add(new Temperatures(day, scale, list));
        }
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter writer = new PrintWriter(outputStream);
        String out;
        temperatures.forEach(t -> {
            if (t.getScale() != scale) {
                if (scale == 'F')
                    t.convertF();
                else
                    t.convertC();
            }
            System.out.println(String.format("%3d: Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c",
                    t.getDays(), t.getCount(), t.getMin(), scale, t.getMax(), scale, t.getAvg(), scale));
        });
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde