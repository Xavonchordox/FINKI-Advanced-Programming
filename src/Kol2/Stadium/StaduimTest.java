package Kol2.Stadium;

import java.util.*;

class SeatNotAllowedException extends Exception {

}

class SeatTakenException extends Exception {

}

class Sector {
    private final String sectorCode;
    private int capacity;
    private int capacityS;
    private Map<Integer, String> seats;
    private String typeOfSeat;

    public Sector(String sectorCode, int capacity) {
        this.sectorCode = sectorCode;
        this.capacity = capacity;
        capacityS = capacity;
        seats = new TreeMap<>();
        for (int i = 1; i <= capacity; i++) {
            seats.put(i, "free");
        }
        typeOfSeat = "0";
    }

    public Map<Integer, String> getSeats() {
        return seats;
    }

    public void setSeat(int seat, String type) {
        seats.put(seat, type);
        if (typeOfSeat.equals("0")) {
            typeOfSeat = type;
        }

    }

    public String getSectorCode() {
        return sectorCode;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getTypeOfSeat() {
        return typeOfSeat;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", sectorCode, capacity, capacityS, 100-(capacity*100.f)/capacityS);
    }

    public void decreaseCapacity() {
        capacity -= 1;
    }
}

class Stadium {
    private final String name;
    private final Map<String, Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        sectors = new TreeMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {

        for (int i = 0; i < sectorNames.length; i++) {
            sectors.put(sectorNames[i], new Sector(sectorNames[i], sizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if (!sectors.get(sectorName).getSeats().get(seat).equals("free"))
            throw new SeatTakenException();

        boolean b1 = sectors.get(sectorName).getTypeOfSeat().equals("0");
        boolean b2 = sectors.get(sectorName).getTypeOfSeat().equals(String.valueOf(type));
        boolean b3 = type == 0;

        if (!b3) {
            if (!b2 && !b1)
                throw new SeatNotAllowedException();
        }

        sectors.get(sectorName).setSeat(seat, String.valueOf(type));
        sectors.get(sectorName).decreaseCapacity();
    }

    public void showSectors() {
        sectors.values()
                .stream()
                .sorted(Comparator.comparing(Sector::getCapacity).reversed()
                        .thenComparing(Sector::getSectorCode))
                .forEach(i -> System.out.println(i));
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
