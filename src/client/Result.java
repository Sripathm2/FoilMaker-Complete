package client;

import com.sun.prism.paint.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
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
public class Result {
    public Result(String UserInfo,String GameInfo,Socket socket,String ques,String User_name) {

        ques=ques.substring(ques.indexOf('-')+2);
        String info[]=ques.split("--");
        if(info[0].equalsIgnoreCase(User_name))
            ques=info[1];
        else
            ques=info[6];

        String text="=>"+info[0]+" || Score: "+info[2]+" || Fooled :"+info[3]+"(players)|| Fooled By :"+info[4]+"(players)";
        text=text+"\n=>"+info[5]+" || Score: "+info[7]+" || Fooled :"+info[8]+"(players)|| Fooled By :"+info[9]+"(players)";
        JFrame MainFrame = new JFrame();
        JPanel TitlePanel=new JPanel();
        TitlePanel.add(new JLabel("Foilmaker!! Result - "+User_name));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500,50));

        JPanel MenuPanel=new JPanel();
        JButton Next=new JButton("Next Round");
        JTextArea Ques=new JTextArea(10,40);
        Ques.setBorder(BorderFactory.createTitledBorder("Round Result:"));
        Ques.setText(ques);
        Ques.setEditable(false);
        Ques.setBackground(Color.orange);
        JTextArea over=new JTextArea(10,40);
        over.setBorder(BorderFactory.createTitledBorder("Overall Results :"));
        over.setText(text);
        over.setEditable(false);
        over.setBackground(Color.red);
        MenuPanel.setLayout(new BorderLayout());
        MenuPanel.add(Ques,BorderLayout.NORTH);
        MenuPanel.add(over,BorderLayout.CENTER);
        MenuPanel.add(Next,BorderLayout.SOUTH);
        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500,500));

        JPanel BottomPanel=new JPanel();
        JLabel result=new JLabel("Results click on next for next round");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result,BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500,50));

        Next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                call(UserInfo,GameInfo,socket,MainFrame,result,User_name);



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

    public void call(String Userinfo,String Gameinfo, Socket socket, JFrame MainFrame,JLabel result,String User_name) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String input = in.readLine();
            if(input.contains("GAMEOVER"))
            {
                result.setText("No more Questions avaliable =(");
            }
            else {
                MainFrame.dispose();
                new Suggestion(Userinfo,Gameinfo,socket,input,User_name);
            }
        }
        catch (Exception e){}
    }
}
