package com.rossallenbell.strifebasin.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;

@SuppressWarnings("serial")
public class ConnectionPanel extends JPanel implements ActionListener{
    
    private JButton reservePortButton;
    private JTextField myPortInput;
    
    private JButton inviteButton;
    private JTextField theirIP;
    private JTextField theirPort;
    
    private JButton acceptButton;
    private JLabel incomingIP;
    private JLabel incomingPort;
    
    private static ConnectionPanel theInstance;
    
    public static ConnectionPanel  getInstance() {
        if(theInstance == null){
            theInstance = new ConnectionPanel();
        }
        return theInstance;
    }

    private ConnectionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel reservePortPanel = new JPanel();
        int portToUse = ConnectionToOpponent.DEFAULT_MY_PORT;
        if(!ConnectionToOpponent.portAvailable(portToUse)) {
            portToUse++;
        }
        myPortInput = new JTextField(Integer.toString(portToUse), 7);
        reservePortPanel.add(myPortInput);
        
        reservePortButton = new JButton("Reserve Port");
        reservePortButton.setActionCommand("reservePort");
        reservePortButton.addActionListener(this);
        reservePortPanel.add(reservePortButton);
        
        add(reservePortPanel);
    }
    
    private void afterPortReserved() {
        myPortInput.setEnabled(false);
        reservePortButton.setEnabled(false);
        
        JPanel myInfoPanel = new JPanel();
        
        myInfoPanel.add(new JLabel("Your IP: " + ConnectionToOpponent.getInstance().getIP()));
        myInfoPanel.add(new JLabel("Your Port: " + ConnectionToOpponent.getInstance().getPort()));
        
        add(myInfoPanel);
        
        JPanel invitePanel = new JPanel();
        
        theirIP = new JTextField("127.0.0.1", 20);
        invitePanel.add(theirIP);
        
        theirPort = new JTextField(Integer.toString(ConnectionToOpponent.DEFAULT_MY_PORT + 1), 7);
        invitePanel.add(theirPort);
        
        inviteButton = new JButton("Invite");
        inviteButton.setActionCommand("invite");
        inviteButton.addActionListener(this);
        invitePanel.add(inviteButton);
        
        add(invitePanel);
        
        JPanel acceptPanel = new JPanel();
        
        incomingIP = new JLabel();
        acceptPanel.add(incomingIP);
        
        incomingPort = new JLabel();
        acceptPanel.add(incomingPort);
        
        acceptButton = new JButton("Accept");
        acceptButton.setActionCommand("accept");
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(this);
        acceptPanel.add(acceptButton);
        
        add(acceptPanel);
        
        Window.getInstance().pack();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        if("reservePort".equals(action.getActionCommand())){
            ConnectionToOpponent.getInstance().reservePort(Integer.parseInt(myPortInput.getText()));
            afterPortReserved();
        } else if("invite".equals(action.getActionCommand())){
            ConnectionToOpponent.getInstance().invite(theirIP.getText(), Integer.parseInt(theirPort.getText()));
        } else if("accept".equals(action.getActionCommand())){
            ConnectionToOpponent.getInstance().accept();
        }
    }

    public void incomingConnection(String hostName, int port) {
        incomingIP.setText(hostName);
        incomingPort.setText(Integer.toString(port));
        acceptButton.setEnabled(true);
    }
    
}
