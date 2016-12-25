package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Sripath Mishra  on 11/3/2016.
 * This code is for the client implementation of Foilmaker game.
 */
public class StartGame {
    public StartGame()
    {}
    JFrame MainFrame = new JFrame();
    public StartGame(String UserInfo,String GameInfo,String text,Socket socket,String User_name) {
        if(text.length()>2) {
            text = text.substring(text.indexOf('-') + 2);
            text = text.substring(0, text.indexOf('-'));
        }

        JPanel TitlePanel=new JPanel();
        TitlePanel.add(new JLabel("Foilmaker! Start Game -"+User_name));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500,50));

        JPanel MenuPanel=new JPanel();
        JButton StartGame=new JButton("Start Game");
        JTextArea players=new JTextArea(20,20);
        players.setBackground(Color.yellow);
        players.setText("--"+text);
        players.setEditable(false);
        GridBagConstraints gbc = new GridBagConstraints();
        MenuPanel.setLayout(new GridBagLayout());
        gbc.fill=GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        MenuPanel.add(new JLabel("Others should use this key to join your game:  "+GameInfo));
        gbc.gridy++;
        MenuPanel.add(players,gbc);
        gbc.gridy++;
        MenuPanel.add(StartGame, gbc);
        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500,500));

        JPanel BottomPanel=new JPanel();
        JLabel result=new JLabel("Started game you are the Leader: Please click on Start Game to start the game");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result,BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500,50));

        StartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Call(UserInfo,GameInfo,socket,MainFrame,result,User_name);

            }
        });


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
    public void Call(String UserInfo,String GameInfo,Socket socket,JFrame MainFrame,JLabel result,String User_name )
    {
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            out.println("ALLPARTICIPANTSHAVEJOINED--"+UserInfo+"--"+GameInfo);
            String input =in.readLine();
            if(input.contains("ALLPARTICIPANTSHAVEJOINED"))
            {
                if(input.contains("USERNOTLOGGEDIN")) {
                    result.setText("Invalid user token please check");
                }
                if(input.contains("INVALIDGAMETOKEN")) {
                    result.setText("Invalid game token please try again");
                }
                if(input.contains("USERNOTGAMELEADER")) {
                    result.setText("User already playing the game");
                }
            }
            else {
                MainFrame.dispose();
                new Suggestion(UserInfo,GameInfo,socket,input,User_name);
            }



        }
        catch(Exception e){}

    }

}
