package Lab6.P1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

class SuperString{
    private LinkedList<String> list;
    private Stack<String> stack;
    public SuperString() {
        list = new LinkedList<>();
        stack = new Stack<>();
    }

    public void append(String s) {
        list.addLast(s);
        stack.push(s);
    }

    public void insert(String s) {
        list.addFirst(s);
        stack.push(s);
    }

    public boolean contains(String s) {
        return toString().contains(s);
    }

    public void reverse() {
        LinkedList<String> reversedList = new LinkedList<>();
        String str;
        while (!list.isEmpty()){
            str = list.getLast();
            str = reverseString(str);
            reversedList.addLast(str);
            list.removeLast();
        }

        for (String s : reversedList)
            list.addLast(s);
    }

    private String reverseString(String str){
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }

    public void removeLast(int k) {
        for (int i=0; i<k; i++){
            String removeStr = stack.pop();
            list.remove(removeStr);
            String removeRevStr = reverseString(removeStr);
            list.remove(removeRevStr);
        }

    }

    @Override
    public String toString() {
        return String.join("", list);
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
