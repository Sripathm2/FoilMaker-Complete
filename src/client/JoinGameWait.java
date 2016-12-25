package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Sripath Mishra  on 11/3/2016.
 * This code is for the client implementation of Foilmaker game.
 */
public class JoinGameWait {
    JFrame MainFrame = new JFrame();
    public JoinGameWait()
    {}
    public JoinGameWait(int n,Socket socket) {

        JPanel TitlePanel=new JPanel();
        TitlePanel.add(new JLabel("Join Game"));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500,50));

        JPanel MenuPanel=new JPanel();;
        GridBagConstraints gbc = new GridBagConstraints();
        MenuPanel.setLayout(new GridBagLayout());
        gbc.fill=GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        MenuPanel.add(new JLabel("Waiting For the Leader:"));;
        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500,500));

        JPanel BottomPanel=new JPanel();
        JLabel result=new JLabel("Joined Game: Waiting for Leader to start");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result,BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500,50));

        MainFrame.setPreferredSize(new Dimension(500,1000));
        MainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try{
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("LOGOUT--");
                    MainFrame.dispose();
                }catch(Exception e)
                {}

            }
        });
        MainFrame.setLayout(new BorderLayout());
        MainFrame.pack();
        MainFrame.setLocationRelativeTo(null);
        MainFrame.add(TitlePanel,BorderLayout.NORTH);
        MainFrame.add(MenuPanel,BorderLayout.CENTER);
        MainFrame.add(BottomPanel,BorderLayout.SOUTH);
        MainFrame.setTitle("Foilmaker");
        MainFrame.setVisible(true);


    }
}
