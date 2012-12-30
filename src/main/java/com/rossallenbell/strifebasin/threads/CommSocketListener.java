package com.rossallenbell.strifebasin.threads;

import java.io.EOFException;
import java.io.ObjectInputStream;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;
import com.rossallenbell.strifebasin.connection.protocol.ConnectionAccepted;

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
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(ConnectionToOpponent.getInstance().getCommSocket().getInputStream());
            while (!ConnectionToOpponent.getInstance().getCommSocket().isClosed()) {
                Object commInput;
                while ((commInput = in.readObject()) != null) {
                    if (commInput instanceof ConnectionAccepted) {
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
        } catch (EOFException e) {
            //somebody DCed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
