package com.rossallenbell.strifebasin.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;

public class CommSocketListener implements Runnable {
    
    private static CommSocketListener theInstance;
    
    public static CommSocketListener getInstance() {
        if (theInstance == null) {
            theInstance = new CommSocketListener();
        }
        return theInstance;
    }
    
    private CommSocketListener() {
        
    }
    
    @Override
    public void run() {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(ConnectionToOpponent.getInstance().getCommSocket().getInputStream()));
            while (!ConnectionToOpponent.getInstance().getCommSocket().isClosed()) {
                String commInput;
                while ((commInput = in.readLine()) != null) {
                    if(commInput.equals("accept")) {
                        ConnectionToOpponent.getInstance().theyAccepted();
                    } else {
                        System.out.println("Unknown incoming data: " + commInput);
                    }
                }
                
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            if(e.getMessage().equals("socket closed")) {
                System.out.println("Remote communication closed");
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
