package com.raven.service;

import com.raven.model.Model_User_Account;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.HashMap;
import com.raven.event.EventFileReceiver;
import com.raven.model.Model_Send_Message;
import java.io.File;

public class Service {

    private static Service instance;
    private Socket client;
    private Model_User_Account user;
    private Model_User_Account selectedUser;
    private final HashMap<String, Object> fileSenders = new HashMap<>();
    private final HashMap<String, Object> fileReceivers = new HashMap<>();

    private Service() {
        try {
            client = IO.socket("http://localhost:3000"); // Update this URL if your server runs elsewhere
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public Socket getClient() {
        return client;
    }

    public Model_User_Account getUser() {
        return user;
    }

    public void setUser(Model_User_Account user) {
        this.user = user;
    }

    public Model_User_Account getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Model_User_Account selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void addFileSender(String fileID, Object sender) {
        fileSenders.put(fileID, sender);
    }

    public Object getFileSender(String fileID) {
        return fileSenders.get(fileID);
    }

    public void fileSendFinish(Object sender) {
        // Optional cleanup
    }

    public void addFileReceiver(String fileID, Object receiver) {
        fileReceivers.put(fileID, receiver);
    }

    public Object getFileReceiver(String fileID) {
        return fileReceivers.get(fileID);
    }

    public void fileReceiveFinish(Object receiver) {
        // Optional cleanup
    }

    public void startServer() {
        // Optional: start embedded server logic
    }public void sendFile(File file) {
    // Dummy method stub: implement logic to upload or transfer file
    System.out.println("Sending file: " + file.getAbsolutePath());
}

public void addFile(File file, Model_Send_Message message) {
    // Dummy method stub: link file with message
    System.out.println("File added to message: " + file.getName());
}

public void addFileReceiver(String fileID, EventFileReceiver receiver) {
    fileReceivers.put(fileID, receiver);
}
}
