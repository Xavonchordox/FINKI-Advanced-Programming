package Lab5.P3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class IntegerList {
    private ArrayList<Integer> list;

    public IntegerList() {
        list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        this.list = new ArrayList<>(numbers.length);
        Collections.addAll(this.list, numbers);
    }

    public void add(int el, int idx) {
        if (idx > list.size()) {
            ArrayList<Integer> temp = new ArrayList<>(idx + 1);
            temp.addAll(list);
            for (int i = list.size(); i < idx; i++) {
                temp.add(0);
            }
            temp.add(el);
            list = temp;
        } else
            list.add(idx, el);
    }

    public int remove(int idx) {
        inRange(idx);
        return list.remove(idx);
    }

    public void set(int el, int idx) {

    }


    public int get(int idx) {
        inRange(idx);
        return list.get(idx);
    }

    public int size() {
        return list.size();
    }

    public int count(int element) {
        return Collections.frequency(list, element);
    }


    public void removeDuplicates() {
        Collections.reverse(list);
        ArrayList<Integer> temp = (ArrayList<Integer>) list.stream()
                .distinct()
                .collect(Collectors.toList());
        Collections.reverse(temp);
        list = temp;
    }

    int sumFirst(int k) {
        return list.stream().limit(k).mapToInt(Integer::intValue).sum();
    }


    int sumLast(int k) {
        return list.stream().skip(list.size() - k).mapToInt(Integer::intValue).sum();
    }

    public void shiftRight(int idx, int k) {
        inRange(idx);
        int newPoss = (idx + k) % list.size();
        int el = list.remove(idx);
        list.add(newPoss, el);
    }

    public void shiftLeft(int idx, int k) {
        inRange(idx);
        int newPoss = (idx - k) % list.size();
        if (newPoss < 0)
            newPoss += list.size();

        int el = list.remove(idx);
        list.add(newPoss, el);

    }

    public IntegerList addValue(int value) {
        return new IntegerList(list.stream().mapToInt(i -> i + value).boxed().toArray(Integer[]::new));
    }

    public void inRange(int idx) {
        if (idx < 0 || idx >= list.size())
            throw new ArrayIndexOutOfBoundsException();
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}