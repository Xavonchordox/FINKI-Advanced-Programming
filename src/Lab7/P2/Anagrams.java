package Lab7.P2;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) throws IOException {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        Scanner in = new Scanner(inputStream);
        Map<String, ArrayList<String>> anagrams = new TreeMap<>();

        while (in.hasNext()){
            String word = in.nextLine();
            boolean isAnagram = false;

            for (String key : anagrams.keySet()){
                isAnagram = checkAnagram(key, word);
                if (isAnagram){
                    anagrams.get(key).add(word);
                    break;
                }
            }

            if (!isAnagram) {
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                anagrams.put(word, list);
            }

        }

        in.close();

        StringBuilder sb = new StringBuilder();

        for (String key : anagrams.keySet()){
            if (anagrams.get(key).size() >= 5)
                sb.append(String.join(" ", anagrams.get(key))).append("\n");
        }
        System.out.println(sb);
    }

    private static boolean checkAnagram(String word1, String word2) {
        char[] w1 = word1.toCharArray();
        char[] w2 = word2.toCharArray();

        Arrays.sort(w1);
        Arrays.sort(w2);

        String sorted1 = new String(w1);
        String sorted2 = new String(w2);

        return sorted1.equals(sorted2);
    }
}
