package lab1;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 12.09.2017
 */
public class DecryptionService {
    public static final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static final double[] frequency = {0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.0228, 0.02015, 0.06094, 0.06966, 0.00153,
            0.00772, 0.04025, 0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056,
            0.02758, 0.00978, 0.0236, 0.0015, 0.01974, 0.00074};

    public static int nod(int a, int b) {
        if (b == 0)
            return a;
        else
            return nod(b, a % b);
    }

    public static int kasiski(int l, String text) {
        List<Integer> length = new ArrayList<>();

        for (int i = 0; i < text.length() - (l - 1); i++) {
            String str1 = text.substring(i, i + l);
            for (int j = i + 1; j < text.length() - (l - 1); j++) {
                String str2 = text.substring(j, j + l);
                if (str1.compareTo(str2) == 0)
                    length.add(j - i);
            }
        }
        Map<Integer, Integer> predictLength = new HashMap<>();

        for (int i = 0; i < length.size(); i++) {
            for (int j = i + 1; j < length.size(); j++) {
                if (length.get(i) > length.get(j)) {
                    int nod = nod(length.get(i), length.get(j));
                    if (nod == 1)
                        continue;

                    if (predictLength.containsKey(nod)) {
                        int len = predictLength.get(nod);
                        predictLength.put(nod, ++len);
                    } else
                        predictLength.put(nod, 1);
                }

            }
        }

        int max = Integer.MIN_VALUE;
        int nod = 0;
        for (Map.Entry<Integer, Integer> obj : predictLength.entrySet()) {
            if (obj.getValue() > max) {
                max = obj.getValue();
                nod = obj.getKey();
            }
        }

        if (nod == 0)
            nod = 1;
        System.out.println("Length of key word = " + nod);

        return nod;
    }

    public static String decode(String text) {
        Map<Character, Integer> charToIndex = new HashMap<>();
        int n = alphabet.length;
        for (int i = 0; i < n; i++) {
            charToIndex.put(alphabet[i], i);
        }
        int nod = kasiski(3, text);
        List<Map<Character, Integer>> predictFrequency = new ArrayList<>(nod);
        for (int i = 0; i < nod; i++) {
            Map<Character, Integer> curMap = new HashMap<>();
            for (int j = i; j < text.length(); j += nod) {
                if (curMap.containsKey(text.charAt(j))) {
                    int number = curMap.get(text.charAt(j));
                    curMap.put(text.charAt(j), ++number);
                } else
                    curMap.put(text.charAt(j), 1);
            }
            predictFrequency.add(curMap);
        }
        int len = text.length() / nod;
        StringBuilder decodeText = new StringBuilder(text);
        int index = 0;
        for (Map<Character, Integer> curMap : predictFrequency) {
            double maxFreq = Double.MIN_VALUE;
            Character maxLetter = ' ';
            for (Map.Entry<Character, Integer> item : curMap.entrySet()) {
                if (item.getKey() == ' ')
                    continue;
                double freq = 0;
                if (index < text.length() % nod)
                    freq = (double) item.getValue() / (len + 1);
                else
                    freq = (double) item.getValue() / len;
                if (freq > maxFreq) {
                    maxFreq = freq;
                    maxLetter = item.getKey();
                }
            }
            int difference = 0;
            if (charToIndex.get(maxLetter) > charToIndex.get('e'))
                difference = charToIndex.get(maxLetter) - charToIndex.get('e');
            else
                difference = alphabet.length - Math.abs(charToIndex.get(maxLetter) - charToIndex.get('e'));

            for (int i = index; i < decodeText.length(); i += nod) {
                if (decodeText.charAt(i) == ' ')
                    continue;
                decodeText.setCharAt(i, alphabet[(charToIndex.get(decodeText.charAt(i)) + alphabet.length - difference) % alphabet.length]);
            }
            index++;
        }
        System.out.println("Length of the text = " + decodeText.length());
        System.out.println(decodeText);
        return new String(decodeText);
    }
}
