package Labs6;

import java.util.*;
import java.util.stream.Collectors;


class IntegerList {
    private class ArrayShifter {
        ArrayShifter(){}
        public void shiftRightAll(int idx) {
            for (int i = size - 2; i >= idx; i--) {
                elements[i + 1] = elements[i];
            }
        }

        public void shiftLeftAll(int idx) {
            for (int i = idx; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
        }

        public void optimalShift(int idx, int k) {
            int targetIdx = ((idx + (k % size)) % size);
            if (targetIdx > idx) shiftRightExec(idx, targetIdx - idx);
            else if (targetIdx < idx) shiftLeftExec(idx, Math.abs(idx - targetIdx));
        }

        private void shiftRightExec(int idx, int k) {
            for (int i = idx; k != 0; i++) {
                int temp = elements[i];
                elements[i] = elements[i + 1];
                elements[i + 1] = temp;
                k--;
            }
        }

        private void shiftLeftExec(int idx, int k) {
            for (int i = idx; k != 0; i--) {
                int temp = elements[i];
                elements[i] = elements[i - 1];
                elements[i - 1] = temp;
                k--;
            }
        }
    }

    private Integer[] elements;
    private int size;

    IntegerList() {
        elements = new Integer[0];
        size = 0;
    }

    IntegerList(Integer[] nums) {
        elements = new Integer[nums.length];
        System.arraycopy(nums, 0, elements, 0, nums.length);
        size = elements.length;
    }

    // utility
    private void resize(int factor) {
        int len = elements.length == 0 ? size : elements.length;
        factor = factor == 1 ? 2 : factor;
        Integer[] copy = new Integer[len * factor];
        Arrays.fill(copy, 0);
        System.arraycopy(elements, 0, copy, 0, elements.length);
        elements = copy;
    }

    void add(int elem, int idx) {
        ++size;
        boolean toShift = idx < elements.length - 1;
        while (size > elements.length || idx > elements.length - 1) {
            resize(2);
        }

        if (toShift) {
            ArrayShifter arrayShifter = new ArrayShifter();
            arrayShifter.shiftRightAll(idx);
        }

        elements[idx] = elem;
        size = size();
    }


    int remove(int idx) {
        if (idx < 0 || idx > size - 1) throw new ArrayIndexOutOfBoundsException();
        int removed = elements[idx];

        Integer[] tmp = new Integer[size() - 1];
        for(int i = 0, j = 0; i < size; i++){
            if(i != idx){
                tmp[j] = elements[i];
                j++;
            }
        }
        --size;
        elements = tmp;
        return removed;
    }

    void set(int elem, int idx) {
        if (idx < 0 || idx > size - 1) throw new ArrayIndexOutOfBoundsException();
        elements[idx] = elem;
    }

    int get(int idx) {
        return elements[idx];
    }

    int size() {
        int lastIdx = 0;
        for(int i = 0; i < elements.length; i++){
            if(elements[i] != 0){
                lastIdx = i;
            }
        }
        return lastIdx + 1;
    }

    void shiftRight(int idx, int k) {
        ArrayShifter arrayShifter = new ArrayShifter();
        if (idx < 0 || idx > size - 1) throw new ArrayIndexOutOfBoundsException();
        if (idx + k > size - 1) arrayShifter.optimalShift(idx, k);
        else arrayShifter.shiftRightExec(idx, k);
    }

    void shiftLeft(int idx, int k) {
        ArrayShifter arrayShifter = new ArrayShifter();
        if (idx < 0 || idx > size - 1) throw new ArrayIndexOutOfBoundsException();
        if (idx - k < 0) arrayShifter.optimalShift(idx, Math.abs(size - (k % size)));
        else arrayShifter.shiftLeftExec(idx, k);

    }

    int count(int elem) {
        List<Integer> result = Arrays.stream(elements)
                .filter(integer -> integer == elem)
                .collect(Collectors.toList());

        return result.size();
    }

    void removeDuplicates() {
        Map<Integer, Integer> hash = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            hash.put(elements[i], i);
        }
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(hash.entrySet());
        list.sort(Map.Entry.comparingByValue());
        size = hash.size();
        elements = list.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .toArray(new Integer[0]);
    }

    int sumFirst(int k) {
        int sum = 0;
        if (k > size) k = size;

        for (int i = 0; i < k; i++) {
            sum += elements[i];
        }
        return sum;
    }

    int sumLast(int k) {
        int sum = 0;
        for (int i = size - 1; i > size - k - 1; i--) {
            sum += elements[i];
        }
        return sum;
    }

    IntegerList addValue(int value) {
        Integer[] result = Arrays.copyOf(elements, size);
        result = Arrays.stream(result)
                .mapToInt(i -> i + value)
                .boxed().collect(Collectors.toList())
                .toArray(new Integer[0]);
        return new IntegerList(result);
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
