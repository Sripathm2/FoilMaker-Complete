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
public class Menu extends StartGame {
    public Menu(String Userinfo, Socket socket,String User_name) {
        super();
        JFrame MainFrame = new JFrame();
        JPanel TitlePanel = new JPanel();
        TitlePanel.add(new JLabel("FoilMaker! Menu -"+User_name));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500, 50));

        JPanel MenuPanel = new JPanel();
        JButton NewGame = new JButton("Start Game");
        JButton JoinGame = new JButton("Join Game");
        GridBagConstraints gbc = new GridBagConstraints();
        MenuPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        MenuPanel.add(NewGame, gbc);
        MenuPanel.add(new JLabel("               "), gbc);
        gbc.gridx++;
        MenuPanel.add(JoinGame, gbc);
        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500, 500));

        JPanel BottomPanel = new JPanel();
        JLabel result = new JLabel("");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result, BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500, 50));

        NewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CallStartGame(Userinfo, socket, MainFrame, result,User_name);
            }
        });
        JoinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CallJoinGame(Userinfo, socket, MainFrame,User_name);
            }
        });


        MainFrame.setPreferredSize(new Dimension(500, 1000));
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
        MainFrame.add(TitlePanel, BorderLayout.NORTH);
        MainFrame.add(MenuPanel, BorderLayout.CENTER);
        MainFrame.add(BottomPanel, BorderLayout.SOUTH);
        MainFrame.setTitle("Foilmaker");
        MainFrame.setVisible(true);


    }

    public void CallStartGame(String Userinfo, Socket socket, JFrame MainFrame, JLabel result,String User_name) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            out.println("STARTNEWGAME--" + Userinfo);
            String input = in.readLine();
            input = input.substring(23);
            String GameInfo = input.substring(input.indexOf('-') + 10);
            input = input.substring(0, input.indexOf('-'));
            if (input.equals("USERNOTLOGGEDIN"))
                result.setText("Unsuccessful Invalid user token");
            if (input.equals("FAILURE"))
                result.setText("Unsuccessful YOu are already playing or internal Failure.");
            else {
                MainFrame.dispose();
                String text="";
                int n=3;
                final StartGame ob = new StartGame(Userinfo, GameInfo, text, socket,User_name);
                new Thread() {
                    public void run() {
                        try {

                            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                            BufferedReader in = new BufferedReader(isr);
                            String input = in.readLine();
                            ob.MainFrame.dispose();
                            StartGame on = new StartGame(Userinfo, GameInfo, input, socket,User_name);

                        } catch (Exception e) {
                        }
                    }
                }.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CallJoinGame(String UserInfo, Socket socket, JFrame MainFrame,String User_name) {
        MainFrame.dispose();
        new JoinGameInput(UserInfo,socket,User_name);
    }
}

