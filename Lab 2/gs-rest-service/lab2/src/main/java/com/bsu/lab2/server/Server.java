package com.bsu.lab2.server;


import com.bsu.lab2.client.EncryptionService;
import com.bsu.lab2.model.InitResponse;
import com.bsu.lab2.model.TextResponse;
import com.bsu.lab2.server.aes.AESUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.rmi.server.UID;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static spark.Spark.post;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 13.10.2017
 */
public class Server {
    public static String folderPath = "files/";
    public static void main(String[] args)
    {
        Map<String, String> users = new HashMap<>();

        post("/init", ((request, response) ->
        {
            String publicKey = request.body();
            System.out.println(publicKey);
            String sessionKey = Utils.generateKey();
            String tmp = EncryptionService.encrypt(sessionKey, publicKey);
            String userId = new UID().toString();
            users.put(userId, sessionKey);
            response.status(200);
            InitResponse initResponse = new InitResponse(userId, tmp, getFileList());
            String res = new GsonBuilder().disableHtmlEscaping().create().toJson(initResponse, InitResponse.class);
            System.out.println(res);
            return res;
        }));
        post("/file/:id/:filename",((request, response) ->
        {
            String id = request.params(":id");
            System.out.println(id);
            if (users.containsKey(id))
            {
                Optional<String> file = getFile(request.params(":filename"));
                //System.out.println(file);
                if (file.isPresent())
                {
                    response.status(200);
                    String initBlock = AESUtils.composeInitBlock();
                    //System.out.println(Arrays.toString(Base64.decodeBase64(initBlock)));
                    String key = users.get(id);
                    System.out.println(key);
                    //System.out.println(key);
                    String encryptedText = AESUtils.encrypt(file.get(), key.getBytes(), Base64.decodeBase64(initBlock));
                    //System.out.println(encryptedText);
                    TextResponse textResponse = new TextResponse(initBlock, encryptedText);
                    //System.out.println(textResponse);
                    return new GsonBuilder().disableHtmlEscaping().create().toJson(textResponse, TextResponse.class);
                }
                else
                {
                    response.status(404);
                    return "File not found.";
                }
            }
            else
            {
                response.status(401);
                return "No such user";
            }
        }));

    }

    public static Optional<String> getFile(String fileName)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(folderPath + "/" + fileName));
            StringBuffer res = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null)
            {
                res.append(line).append("\n");
            }

            return Optional.ofNullable(new String(res));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static String[] getFileList()
    {
        File folderFile = new File(folderPath);
        File [] textFiles = folderFile.listFiles();
        String [] res = new String[textFiles.length];
        for(int i = 0; i < textFiles.length; i++)
        {
            res[i] = textFiles[i].getName();
        }
        return res;
    }
}
