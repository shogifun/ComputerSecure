package lab1;

import com.sun.deploy.util.StringUtils;

import java.util.Random;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 12.09.2017
 */
public class EncryptionService {
    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//"abcdefghijklmnopqrstuwxyz";
    private static char transform(char ch, int k)
    {
        int pos = alphabet.indexOf(ch); //+ 1;
        int transform_position = pos + k;
        if (transform_position > 25)
        {
            transform_position = (pos + k) % 26;
        }
        return alphabet.charAt(transform_position);
    }
    public static boolean isAlpha(char symbol)
    {
        return Character.isLetter(symbol);
    }
    public static String encrypt(String text, String key)
    {
        alphabet = alphabet.toLowerCase();
        int num_of_alphabets = key.length();
        text = text.toLowerCase();
        key = key.toLowerCase();
        int[] keys = new int[num_of_alphabets];
        for (int i =0; i < num_of_alphabets; i++)
        {
            keys[i] = alphabet.indexOf(key.charAt(i));
        }
        StringBuffer clear_text = getClearText(text);
        System.out.println(clear_text);
        for (int i = 0; i < clear_text.length(); i++)
        {
            int k = i % num_of_alphabets;
            char tr_ch = transform(clear_text.charAt(i), keys[k]);
            clear_text.setCharAt(i, tr_ch);
        }
        return new String(clear_text);
    }

    public static StringBuffer getClearText(String text)
    {
        StringBuffer clear_text = new StringBuffer();

        for (int i = 0; i <text.length(); i++)
        {
            char current_ch = text.charAt(i);
            if (isAlpha(current_ch))
            {
                clear_text.append(current_ch);
            }
        }
        return  clear_text;
    }
}
