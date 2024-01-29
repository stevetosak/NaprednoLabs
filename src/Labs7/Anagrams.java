package Labs7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {



    public static void main(String[] args) throws IOException {
        findAll(System.in);
    }


    public static void findAll(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        Map<String, Set<String>> map = new LinkedHashMap<>();
        String word = br.readLine();

        while (word != null) {
            if (word.equals("peder")) break;
            char[] arr = word.toCharArray();
            Arrays.sort(arr);
            String letters = new String(arr);

            if (!map.containsKey(letters)) {
                map.put(letters, new TreeSet<>());
                map.get(letters).add(word);
            } else {
                map.get(letters).add(word);
            }

            word = br.readLine();
        }

        filterAndPrint(map);

    }

    static void filterAndPrint(Map<String, Set<String>> map) {
        Map<String, Set<String>> filteredMap = map.entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 4)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));

//        filteredMap.values().stream().reduce("Stefan",(acc,val) -> { // UBA RABOTA :D
//            acc.addAll(val);
//            return acc;
//        }, (val1,val2) -> {
//            val1.addAll(val2);
//            return val1;
//        });


        for (Set<String> set : filteredMap.values()) {
            for (String s : set) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

}

