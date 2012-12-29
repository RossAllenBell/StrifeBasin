package com.rossallenbell.strifebasin.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;

public class ConnectionToOpponent {
    
    public static short DEFAULT_MY_PORT = 23456;
    
    private ServerSocket socket;
    
    private static ConnectionToOpponent theInstance;
    
    public static ConnectionToOpponent getInstance() {
        if(theInstance == null){
            theInstance = new ConnectionToOpponent();
        }
        return theInstance;
    }
    
    private ConnectionToOpponent() {
        
    }
    
    public void reservePort(int port) {
        try {
            socket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cleanup() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public int getPort() {
        if (socket != null) {
            return socket.getLocalPort();
        }
        return -1;
    }
    
    public String getIP() {
        try {
            URL whatismyip;
            whatismyip = new URL("http://checkip.amazonaws.com/");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));            
            return in.readLine(); // you get the IP as a String
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
}
