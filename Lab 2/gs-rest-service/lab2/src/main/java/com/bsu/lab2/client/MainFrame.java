package com.bsu.lab2.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 24.10.2017
 */
public class MainFrame extends JFrame {

    private JComboBox<String> fileList;
    private MessageClient client;
    private JButton openFile = new JButton("Open choosen file");
    private JTextArea textArea = new JTextArea();

    public MainFrame()
    {
        super();
        try {
            client = new MessageClient();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No connection to server", e);
        }
        String [] fileNames = client.getInitResponse().fileList;
        fileList = new JComboBox<>(fileNames);
        textArea.setEditable(false);
        JScrollPane pane = new JScrollPane(textArea);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, textArea);
        JPanel tmp = new JPanel();
        add(tmp, BorderLayout.NORTH);
        tmp.setLayout(new GridLayout(1, 2));
        tmp.add(fileList);
        tmp.add(openFile);
        openFile.addActionListener(e -> {
            try {
                String text = client.getText((String) fileList.getSelectedItem());
                textArea.setText(text);
            } catch (IOException e1) {
                throw new RuntimeException("No connection to server", e1);
            }
        });
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setSize(400,800);
        frame.setVisible(true);
    }
}
