package lab1;

import java.io.*;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 17.09.2017
 */
public class Test {
    public static void main(String... args) throws IOException {
        File testFile = new File("TestText.txt");
        BufferedReader reader = new BufferedReader(new FileReader(testFile));
        StringBuffer res = new StringBuffer();
        String line = reader.readLine();
        while (line != null)
        {
            res.append(line);
            line = reader.readLine();
        }
        String text = new String(res).toLowerCase();
        String clearText = String.valueOf(EncryptionService.getClearText(text));
        String encryptText = EncryptionService.encrypt(text, "military");
        String decryptText = DecryptionService.decode(encryptText);
        System.out.println("Percent of identity - " + compare(decryptText, clearText));
    }

    public static double compare(String  text, String decryptText)
    {
        if (text.length() != decryptText.length())
        {
            return 0;
        }
        else
        {
            double rigthSymbols = 0;
            for (int i = 0; i < text.length(); i++)
            {
                if (text.charAt(i) == decryptText.charAt(i))
                {
                    rigthSymbols ++;
                }
            }
            return rigthSymbols / text.length() * 100;
        }
    }
}