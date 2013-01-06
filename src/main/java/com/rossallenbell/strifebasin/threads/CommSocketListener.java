package com.rossallenbell.strifebasin.threads;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;
import com.rossallenbell.strifebasin.connection.domain.NetworkPlayer;
import com.rossallenbell.strifebasin.connection.gameevents.AttackEvent;
import com.rossallenbell.strifebasin.connection.protocol.ConnectionAccepted;
import com.rossallenbell.strifebasin.domain.Game;

public class CommSocketListener extends StoppableThread {
    
    private static CommSocketListener theInstance;
    
    public static CommSocketListener getInstance() {
        if (theInstance == null) {
            theInstance = new CommSocketListener();
        }
        return theInstance;
    }
    
    private CommSocketListener() {
        super();
    }
    
    @Override
    public void run() {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(ConnectionToOpponent.getInstance().getCommSocket().getInputStream());
            while (isRunning() && !ConnectionToOpponent.getInstance().getCommSocket().isClosed()) {
                Object commInput;
                while ((commInput = in.readObject()) != null) {
                    if (commInput instanceof ConnectionAccepted) {
                        ConnectionToOpponent.getInstance().theyAccepted();
                    } else if (commInput instanceof NetworkPlayer) {
                        Game.getInstance().updateTheirUnitsAndBuildings((NetworkPlayer) commInput);
                    } else if (commInput instanceof AttackEvent) {
                        Game.getInstance().attackEvent((AttackEvent) commInput);
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
        } catch (SocketException | EOFException e) {
            //somebody DCed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
