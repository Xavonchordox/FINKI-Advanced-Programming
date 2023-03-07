package Lab3.P1;

import java.util.Arrays;
import java.util.Scanner;

interface Item {
    int getPrice();

    String getType();

}

class ExtraItem implements Item {
    private String type;
    private static final int COKE_PRICE = 5;
    private static final int KETCHUP_PRICE = 3;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (!type.equals("Coke") && !type.equals("Ketchup"))
            throw new InvalidExtraTypeException();

        this.type = type;
    }

    @Override
    public int getPrice() {
        if (type.equals("Coke"))
            return COKE_PRICE;
        return KETCHUP_PRICE;
    }

    @Override
    public String getType() {
        return type;
    }
}

class PizzaItem implements Item {
    private String type;
    private static final int STANDARD_PRICE = 10;
    private static final int PEPPERONI_PRICE = 12;
    private static final int VEGETARIAN_PRICE = 8;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if (!type.equals("Standard") && !type.equals("Pepperoni") && !type.equals("Vegetarian"))
            throw new InvalidPizzaTypeException();

        this.type = type;
    }

    @Override
    public int getPrice() {
        if (type.equals("Standard"))
            return STANDARD_PRICE;
        else if (type.equals("Pepperoni"))
            return PEPPERONI_PRICE;
        return VEGETARIAN_PRICE;
    }

    @Override
    public String getType() {
        return type;
    }
}

class Order {
    private Item[] items;
    private int[] counts;
    private boolean isLocked;

    public Order() {
        items = new Item[0];
        counts = new int[0];
        isLocked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (isLocked)
            throw new OrderLockedException();
        if (count > 10)
            throw new ItemOutOfStockException(item);
        if (alreadyOrdered(item) == -1) {
            Item[] tempItems = Arrays.copyOf(items, items.length + 1);
            int[] tempCounts = Arrays.copyOf(counts, counts.length + 1);

            tempItems[items.length] = item;
            tempCounts[counts.length] = count;

            items = tempItems;
            counts = tempCounts;
        } else {
            int index = alreadyOrdered(item);
            counts[index] = count;
        }
    }

    private int alreadyOrdered(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].getType().equals(item.getType())) {
                return i;
            }
        }
        return -1;
    }

    public int getPrice() {
        int sum=0;
        for (int i=0; i<items.length; i++){
            sum += getItemPrice(i);
        }

        return sum;
    }

    private int getItemPrice(int index) {
        return items[index].getPrice() * counts[index];
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<items.length; i++){
            sb.append(String.format("%3d.%-15sx%2d%5d$\n", i+1, items[i].getType(), counts[i], getItemPrice(i)));
        }
        sb.append(String.format("%-22s%5d$", "Total:", getPrice()));
        System.out.println(sb);
    }

    public void removeItem(int index) throws ArrayIndexOutOfBоundsException, OrderLockedException {
        if (isLocked)
            throw new OrderLockedException();
        if (index < 0 || index > items.length)
            throw new ArrayIndexOutOfBоundsException(index);
        for (int i=index; i<items.length-1; i++){
            items[i] = items[i+1];
            counts[i] = counts[i+1];
        }

        items = Arrays.copyOf(items, items.length-1);
        counts = Arrays.copyOf(counts, counts.length-1);
    }

    public void lock() throws EmptyOrder {
        if (items.length != 0){
            isLocked = true;
        }
        else throw new EmptyOrder();
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}