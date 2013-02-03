package com.rossallenbell.strifebasin.threads;

import java.util.LinkedList;
import java.util.List;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;

public class CommSocketSender extends StoppableThread {
    
    private List<CommObject> sendQueue = new LinkedList<CommObject>();
    
    private static CommSocketSender theInstance;
    
    public static CommSocketSender getInstance() {
        if (theInstance == null) {
            synchronized (CommSocketSender.class) {
                if (theInstance == null) {
                    theInstance = new CommSocketSender();
                }
            }
        }
        return theInstance;
    }
    
    private CommSocketSender() {
        super();
    }
    
    @Override
    public void run() {
        Thread.currentThread().setName(getClass().getSimpleName());
        
        while (isRunning()) {
            synchronized (CommSocketSender.class) {
                while (!sendQueue.isEmpty() && isRunning()) {
                    ConnectionToOpponent.getInstance().sendObjectToThem(sendQueue.remove(0));
                }
            }
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void enqueue(CommObject object) {
        synchronized (CommSocketSender.class) {
            sendQueue.add(object);
        }
    }
    
}
