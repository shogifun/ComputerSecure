package com.bsu.lab2.model;

import java.util.Arrays;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 20.10.2017
 */
public class InitResponse {
    public String id;
    public String sessionKey;
    public String[] fileList;

    @Override
    public String toString() {
        return "InitResponse{" +
                "id='" + id + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                ", fileList=" + Arrays.toString(fileList) +
                '}';
    }

    public InitResponse(String id, String sessionKey, String[] fileList) {
        this.id = id;
        this.sessionKey = sessionKey;
        this.fileList = fileList;
    }
}
