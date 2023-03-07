package Lab5.P5;

import java.util.Arrays;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.stream.IntStream;

class ResizableArray<T> {
    private T[] niza;
    private int n;
    private int size;

    public ResizableArray() {
        niza = (T[]) new Object[10];
        size = 10;
        n = 0;
    }

    public void addElement(T element) {
        if (size == n) {
            size *= 2;
            T[] temp = Arrays.copyOf(niza, size);
            niza = temp;
        }

        niza[n++] = element;
    }

    public boolean removeElement(T element) {
        int index = this.findIndex(element);
        if (index == -1)
            return false;

        for (int i = index; i < n - 1; i++) {
            niza[i] = niza[i + 1];
        }

        n--;
        return true;
    }

    private int findIndex(T element) {
        for (int i = 0; i < n; i++) {
            if (niza[i].equals(element))
                return i;
        }

        return -1;
    }

    public boolean contains(T element) {
        if (this.findIndex(element) != -1)
            return true;
        return false;
    }

    public Object[] toArray() {
        return Arrays.copyOf(niza, size);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int count() {
        return n;
    }

    public T elementAt(int index) {
        if (index >= n || index < 0)
            throw new ArrayIndexOutOfBoundsException();

        return niza[index];
    }

    static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        for (int i = 0, len = src.count(); i < len; i++) {
            dest.addElement(src.elementAt(i));
        }
    }
}

class IntegerArray extends ResizableArray<Integer> {
    public IntegerArray() {
        super();
    }

    public double sum() {
        double n = 0;
        for (int i = 0; i < count(); i++) {
            n += elementAt(i);
        }
        return n;
    }

    public double mean() {
        return sum() / count();
    }

    public int countNonZero() {
        int count = 0;
        for (int i = 0; i < count(); i++) {
            if (elementAt(i) != 0)
                count++;
        }
        return count;
    }

    public IntegerArray distinct() {
        IntegerArray arr = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            if (!arr.contains(elementAt(i)))
                arr.addElement(elementAt(i));
        }

        return arr;
    }

    public IntegerArray increment(int offset) {
        IntegerArray arr = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            arr.addElement(elementAt(i) + offset);
        }

        return arr;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
