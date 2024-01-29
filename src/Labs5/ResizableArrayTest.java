package Labs5;
import java.util.*;

class ResizableArray<T>{
    private T[] array;
    private int size;
    @SuppressWarnings("unchecked")
    ResizableArray(){
        array = (T[]) new Object[0];
        size = 0;
    }
    @SuppressWarnings("unchecked")
    public void addElement(T element){
        if(++size >= array.length){
            T[] copy = (T[]) new Object[size * 2];
            System.arraycopy(array,0,copy,0,size -1);
            array = copy;
        }
        array[size -1] = element;
    }
    public boolean removeElement(T element){
        for(int i = 0; i < size; i++){
            if(element.equals(array[i])){
                shift(i);
                --size;
                return true;
            }
        }

        return false;
    }

    public boolean contains(T element){
        for(int i = 0; i < size; i++){
            if(element.equals(array[i])){
                return true;
            }
        }

        return false;
    }

    public Object[] toArray(){
        return Arrays.copyOf(array,size,Object[].class);
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int count(){
        return size;
    }

    public T elementAt(int idx){
        if(idx < 0 || idx > size -1){
            throw new ArrayIndexOutOfBoundsException();
        }

        return array[idx];
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
        int n = src.count();
        for(int i = 0; i < n; i++){
            dest.addElement(src.elementAt(i));
        }
    }

    private void shift(int i) {
        for(int j = i; j < size; j++){
            T temp = array[j];
            array[j] = array[j+1];
            array[j+1] = temp;
        }
    }
}

class IntegerArray extends ResizableArray<Integer>{

    IntegerArray(){
        super();
    }

    public double sum(){
        double result = 0;
        for(int i = 0; i < count(); i++){
            result += elementAt(i);
        }

        return result;
    }
    public double mean(){
        return sum()/count();
    }
    public int countNonZero(){
        int count = 0;
        for(int i = 0; i < count(); i++){
            if(elementAt(i) != 0){
                count ++;
            }
        }
        return count;
    }
   public IntegerArray distinct(){
        IntegerArray copyArr = new IntegerArray();
        Set<Integer> set = new HashSet<>();

        for(int i = 0; i < count(); i++){
            set.add(elementAt(i));
        }

        for(Integer elem : set){
            copyArr.addElement(elem);
        }

        return copyArr;
    }

    public IntegerArray increment(int offset){
        IntegerArray copyArr = new IntegerArray();
        for(int i = 0; i < count(); i++){
            copyArr.addElement(elementAt(i) + offset);
        }
        return copyArr;
    }

}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
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
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
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
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
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
            if ( a.sum() > 100 )
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
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

