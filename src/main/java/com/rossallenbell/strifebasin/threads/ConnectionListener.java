package com.rossallenbell.strifebasin.threads;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;

public class ConnectionListener implements Runnable {
    
    private ConnectionToOpponent connection;
    
    private static ConnectionListener theInstance;
    
    public static ConnectionListener getInstance() {
        if (theInstance == null) {
            theInstance = new ConnectionListener();
        }
        return theInstance;
    }
    
    private ConnectionListener() {
        connection = ConnectionToOpponent.getInstance();
    }

    @Override
    public void run() {
        Socket socket;
        try {
            if((socket = connection.getListeningSocket().accept()) != null){
                connection.incomingConnection(socket);
            }
        } catch (SocketException e) {
            //we invited somebody
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
