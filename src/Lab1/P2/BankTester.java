package Lab1.P2;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

class Account implements Cloneable{
    private String name;
    private long id;
    private String balance;

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance.substring(0, balance.length()-1);
        this.id = new Random().nextLong();
        while (this.id < 0){
            this.id = new Random().nextLong();
        }
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    //toString

    @Override
    public String toString() {
        DecimalFormat pattern = new DecimalFormat("0.00");
        return "Name: " + name + '\n' + "Balance: " + pattern.format(Double.parseDouble(balance)) + "$" + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(name, account.name) && Objects.equals(balance, account.balance);
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, balance);
    }
}

abstract class Transaction{
    private final long fromId;
    private final long toId;
    private final String description;
    private final String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount.substring(0, amount.length()-1);
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        DecimalFormat pattern = new DecimalFormat("0.00");
        return pattern.format(Double.parseDouble(amount)) + "$";
    }

    public String getDescription() {
        return description;
    }

    public abstract double getProvisions();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId && toId == that.toId && Objects.equals(description, that.description) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private final String flatAmount;
    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatAmount) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatAmount = flatAmount.substring(0, flatAmount.length()-1);
    }

    @Override
    public double getProvisions() {
        return Double.parseDouble(flatAmount);
    }

    public double getFlatProvision() {
        return Double.parseDouble(flatAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Objects.equals(flatAmount, that.flatAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatAmount);
    }
}

class FlatPercentProvisionTransaction extends Transaction{
    private final int flatPercent;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int flatPercent) {
        super(fromId, toId, "FlatPercent", amount);
        this.flatPercent = flatPercent;
    }

    @Override
    public double getProvisions() {
        String temp = getAmount().replaceAll("\\$","");
        double amount = Double.parseDouble(temp);
        double finalAmount = (int)amount*flatPercent;

        return finalAmount/(100.0);
    }

    public int getFlatPercent() {
        return flatPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return flatPercent == that.flatPercent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatPercent);
    }
}

class Bank{
    private Account accounts[];
    private String name;
    private double transfers;
    private double provisions;

    public Bank(String name, Account[] accounts) {
        this.accounts = Arrays.copyOf(accounts, accounts.length);
        this.name = name;
        this.transfers = 0.0;
        this.provisions = 0.0;
    }

    private Account findUser(long fromId){
        for (Account account : accounts){
            if(account.getId() == fromId)
                return account;
        }

        return null;
    }

    public boolean makeTransaction(Transaction t){
        Account accFrom = findUser(t.getFromId());
        Account accTo = findUser(t.getToId());

        if (accFrom == null || accTo == null)
            return false;

        double accFromBalance = Double.parseDouble(accFrom.getBalance());
        double transactionValue = Double.parseDouble(t.getAmount().substring(0,t.getAmount().length()-1));
        double provision = t.getProvisions();

        if (accFromBalance >= transactionValue + provision){
            double newBalanceFrom = Double.parseDouble(accFrom.getBalance().replace("$","")) - t.getProvisions() - Double.parseDouble(t.getAmount().replace("$",""));
            double newBalanceTo = Double.parseDouble(accTo.getBalance().replace("$","")) + Double.parseDouble(t.getAmount().replace("$",""));

            if (accFrom.equals(accTo)){
                accFrom.setBalance(Double.toString(Double.parseDouble(accFrom.getBalance().replace("$","")) - t.getProvisions()));
            }
            else {
                accFrom.setBalance(Double.toString(newBalanceFrom));
                accTo.setBalance(Double.toString(newBalanceTo));
            }

            provisions += provision;
            transfers += transactionValue;

            return true;
        }
        return false;
    }

    public String totalTransfers(){
        DecimalFormat pattern = new DecimalFormat("0.00");
        return pattern.format(transfers) + "$";
    }

    public String totalProvision(){
        DecimalFormat pattern = new DecimalFormat("0.00");
        return pattern.format(provisions) + "$";
    }

    public Account[] getAccounts() {
        Account[] temp = new Account[accounts.length];
        for (int i=0; i<accounts.length; i++){
            temp[i] = (Account) accounts[i].clone();
        }

        return temp;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTransfers(double transfers) {
        this.transfers = transfers;
    }

    public void setProvisions(double provisions) {
        this.provisions = provisions;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder("Name: " + name + "\n\n");
        for (Account account : accounts){
            temp.append(account.toString());
        }
        return temp.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(bank.transfers, transfers) == 0 && Double.compare(bank.provisions, provisions) == 0 && Arrays.equals(accounts, bank.accounts) && Objects.equals(name, bank.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, transfers, provisions);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }
}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}