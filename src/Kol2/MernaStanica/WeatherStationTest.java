package Kol2.MernaStanica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

class Measurement implements Comparable<Measurement> {
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWind() {
        return wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date);
    }

    @Override
    public int compareTo(Measurement o) {
        if (Math.abs(date.getTime() - o.date.getTime()) < 150000)
            return 0;
        return date.compareTo(o.date);
    }
}

class WeatherStation{
    private int days;
    private Set<Measurement> measurements;

    public WeatherStation(int days) {
        this.days = days;
        measurements = new TreeSet<>();
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        Measurement m = new Measurement(temperature, wind, humidity, visibility, date);
        if (measurements.add(m))
            measurements.removeIf(measurement -> date.getTime() - measurement.getDate().getTime() >= days * 86400000L);
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) {
        Predicate<Measurement> inRange = measurement -> !(measurement.getDate().before(from) || measurement.getDate().after(to));
        double d = measurements.stream().filter(inRange).mapToDouble(m -> m.getTemperature()).average().orElse(0);

        if (d == 0)
            throw  new RuntimeException();

        measurements.stream().filter(inRange).forEach(i -> System.out.println(i));
        System.out.println(String.format("Average temperature: %.2f", d));
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde