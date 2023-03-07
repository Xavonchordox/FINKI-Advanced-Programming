package Ispit.LabVezbi;

import java.util.*;
import java.util.stream.Collectors;

class Student{
    private String index;
    private List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public double sumPoints(){
        return points.stream().mapToInt(i -> i).sum()*1.0 / 10;
    }

    public String getIndex() {
        return index;
    }

    public boolean attendance() {
        if (points.size() < 8)
            return false;
        return true;
    }

    public String attended() {
        if (points.size() < 8)
            return "NO";
        return "YES";
    }

    public int getYear(){
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, attended(), sumPoints());
    }
}

class LabExercises{
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }


    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        if (ascending) {
            students.stream()
                    .sorted(Comparator.comparing(Student::sumPoints).thenComparing(Student::getIndex))
                    .limit(n)
                    .forEach(System.out::println);
        }else {
            students.stream()
                    .sorted(Comparator.comparing(Student::sumPoints).thenComparing(Student::getIndex).reversed())
                    .limit(n)
                    .forEach(System.out::println);
        }
    }

    public List<Student> failedStudents() {
        List<Student> failed = new ArrayList<>();
        for (Student s : students){
            if (!s.attendance())
                failed.add(s);
        }

        return failed.stream()
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::sumPoints))
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        Map<Integer, Double> averagePointsByYear = new TreeMap<>();
        Map<Integer, Integer> counterMap = new TreeMap<>();
        for (Student s : students){
            if (s.attendance()){
                int year = s.getYear();
                averagePointsByYear.putIfAbsent(year, 0.0);
                averagePointsByYear.put(year, averagePointsByYear.get(year) + s.sumPoints());

                counterMap.putIfAbsent(year, 0);
                counterMap.put(year, counterMap.get(year) + 1);
            }
        }

        averagePointsByYear.keySet().forEach(key -> {
            averagePointsByYear.put(key, averagePointsByYear.get(key)/counterMap.get(key));
        });

        return averagePointsByYear;
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}