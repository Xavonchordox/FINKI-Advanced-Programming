package Lab6.P2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

class IntegerList{
    private ArrayList<Integer> list;
    private int size;

    public IntegerList(){
        list = new ArrayList<>();
        size = 0;
    }
    public IntegerList(Integer... numbers){
        list = (ArrayList<Integer>) Arrays.asList(numbers);
    }


    public void add(int el, int index) {
        if (index > size()){
            int addMore = index-size;
            while (addMore < 1){
                list.add(0);
            }
            list.add(el);
            size = index;
        }
        else {
            list.add(index, el);
            size++;
        }
    }

    public int remove(int index) {
        int el = list.remove(index);
        size--;
        return el;
    }

    public void set(int el, int index){
        list.set(index, el);
    }

    public int get(int index) {
        return list.get(index);
    }

    public int size() {
        return size;
    }

    public int count(int element) {
//        int count = 0;
//        for(int i=0; i<size; i++){
//            if (list.get(i) == element)
//                count++;
//        }
//        return count;

        return Collections.frequency(list, element);
    }

    public void removeDuplicates() {
        list = (ArrayList<Integer>) list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public int sumFirst(int k) {
        return list.stream().limit(k).mapToInt(i -> i).sum();
    }

    public int sumLast(int k) {
        return list.stream().skip(list.size()-k).mapToInt(i -> i).sum();
    }

    public IntegerList addValue(int value) {
        return new IntegerList(list.stream().mapToInt(i -> i + value).boxed().toArray(Integer[]::new));
    }

    public void shiftLeft(int index, int k) {
    }

    public void shiftRight(int index, int k) {
        
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}