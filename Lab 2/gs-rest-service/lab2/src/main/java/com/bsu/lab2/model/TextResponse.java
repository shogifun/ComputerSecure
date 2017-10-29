package com.bsu.lab2.model;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 22.10.2017
 */
public class TextResponse {
    public String initBlock;
    public String text;

    public TextResponse(String initBlock, String text) {
        this.initBlock = initBlock;
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextResponse{" +
                "initBlock='" + initBlock + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
