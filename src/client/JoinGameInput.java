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
public class JoinGameInput extends JoinGameWait {
    public JoinGameInput(String UserInfo,Socket socket,String User_name) {
        super();
        JFrame MainFrame = new JFrame();
        JPanel TitlePanel=new JPanel();
        TitlePanel.add(new JLabel("Foilmaker!! Join Game - "+User_name));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500,50));

        JPanel MenuPanel=new JPanel();
        JButton JoinGame=new JButton("Join Game");
        JTextField Gameinfo=new JTextField(5);
        GridBagConstraints gbc = new GridBagConstraints();
        MenuPanel.setLayout(new GridBagLayout());
        gbc.fill=GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        MenuPanel.add(new JLabel("Enter The Game Key to Join the Game:"));
        gbc.gridy++;
        MenuPanel.add(Gameinfo,gbc);
        gbc.gridy++;
        MenuPanel.add(JoinGame, gbc);
        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500,500));

        JPanel BottomPanel=new JPanel();
        JLabel result=new JLabel("Welcome!");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result,BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500,50));

        JoinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String GameInfo=Gameinfo.getText();
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
    public void Call(String UserInfo,String GameInfo,Socket socket,JFrame Mainframe,JLabel result,String User_name)
    {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            out.println("JOINGAME--"+UserInfo+"--"+GameInfo);
            String input = in.readLine();
            input=input.substring(20);
            input=input.substring(0,input.indexOf('-'));
            if(input.equals("USERNOTLOGGEDIN"))
                result.setText("Unsuccessful Invalid user token");
            else if(input.equals("GAMEKEYNOTFOUND"))
                result.setText("Unsuccessful Game user token");
            else if (input.equals("FAILURE"))
                result.setText("Unsuccessful YOu are already playing this game.");
            else {
                Mainframe.dispose();
                JoinGameWait ob=new JoinGameWait(1,socket);
                new Thread() {
                    public void run() {
                        try {

                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                            BufferedReader in = new BufferedReader(isr);
                            String input = in.readLine();
                            ob.MainFrame.dispose();
                            new Suggestion(UserInfo,GameInfo,socket,input,User_name);

                        } catch (Exception e) {
                        }
                    }
                }.start();
                //call ques frame

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
