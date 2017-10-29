package com.bsu.lab2.client;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 19.10.2017
 */
public class EncryptionService {
    private final KeyPair keyPair;

    public EncryptionService()
    {
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            keyPair = keyGen.genKeyPair();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Initialization error", e);
        }
    }

    PublicKey getPublic()
    {
        return keyPair.getPublic();
    }

    PrivateKey getPrivate()
    {
        return keyPair.getPrivate();
    }

    public String getPublicAsString()
    {
        return Base64.encodeBase64String(keyPair.getPublic().getEncoded());
    }

    public String decrypt(String text)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(text));
            return new String(decrypted, "UTF-8");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Decryption exception", e);
        }
    }

    public static String encrypt(String data, String publicKey)
    {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            byte[] keyBytes = java.util.Base64.getDecoder().decode(publicKey.getBytes());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(spec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeBytes = cipher.doFinal(data.getBytes());
            return java.util.Base64.getEncoder().encodeToString(encodeBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }

    }
}
