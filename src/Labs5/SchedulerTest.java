package Labs5;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

class Timestamp <T> implements Comparable<Timestamp<T>>{
    private final LocalDateTime time;
    private final T element;

    Timestamp(LocalDateTime time, T element){
        this.time = time;
        this.element = element;
    }
    LocalDateTime getTime(){
        return time;
    }
    T getElement(){
        return element;
    }

    @Override
    public int compareTo(Timestamp<T> o) {
        return time.compareTo(o.time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return time.isEqual(((Timestamp<?>) o).time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    @Override
    public String toString() {
        return time.toString() + " " + element.toString();
    }
}

class Scheduler<T>{
    List<Timestamp<T>> timestamps = new ArrayList<>();

    Scheduler(){}

    void add(Timestamp<T> timestamp){
        timestamps.add(timestamp);
    }
    boolean remove(Timestamp<T> t){
        for(int i = 0; i < timestamps.size(); i++){
            if(timestamps.get(i).equals(t)){
                timestamps.remove(i);
                return true;
            }
        }
        return false;
    }

    Timestamp<T> next(){
        List<Timestamp<T>> after = timestamps.stream()
                .filter(timestamp -> timestamp.getTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        return minDuration((List<Timestamp<T>>) after);
    }

    Timestamp<T> last(){
        List<Timestamp<T>> before = timestamps.stream()
                .filter(timestamp -> timestamp.getTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        return minDuration((List<Timestamp<T>>) before);
    }

    List<Timestamp<T>> getAll(LocalDateTime begin, LocalDateTime end){
        return timestamps.stream()
                .filter(timestamp -> timestamp.getTime().isAfter(begin) && timestamp.getTime().isBefore(end))
                .collect(Collectors.toList());
    }

    private Timestamp<T> minDuration(List<Timestamp<T>> before) {
        Timestamp<T> minTimestamp = before.get(0);
        Duration minDuration = Duration.between(before.get(0).getTime(),LocalDateTime.now()).abs();

        for(Timestamp<T> timestamp : before){
            Duration current = Duration.between(timestamp.getTime(),LocalDateTime.now()).abs();
            if(current.compareTo(minDuration) < 0){
                minDuration = current;
                minTimestamp = timestamp;
            }
        }
        return minTimestamp;
    }
}

public class SchedulerTest {

    static final LocalDateTime TIME = LocalDateTime.of(2016, 10, 25, 10, 15);

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Timestamp with String
            Timestamp<String> t = new Timestamp<>(TIME, jin.next());
            System.out.println(t);
            System.out.println(t.getTime());
            System.out.println(t.getElement());
        }
        if (k == 1) { //test Timestamp with ints
            Timestamp<Integer> t1 = new Timestamp<>(TIME, jin.nextInt());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<Integer> t2 = new Timestamp<>(TIME.plusDays(10), jin.nextInt());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 2) {//test Timestamp with String, complex
            Timestamp<String> t1 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<String> t2 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            System.out.println(scheduler.next().getElement());
            System.out.println(scheduler.last().getElement());
            List<Timestamp<String>> result = scheduler.getAll(now.minusHours(3), now.plusHours(4).plusMinutes(15));
            String out = result.stream()
                    .sorted()
                    .map(Timestamp::getElement)
                    .collect(Collectors.joining(", "));
            System.out.println(out);
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<>();
            int counter = 0;
            ArrayList<Timestamp<Integer>> forRemoval = new ArrayList<>();
            while (jin.hasNextLong()) {
                Timestamp<Integer> ti = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.nextInt());
                if ((counter & 7) == 0) {
                    forRemoval.add(ti);
                }
                scheduler.add(ti);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                LocalDateTime left = ofEpochMS(jin.nextLong());
                LocalDateTime right = ofEpochMS(jin.nextLong());
                List<Timestamp<Integer>> res = scheduler.getAll(left, right);
                Collections.sort(res);
                System.out.println(left + " <: " + print(res) + " >: " + right);
            }
            System.out.println("test");
            List<Timestamp<Integer>> res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            forRemoval.forEach(scheduler::remove);
            res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static LocalDateTime ofEpochMS(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
    }

    private static <T> String print(List<Timestamp<T>> res) {
        if (res == null || res.size() == 0) return "NONE";
        return res.stream()
                .map(each -> each.getElement().toString())
                .collect(Collectors.joining(", "));
    }

}

// vashiot kod ovde


