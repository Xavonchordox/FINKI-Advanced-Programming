package Lab3.P2;

import java.io.*;
import java.util.*;

class Contact {
    private String name;
    private List<String> listPhoneNumbers;

    public Contact(String name, String... phonenumber) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        checkName(name);
        this.name = name;
        checkPhoneNumber(phonenumber);
        listPhoneNumbers = new ArrayList<>();
        listPhoneNumbers.addAll(Arrays.asList(phonenumber));
    }

    private void checkName(String name) throws InvalidNameException {
        if (name.length() < 5 || name.length() > 10) {
            throw new InvalidNameException();
        }

        String[] parts = name.split("");
        for (int i = 0; i < parts.length; i++) {
            if (!Character.isLetterOrDigit(parts[i].charAt(0)))
                throw new InvalidNameException();
        }

    }

    private void checkPhoneNumber(String[] phonenumber) throws MaximumSizeExceddedException, InvalidNumberException {
        if (phonenumber.length > 5)
            throw new MaximumSizeExceddedException();
        for (String p : phonenumber) {
            if (p.length() != 9)
                throw new InvalidNumberException();
            checkPrefix(p);
        }
    }

    private void checkPrefix(String phonenumber) throws InvalidNumberException {
        String prefix = phonenumber.substring(0, 3);
        if (!(prefix.equals("070") || prefix.equals("071") || prefix.equals("072") ||
                prefix.equals("075") || prefix.equals("076") ||
                prefix.equals("077") || prefix.equals("078")))
            throw new InvalidNumberException();

        for (int i=0; i<phonenumber.length(); i++){
            if (!Character.isDigit(phonenumber.charAt(i)))
                throw new InvalidNumberException();
        }
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        List<String> temp = new ArrayList<>(listPhoneNumbers);
        Collections.sort(temp);
        return temp.toArray(new String[0]);
    }

    public void addNumber(String phonenumber) throws InvalidNumberException {
        if (listPhoneNumbers.size() == 5)
            return;
        checkPrefix(phonenumber);

        listPhoneNumbers.add(phonenumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : listPhoneNumbers) {
            sb.append(s + "\n");
        }
        return name + "\n" + listPhoneNumbers.size() + "\n" + sb;
    }
}

class PhoneBook {
    private ArrayList<Contact> contacts;

    public PhoneBook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if (contacts.size() > 250) {
            throw new MaximumSizeExceddedException();
        }
        for (Contact c : contacts) {
            if (c.getName().equals(contact.getName()))
                throw new InvalidNameException();
        }

        contacts.add(contact);

    }

    public Contact getContactForName(String name) {
        for (Contact c : contacts) {
            if (name.equals(c.getName()))
                return c;
        }

        return null;
    }

    public int numberOfContacts() {
        return contacts.size();
    }

    public Contact[] getContacts() {
        Contact[] temp = new Contact[numberOfContacts()];
        for (int i=0; i<contacts.size(); i++){
            temp[i] = contacts.get(i);
        }

        Arrays.sort(temp);
        return temp;
    }

    public boolean removeContact(String name) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getName().equals(name)) {
                Arrays.asList(contacts).remove(contacts.get(i).getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return contacts.toString();
    }

    public static boolean saveAsTextFile(PhoneBook phonebook, String path) throws IOException {
        File file = new File(path);
        if (phonebook == null)
            return false;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(phonebook);
        }
        return false;
    }

    public static PhoneBook loadFromTextFile(String path) throws IOException, InvalidFormatException {
        File file = new File(path);
        if (!file.exists())
            throw new IOException();
        if (!file.canWrite())
            throw new InvalidFormatException();

        PhoneBook p = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            p = (PhoneBook) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        Contact[] cons = new Contact[contacts.size()];
        int i = 0;
        for (Contact c : contacts) {
            for (String number : c.getNumbers()) {
                if (number.startsWith(number_prefix))
                    cons[i++] = c;
            }
        }
        return Arrays.copyOf(cons, i);
    }

    private boolean checkIfPresent(ArrayList<Contact> contacts, Contact contact) {
        for (Contact c : contacts) {
            if (c.equals(contact))
                return true;
        }

        return false;
    }

}

public class PhonebookTester {
    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch (line) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine())
            phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook, text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if (!pb.equals(phonebook)) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine()) {
            String command = jin.nextLine();
            switch (command) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while (jin.hasNextLine()) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        } catch (InvalidNameException e) {
            System.out.println(e.name);
            exception_thrown = true;
        } catch (Exception e) {
        }
        if (!exception_thrown) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = {"And\nrej", "asd", "AAAAAAAAAAAAAAAAAAAAAA", "Ð�Ð½Ð´Ñ€ÐµÑ˜A123213", "Andrej#", "Andrej<3"};
        for (String name : names_to_test) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if (!exception_thrown) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = {"+071718028", "number", "078asdasdasd", "070asdqwe", "070a56798", "07045678a", "123456789", "074456798", "073456798", "079456798"};
        for (String number : numbers_to_test) {
            try {
                new Contact("Andrej", number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if (!exception_thrown)
                System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for (int i = 0; i < nums.length; ++i) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej", nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if (!exception_thrown)
            System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej", getRandomLegitNumber(rnd), getRandomLegitNumber(rnd), getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070", "071", "072", "075", "076", "077", "078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for (int i = 3; i < 9; ++i)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
