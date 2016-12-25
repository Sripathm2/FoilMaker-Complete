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
public class Suggestion {
    public Suggestion(String UserInfo,String GameInfo,Socket socket,String ques,String User_name) {
        ques=ques.substring(13);
        String ans =ques.substring(ques.lastIndexOf('-')+1);
        ques=ques.substring(0,ques.lastIndexOf('-')-2);

        JFrame MainFrame = new JFrame();
        JPanel TitlePanel=new JPanel();
        TitlePanel.add(new JLabel("Foilmaker!! Suggestion - "+User_name));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500,50));

        JPanel MenuPanel=new JPanel();
        JButton Suggest=new JButton("Submit Suggestion");
        JTextArea Ques=new JTextArea(20,35);
        Ques.setBackground(Color.orange);
        Ques.setText(ques);
        Ques.setEditable(false);
        JTextField answer=new JTextField(20);
        GridBagConstraints gbc = new GridBagConstraints();
        MenuPanel.setLayout(new GridBagLayout());
        gbc.fill=GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        MenuPanel.add(new JLabel("What is a word for?"));
        gbc.gridy++;
        MenuPanel.add(Ques,gbc);
        gbc.gridy++;
        JPanel sug= new JPanel(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.fill=GridBagConstraints.RELATIVE;
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        sug.add(new JLabel("Your Suggestion:"),gbc1);
        gbc1.gridy++;
        sug.add(answer,gbc1);
        gbc1.gridy++;
        sug.add(Suggest,gbc1);
        sug.setBorder(BorderFactory.createTitledBorder(""));
        sug.setPreferredSize(new Dimension(455,460));
        MenuPanel.add(sug,gbc);
        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500,500));

        JPanel BottomPanel=new JPanel();
        JLabel result=new JLabel("Enter a Submission");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result,BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500,50));

        Suggest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(answer.getText().equals(ans))
                {
                    result.setText("Please Change your Suggestion");
                }
                else {
                    call(UserInfo,GameInfo,socket,result,MainFrame,answer.getText(),User_name);
                }



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

    public void call(String Userinfo,String Gameinfo, Socket socket,JLabel result, JFrame MainFrame, String suggest,String User_name) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            out.println("PLAYERSUGGESTION--"+Userinfo+"--"+Gameinfo+"--"+suggest);
            String input = in.readLine();
            if (input.contains("USERNOTLOGGEDIN"))
                result.setText("Unsuccessful Invalid user token");
            if (input.equals("INVALIDGAMETOKEN"))
                result.setText("Unsuccessful Invalid Game Tocken.");
            if (input.contains("UNEXPECTEDMESSAGETYPE"))
                result.setText("A suggestion was sent when a different message was expected by the server");
            if (input.equals("INVALIDMESSAGEFORMAT"))
                result.setText("Message format is not according to what is given above.");
            else {
                MainFrame.dispose();
                new Question(Userinfo,Gameinfo,socket,input,User_name);
            }
        }
        catch (Exception e){}
    }
}
