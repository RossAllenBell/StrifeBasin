package com.rossallenbell.strifebasin.threads;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;

public class CommSocketSender extends StoppableThread {
    
    private List<CommObject> sendQueue = Collections.synchronizedList(new LinkedList<CommObject>());
    
    private static CommSocketSender theInstance;
    
    public static CommSocketSender getInstance() {
        if (theInstance == null) {
            synchronized (theInstance) {
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
            while (!sendQueue.isEmpty() && isRunning()) {
                ConnectionToOpponent.getInstance().sendObjectToThem(sendQueue.remove(0));
            }
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void enqueue(CommObject object) {
        sendQueue.add(object);
    }
    
}
