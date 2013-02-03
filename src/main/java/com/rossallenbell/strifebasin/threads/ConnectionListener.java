package com.rossallenbell.strifebasin.threads;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;

public class ConnectionListener implements Runnable {
    
    private static ConnectionListener theInstance;
    
    public static ConnectionListener getInstance() {
        if (theInstance == null) {
            synchronized (ConnectionListener.class) {
                if (theInstance == null) {
                    theInstance = new ConnectionListener();
                }
            }
        }
        return theInstance;
    }
    
    private ConnectionListener() {
        
    }
    
    @Override
    public void run() {
        Thread.currentThread().setName(getClass().getSimpleName());
        
        Socket socket;
        try {
            if ((socket = ConnectionToOpponent.getInstance().getListeningSocket().accept()) != null) {
                ConnectionToOpponent.getInstance().incomingConnection(socket);
            }
        } catch (SocketException e) {
            // we invited somebody
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
