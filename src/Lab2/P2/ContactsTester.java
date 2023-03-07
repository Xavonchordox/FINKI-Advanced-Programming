package Lab2.P2;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

enum Operator {
    VIP,
    ONE,
    TMOBILE
}

abstract class Contact{
    private String date;

    public Contact(String date) {
        this.date = date;
    }

    public boolean isNewerThan(Contact c){
        return this.date.compareTo(c.date) > 0;
    }

    public abstract String getType();

}

class EmailContact extends Contact{
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    String getEmail(){
        return email;
    }

    public String getType(){
        return "Email";
    }

    @Override
    public String toString() {
        return "\"" + email + "\"";
    }
}

class PhoneContact extends Contact{
    private String pNumber;
    private Operator operator;


    public PhoneContact(String date, String pNumber) {
        super(date);
        this.pNumber = pNumber;
        setOperator();
    }

    public String getPhone(){
        return pNumber;
    }

    public void setOperator(){
        String op = getPhone().substring(0,3);
        switch (op){
            case "070":
            case "071":
            case "072":
                operator = Operator.TMOBILE;
                break;
            case "075":
            case "076":
                operator = Operator.ONE;
                break;
            case "077":
            case "078":
                operator = Operator.VIP;
                break;
        }
    }

    public Operator getOperator(){
        return operator;
    }

    public String getType(){
        return "Phone";
    }

    @Override
    public String toString() {
        return "\"" + pNumber + "\"";
    }
}

class Student{
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;

    private Contact[] contacts;
    private int contSize;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contSize=0;
    }

    public void addEmailContact(String date, String email){
        if (contSize == 0){
            contacts = new Contact[1];
            contacts[contSize++] = new EmailContact(date, email);
        }
        else{
            Contact[] temp = new Contact[contSize + 1];
            Contact newEmail = new EmailContact(date, email);
            for (int i = 0; i < contSize; i++) {
                temp[i] = contacts[i];
            }temp[contSize++] = newEmail;
            contacts = temp;
        }
    }

    public void addPhoneContact(String date, String phone){
        if (contSize == 0){
            contacts = new Contact[1];
            contacts[contSize++] = new PhoneContact(date, phone);
        }
        else {
            Contact[] temp = new Contact[contSize + 1];
            Contact newPhone = new PhoneContact(date, phone);
            for (int i = 0; i < contSize; i++) {
                temp[i] = contacts[i];
            }temp[contSize++] = newPhone;
            contacts = temp;
        }
    }

    public Contact[] getEmailContacts(){
        Contact[] temp = new Contact[contSize];
        int n=0;

        for (Contact c : contacts){
            if (c.getType().equals("Email")) {
                temp[n] = c;
                n++;
            }
        }

        Contact[] email = new Contact[n];
        for (int i=0; i<n; i++){
            email[i] = temp[i];
        }

        return email;
    }

    public Contact[] getPhoneContacts(){
        Contact[] temp = new Contact[contSize];
        int n=0;

        for (Contact c : contacts){
            if (c.getType().equals("Phone")){
                temp[n] = c;
                n++;
            }
        }

        Contact[] phone = new Contact[n];
        for (int i=0; i<n; i++){
            phone[i] = temp[i];
        }

        return phone;
    }

    public String getCity(){
        return city;
    }

    public String getFullName(){
        return firstName + lastName;
    }

    public long getIndex(){
        return index;
    }

    public Contact getLatestContact(){
        Contact newest = contacts[0];
        for (int i=1; i<contSize; i++){
            if (!newest.isNewerThan(contacts[i]))
                newest = contacts[i];
        }

        return newest;
    }

    public int getContSize(){
        return contSize;
    }

    @Override
    public String toString() {
        return "{\"ime\":\"" + firstName + "\"" + ", \"prezime\":\"" + lastName + "\"" + ", \"vozrast\":" + age + ", \"grad\":\"" + city + "\"" + ", \"indeks\":" + index + ", " +
                "\"telefonskiKontakti\":" + Arrays.toString(getPhoneContacts()) + ", \"emailKontakti\":" + Arrays.toString(getEmailContacts()) + "}";
    }
}

class Faculty{
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName){
        int count=0;
        for (Student s : students){
            if (s.getCity().equals(cityName))
                count++;
        }

        return count;
    }

    public Student getStudent(long index){
        for (Student s : students){
            if (s.getIndex() == index)
                return s;
        }

        return null;
    }

    public double getAverageNumberOfContacts(){
        double avg=0.0;

        for (Student s : students){
            avg+=s.getContSize();
        }
        return avg/(double) students.length;
    }

    public Student getStudentWithMostContacts(){
        Student s = students[0];

        for (int i=1; i<students.length; i++){
            if (s.getContSize() < students[i].getContSize())
                s = students[i];
            else if (s.getContSize() == students[i].getContSize()) {
                if (s.getIndex() < students[i].getIndex())
                    s = students[i];
            }
        }

        return s;
    }

    @Override
    public String toString() {
        return "{\"fakultet\":\"" + name + "\"" + ", \"studenti\":" + Arrays.toString(students) + "}";
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}