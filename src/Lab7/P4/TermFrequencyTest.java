package Lab7.P4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency{
    private Map<String,Integer> frequency;
    private Set<String> stop;


    public TermFrequency(InputStream inputStream, String[] stopWords) {
        frequency = new TreeMap<>();
        stop = new HashSet<>();

        for (String w : stopWords){
            stop.add(w);
        }

        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            line = line.trim();

            if (line.length() > 0){
                String[] words = line.split("\\s+");
                for (String word : words){
                    String key = filtrateWord(word);
                    if (key.isEmpty() || stop.contains(key))
                        continue;
                    if (frequency.containsKey(key)) {
                        Integer count = frequency.get(key);
                        frequency.put(key, count + 1);
                    }
                    else
                        frequency.put(key, 1);
                }
            }
        }
    }

    private String filtrateWord(String word){
        return word.replace(".", "").replace(",", "").toLowerCase().trim();
    }

    public int countTotal() {
        int sum = 0;

        for (Integer i : frequency.values())
            sum += i;

        return sum;
    }

    public int countDistinct() {
        return frequency.keySet().size();
    }

    public List<String> mostOften(int k) {
        return frequency.keySet().stream()
                .sorted(Comparator.comparing(key -> frequency.get(key)).reversed())
                .collect(Collectors.toList())
                .subList(0,k);
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
