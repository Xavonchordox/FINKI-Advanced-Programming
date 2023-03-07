package Kol2.Audition;

import java.util.*;

class Candidate{
    private String city;
    private String code;
    private String name;
    private int age;

    public Candidate(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition{
    private Map<String,Set<Candidate>> candidates;

    public Audition() {
        candidates = new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        Candidate c = new Candidate(city, code, name, age);
        candidates.putIfAbsent(city, new TreeSet<>(Comparator.comparing(Candidate::getCode)));
        candidates.get(city).add(c);
    }

    public void listByCity(String city) {
        candidates.get(city)
                .stream()
                .sorted(Comparator.comparing(Candidate::getName)
                        .thenComparing(Candidate::getAge))
                .forEach(i -> System.out.println(i.toString()));
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}