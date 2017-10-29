package com.bsu.lab2.client;

import com.bsu.lab2.model.InitResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 19.10.2017
 */
public class MessageSender {
    private static String baseUrl = "http://localhost:4567";


    public static void main(String[] args) throws IOException {
        MessageClient client = new MessageClient();
        System.out.println(client.getText("1.txt"));
    }

    public static String getSessionKey() throws IOException {
        EncryptionService encryptionService = new EncryptionService();

        String response =Request.Post(baseUrl + "/init")
                .bodyString(encryptionService.getPublicAsString(), ContentType.DEFAULT_TEXT)
                .execute()
                .returnContent().asString();
        Gson gson = new GsonBuilder().create();
        InitResponse initResponse = gson.fromJson(response, InitResponse.class);
        return initResponse.toString();
    }



}
