package com.bsu.lab2.server;

import com.bsu.lab2.server.aes.AESUtils;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static com.bsu.lab2.server.aes.AESUtils.*;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 15.10.2017
 */
public class Utils {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(Arrays.toString(Server.getFileList()));
        System.out.println(Server.getFile("2.txt").get());
    }

    public static String generateKey()
    {
        HashFunction hf = Hashing.goodFastHash(128);
        HashCode hashCode = hf.hashLong(System.currentTimeMillis());
        //System.out.println(hashCode.toString().substring(0, 16) + " " + hashCode.toString().substring(0, 16).getBytes().length);
        return hashCode.toString().substring(0, 16);
    }
}
