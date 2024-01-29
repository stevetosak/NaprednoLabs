package Labs7;

import Labs4.ZonedDateTimeTest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

class Scheduler<T> {
    Map<Date, T> map;

    Scheduler() {
        map = new HashMap<>();
    }

    void add(Date d, T t) {
        map.put(d, t);
    }

    boolean remove(Date d) {
        if (map.containsKey(d)) {
            map.remove(d);
            return true;
        } else return false;
    }

    T last() {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().before(Calendar.getInstance().getTime()))
                .min(Comparator.comparing(entry -> Calendar.getInstance().getTime().getTime() - entry.getKey().getTime()))
                .map(Map.Entry::getValue).orElse(null);

    }

    T next() {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().after(Calendar.getInstance().getTime()))
                .min(Comparator.comparing(entry -> entry.getKey().getTime() - Calendar.getInstance().getTime().getTime()))
                .map(Map.Entry::getValue).orElse(null);

    }

    ArrayList<T> getAll(Date begin, Date end) {
        return (ArrayList<T>) map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().before(end) && entry.getKey().after(begin))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    T getFirst() {
        Optional<Map.Entry<Date, T>> result = map.entrySet()
                .stream()
                .min(Map.Entry.comparingByKey());

        return result.map(Map.Entry::getValue).orElse(null);
    }

    T getLast() {
        Optional<Map.Entry<Date, T>> result = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey());

        return result.map(Map.Entry::getValue).orElse(null);
    }
}

public class SchedulerTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime() - 7200000), jin.next());
            scheduler.add(new Date(now.getTime() - 3600000), jin.next());
            scheduler.add(new Date(now.getTime() - 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 7200000), jin.next());
            scheduler.add(new Date(now.getTime() + 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 3600000), jin.next());
            scheduler.add(new Date(now.getTime() + 18000000), jin.next());
            System.out.println(scheduler.getFirst());
            System.out.println(scheduler.getLast());
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime() - 7200000), jin.next());
            scheduler.add(new Date(now.getTime() - 3600000), jin.next());
            scheduler.add(new Date(now.getTime() - 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 7200000), jin.next());
            scheduler.add(new Date(now.getTime() + 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 3600000), jin.next());
            scheduler.add(new Date(now.getTime() + 18000000), jin.next());
            System.out.println(scheduler.next());
            System.out.println(scheduler.last());
            ArrayList<String> res = scheduler.getAll(new Date(now.getTime() - 10000000), new Date(now.getTime() + 17000000));
            Collections.sort(res);
            for (String t : res) {
                System.out.print(t + " , ");
            }
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<Integer>();
            int counter = 0;
            ArrayList<Date> to_remove = new ArrayList<Date>();

            while (jin.hasNextLong()) {
                Date d = new Date(jin.nextLong());
                int i = jin.nextInt();
                if ((counter & 7) == 0) {
                    to_remove.add(d);
                }
                scheduler.add(d, i);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                Date l = new Date(jin.nextLong());
                Date h = new Date(jin.nextLong());
                ArrayList<Integer> res = scheduler.getAll(l, h);
                Collections.sort(res);
                System.out.println(Converter.stringFormatDate(l) + " <: " + print(res) + " >: " + Converter.stringFormatDate(h));
            }
            System.out.println("test");
            ArrayList<Integer> res = scheduler.getAll(new Date(0), new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            for (Date d : to_remove) {
                scheduler.remove(d);
            }
            res = scheduler.getAll(new Date(0), new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static <T> String print(ArrayList<T> res) {
        if (res == null || res.size() == 0) return "NONE";
        StringBuffer sb = new StringBuffer();
        for (T t : res) {
            sb.append(t + " , ");
        }
        return sb.substring(0, sb.length() - 3);
    }
}

class Converter{
    public static String stringFormatDate(Date d){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(d);
    }
}