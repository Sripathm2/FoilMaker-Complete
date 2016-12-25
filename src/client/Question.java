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
public class Question {
    String answer="";
    public Question(String UserInfo,String GameInfo,Socket socket,String option,String User_name) {

        String options[]=new String[3];
        option=option.substring(option.indexOf('-')+2);
        options[0]=option.substring(0,option.indexOf('-'));
        option=option.substring(option.indexOf('-')+2);
        options[1]=option.substring(0,option.indexOf('-'));
        option=option.substring(option.indexOf('-')+2);
        options[2]=option.substring(0);

        JFrame MainFrame = new JFrame();
        JPanel TitlePanel=new JPanel();
        TitlePanel.add(new JLabel("FoilMaker!! Question - "+User_name));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500,50));

        JPanel MenuPanel=new JPanel();
        JRadioButton button0=new JRadioButton(options[0]);
        JRadioButton button1=new JRadioButton(options[1]);
        JRadioButton button2=new JRadioButton(options[2]);
        JButton Submit=new JButton("Submit Option");
        MenuPanel.setLayout(new BorderLayout());
        MenuPanel.add(new JLabel("                                                         Pick your option below"),BorderLayout.NORTH);
        JPanel sug= new JPanel(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.fill=GridBagConstraints.RELATIVE;
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        sug.add(button0,gbc1);
        gbc1.gridy++;
        sug.add(button1,gbc1);
        gbc1.gridy++;
        sug.add(button2,gbc1);
        gbc1.gridy++;
        MenuPanel.add(sug,BorderLayout.CENTER);
        MenuPanel.add(Submit,BorderLayout.SOUTH);

        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500,500));
        button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                answer=options[0];
                button1.setSelected(false);
                button2.setSelected(false);
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                answer=options[1];
                button0.setSelected(false);
                button2.setSelected(false);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                answer=options[2];
                button0.setSelected(false);
                button1.setSelected(false);
            }
        });

        JPanel BottomPanel=new JPanel();
        JLabel result=new JLabel("Choose an Answer");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result,BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500,50));

        Submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                call(UserInfo,GameInfo,socket,MainFrame,result,answer,User_name);

            }
        });


        MainFrame.setPreferredSize(new Dimension(500,1000));
        MainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try{
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("LOGOUT--");
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
    public void call(String Userinfo,String Gameinfo, Socket socket, JFrame MainFrame,JLabel result, String answer,String User_name) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            out.println("PLAYERCHOICE--"+Userinfo+"--"+Gameinfo+"--"+answer);
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
                new Result(Userinfo,Gameinfo,socket,input,User_name);
            }
        }
        catch (Exception e){}
    }

}

