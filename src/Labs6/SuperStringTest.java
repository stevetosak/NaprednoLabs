package Labs6;

import java.util.*;

class SuperString {
    private List<String> strings;
    private List<String> order;

    SuperString() {
        strings = new LinkedList<>();
        order = new LinkedList<>();
    }

    void append(String s) {
        strings.add(s);
        order.add(s);
    }

    void insert(String s) {
        strings.add(0, s);
        order.add(s);
    }

    boolean contains(String s) {
        return joined().contains(s);
    }

     String reverseString(String s) {
        char[] reversed = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            reversed[i] = s.charAt(s.length() - 1 - i);
        }
        return new String(reversed);
    }

    void reverse() {
        List<String> reversedStrings = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        List<String> newOrder = new LinkedList<>();

        this.order.forEach(s -> newOrder.add(reverseString(s)));

        this.strings.forEach(s -> {
            String reversed = reverseString(s);
            stack.push(reversed);
        });

        while (!stack.empty()) reversedStrings.add((stack.pop()));
        this.strings = reversedStrings;
        this.order = newOrder;
    }

    String joined(){
        StringBuilder sb = new StringBuilder();
        this.strings.forEach(sb::append);
        return sb.toString();
    }


    void removeLast(int k) {
        if(strings.size() - k < 0) k = strings.size();

        for (int i = 0; i < k; i++) {
            strings.remove(order.get(order.size() - 1));
            order.remove(order.size() - 1);
        }

    }

    @Override
    public String toString() {
       return joined();
    }

//    public static void main(String[] args) {
//        SuperString superString = new SuperString();
//        superString.append("Stefan");
//        superString.append("Saka");
//        superString.append("Da");
//        superString.append("Jadi");
//        superString.append("Nudli");
//        superString.append("Najdov");
//        superString.append("Dijamant");
//        System.out.println(superString);
//        superString.removeLast(2);
//        System.out.println(superString);
//        superString.reverse();
//        System.out.println();
//
//    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {//append(String s)
                    s.append(jin.next());
                }
                if (command == 1) {//insert(String s)
                    s.insert(jin.next());
                }
                if (command == 2) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {//reverse()
                    s.reverse();
                }
                if (command == 4) {//toString()
                    System.out.println(s);
                }
                if (command == 5) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {//end
                    break;
                    // A n d r Gaj du K
                }
            }
        }
    }

}

