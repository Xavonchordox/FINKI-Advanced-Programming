package Ispit.Imenik;

import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact implements Comparable<Contact> {
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }

    @Override
    public int compareTo(Contact o) {
        if (name.equals(o.name))
            return number.compareTo(o.number);
        return name.compareTo(o.name);
    }
}

class PhoneBook{
    Set<Contact> contacts;

    public PhoneBook() {
        contacts = new TreeSet<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        for (Contact c : contacts){
            if (c.getNumber().equals(number))
                throw new DuplicateNumberException("Duplicate number: " + number);
        }


        contacts.add(new Contact(name, number));
    }

    public void contactsByNumber(String number) {
        boolean hasAtLeastOne = false;
        for (Contact c : contacts){
            if (c.getNumber().contains(number)) {
                hasAtLeastOne = true;
                System.out.println(c);
            }
        }

        if (!hasAtLeastOne){
            System.out.println("NOT FOUND");
        }
    }

    public void contactsByName(String name) {
        boolean hasAtLeastOne = false;
        for (Contact c : contacts){
            if (c.getName().equals(name)) {
                hasAtLeastOne = true;
                System.out.println(c);
            }
        }

        if (!hasAtLeastOne){
            System.out.println("NOT FOUND");
        }
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

