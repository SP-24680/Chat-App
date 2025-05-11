package com.raven.event;

import java.io.File;

public interface EventFileReceiver {
    void onStartReceiving();
    void onReceiving(double percentage);
    void onFinish(File file); // Must take a File
    

}
