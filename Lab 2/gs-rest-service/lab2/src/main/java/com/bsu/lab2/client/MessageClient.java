package com.bsu.lab2.client;

import com.bsu.lab2.model.InitResponse;
import com.bsu.lab2.model.TextResponse;
import com.bsu.lab2.server.aes.AESUtils;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 23.10.2017
 */
public class MessageClient
{
    private String sessionKey;
    private String baseUrl = "http://localhost:4567";
    private InitResponse initResponse;


    public MessageClient() throws IOException {
        EncryptionService encryptionService = new EncryptionService();
        String response = Request.Post(baseUrl + "/init")
                .bodyString(encryptionService.getPublicAsString(), ContentType.DEFAULT_TEXT)
                .execute()
                .returnContent().asString();
        initResponse = new GsonBuilder().disableHtmlEscaping().create().fromJson(response, InitResponse.class);
        initResponse.sessionKey = encryptionService.decrypt(initResponse.sessionKey);
        System.out.println(initResponse.sessionKey);
    }

    public String getText(String fileName) throws IOException {
        String responseBody = Request.Post(baseUrl + "/" + "file" + "/" + initResponse.id + "/" + fileName)
                .execute()
                .returnContent().asString();
        //System.out.println(responseBody);
        System.out.println(initResponse.sessionKey);
        TextResponse response = new GsonBuilder().disableHtmlEscaping().create().fromJson(responseBody, TextResponse.class);
        //System.out.println(response);
        //System.out.println(Arrays.toString(Base64.decodeBase64(response.initBlock)));
        String res = AESUtils.decrypt(response.text, initResponse.sessionKey.getBytes(), Base64.decodeBase64(response.initBlock));
        //System.out.println(res);
        return res;//AESUtils.decrypt(response.text, initResponse.sessionKey.getBytes(), Base64.decodeBase64(response.initBlock));
    }

    public InitResponse getInitResponse() {
        return initResponse;
    }
}
