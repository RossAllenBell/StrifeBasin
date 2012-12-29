package com.rossallenbell.strifebasin.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import com.rossallenbell.strifebasin.StrifeBasin;
import com.rossallenbell.strifebasin.threads.CommSocketListener;
import com.rossallenbell.strifebasin.threads.ConnectionListener;
import com.rossallenbell.strifebasin.ui.ConnectionPanel;

public class ConnectionToOpponent {
    
    public static short DEFAULT_MY_PORT = 23456;
    
    private ServerSocket listeningSocket;
    private Socket commSocket;
    private Socket incomingSocket;
    private PrintWriter commWriter;
    
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
            listeningSocket = new ServerSocket(port);
            new Thread(ConnectionListener.getInstance()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cleanup() {
        if (listeningSocket != null) {
            try {
                if(incomingSocket != null && !incomingSocket.isClosed())incomingSocket.close();
                if(commSocket != null && !commSocket.isClosed())commSocket.close();
                if(listeningSocket != null && !listeningSocket.isClosed())listeningSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public int getPort() {
        if (listeningSocket != null) {
            return listeningSocket.getLocalPort();
        }
        return -1;
    }
    
    public String getIP() {
        try {
            URL whatismyip;
            whatismyip = new URL("http://checkip.amazonaws.com/");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));            
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public void invite(String ip, int port) {
        try {
            commSocket = new Socket(ip, port);
            new Thread(CommSocketListener.getInstance()).start();
            incomingSocket = null;
            listeningSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ServerSocket getListeningSocket() {
        return listeningSocket;
    }
    
    public Socket getCommSocket() {
        return commSocket;
    }

    public void incomingConnection(Socket socket) {
        incomingSocket = socket;
        InetAddress remoteSocketAddress = incomingSocket.getInetAddress();
        ConnectionPanel.getInstance().incomingConnection(remoteSocketAddress.getCanonicalHostName(), socket.getPort());
    }

    public void accept() {
        commSocket = incomingSocket;
        new Thread(CommSocketListener.getInstance()).start();
        incomingSocket = null;
        try {
            commWriter = new PrintWriter(commSocket.getOutputStream(), true);
            commWriter.println("accept");
            listeningSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StrifeBasin.connectionComplete();
    }

    public void theyAccepted() {
        try {
            commWriter = new PrintWriter(commSocket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StrifeBasin.connectionComplete();
    }
    
}
