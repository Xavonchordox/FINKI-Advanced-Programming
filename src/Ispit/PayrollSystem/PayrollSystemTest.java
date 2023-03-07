package Ispit.PayrollSystem;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

abstract class Employee implements Comparable<Employee> {
    private String id;
    private String level;
    private double rate;

    public Employee(String id, String level, double rate) {
        this.id = id;
        this.level = level;
        this.rate = rate;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f", id, level, calculateSalary());
    }

    abstract double calculateSalary();

    @Override
    public int compareTo(Employee o) {
        return Comparator.comparingDouble(Employee::calculateSalary).thenComparing(Employee::getLevel).reversed().compare(this, o);
    }
}

class HourlyEmployee extends Employee {
    private double hours;
    private double overtime;
    private double regular;
    private double rate;
    private String level;

    public HourlyEmployee(String id, String level, double rate, double hours) {
        super(id, level, rate);
        this.hours = hours;
        this.rate = rate;
        this.level = level;
        overtime = Math.max(0, hours - 40);
        regular = hours - overtime;
    }

    @Override
    double calculateSalary() {
        return regular * rate + overtime * rate * 1.5;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f", regular, overtime);
    }

//    @Override
//    public int compareTo(Employee o) {
//        if (calculateSalary() == o.calculateSalary())
//            return level.compareTo(o.getLevel());
//        return Double.compare(calculateSalary(), o.calculateSalary());
//    }
}

class FreelanceEmployee extends Employee {
    private List<Integer> ticketPoints;
    private double rate;

    private String level;

    public FreelanceEmployee(String id, String level, double rate, List<Integer> ticketPoints) {
        super(id, level, rate);
        this.ticketPoints = ticketPoints;
        this.rate = rate;
        this.level = level;
    }

    @Override
    double calculateSalary() {
        return ticketPoints.stream().mapToInt(i -> i).sum() * rate;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Tickets count: %d Tickets points: %d",
                ticketPoints.size(),
                ticketPoints.stream().mapToInt(i -> i).sum()
        );
    }

//    @Override
//    public int compareTo(Employee o) {
//        if (calculateSalary() == o.calculateSalary())
//            return level.compareTo(o.getLevel());
//        return Double.compare(calculateSalary(), o.calculateSalary());
//    }
}

class EmployeeFactory {

    public static Employee createEmployee(String line, Map<String, Double> hourlyRate, Map<String, Double> ticketRate) {
        String[] parts = line.split(";");
        String type = parts[0];
        String id = parts[1];
        String level = parts[2];

        if (type.equals("H")) {
            double hours = Double.parseDouble(parts[3]);
            return new HourlyEmployee(id, level, hourlyRate.get(level), hours);
        } else {
//            List<Integer> tickets = Arrays.stream(parts).skip(3)
//                    .map(Integer::parseInt)
//                    .collect(Collectors.toList());

            List<Integer> tickets = new ArrayList<>();
            for (int i = 3; i < parts.length; i++) {
                tickets.add(Integer.parseInt(parts[i]));
            }
            return new FreelanceEmployee(id, level, ticketRate.get(level), tickets);
        }
    }
}

class PayrollSystem {
    private Map<String, Double> hourlyRate;
    private Map<String, Double> ticketRate;
    private List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRate, Map<String, Double> ticketRate) {
        this.hourlyRate = hourlyRate;
        this.ticketRate = ticketRate;
        employees = new ArrayList<>();
    }

    public void readEmployees(InputStream inputStream) {
//        Scanner scanner = new Scanner(inputStream);
        employees = new BufferedReader(new InputStreamReader(inputStream)).lines()
                .map(line -> EmployeeFactory.createEmployee(line, hourlyRate, ticketRate))
                .collect(Collectors.toList());
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        Map<String, Set<Employee>> setEmployeesByLevel = new TreeMap<>();

        for (Employee e : employees) {
            if (levels.contains(e.getLevel())) {
                setEmployeesByLevel.putIfAbsent(e.getLevel(), new TreeSet<>());
                setEmployeesByLevel.get(e.getLevel()).add(e);
            }
        }

        return setEmployeesByLevel;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}