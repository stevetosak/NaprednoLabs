package Labs7;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency {
    private final Map<String, Integer> termsToOccurrences;

    private boolean isStopWord(String s, String[] stopWords) {
        return Arrays.asList(stopWords).contains(s);
    }

    public void parseInput(String[] words, String[] stopWords) {
        Arrays.stream(words)
                .filter(word -> !isStopWord(word, stopWords) && !word.isEmpty())
                .forEach(word -> termsToOccurrences.put(word, termsToOccurrences.getOrDefault(word, 0) + 1));

    }

    public int countTotal() {
        return termsToOccurrences.values().stream().reduce(0, Integer::sum);
    }

    public int countDistinct() {
        return termsToOccurrences.keySet().size();
    }

    public List<String> mostOften(int k) {
        Comparator<Map.Entry<String, Integer>> comparator = Comparator.comparingInt(Entry::getValue);
        return termsToOccurrences
                .entrySet()
                .stream()
                .sorted(comparator.reversed().thenComparing(Entry::getKey))
                .limit(k)
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    public TermFrequency(InputStream inputStream, String[] stopWords) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        termsToOccurrences = new HashMap<>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] words = Arrays.stream(
                            line.replaceAll("[.,]", "").split("\\s++"))
                    .map(String::toLowerCase)
                    .toArray(String[]::new);

            parseInput(words, stopWords);
        }
        br.close();
    }
}


public class TermFrequencyTest {
    public static void main(String[] args) throws IOException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
