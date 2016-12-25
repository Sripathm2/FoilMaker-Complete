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
public class Login {
    public Login() {
        JFrame MainFrame = new JFrame();
        JPanel TitlePanel=new JPanel();
        TitlePanel.add(new JLabel("Foilmaker!!"));
        TitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        TitlePanel.setBorder(BorderFactory.createTitledBorder(""));
        TitlePanel.setPreferredSize(new Dimension(500,50));

        JPanel MenuPanel=new JPanel();
        JButton Login=new JButton("Login");
        JButton Register=new JButton("Register");
        JTextField Username=new JTextField(20);
        JPasswordField Password=new JPasswordField(20);
        GridBagConstraints gbc = new GridBagConstraints();
        MenuPanel.setLayout(new GridBagLayout());
        gbc.fill=GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        MenuPanel.add(new JLabel("Username"));
        gbc.gridx++;
        MenuPanel.add(new JLabel("               "),gbc);
        gbc.gridx++;
        MenuPanel.add(Username,gbc);
        gbc.gridx-=2;
        gbc.gridy++;
        MenuPanel.add(new JLabel("               "),gbc);
        gbc.gridy++;
        MenuPanel.add(new JLabel("Password"), gbc);
        gbc.gridx++;
        MenuPanel.add(new JLabel("               "),gbc);
        gbc.gridx++;
        MenuPanel.add(Password,gbc);
        gbc.gridx-=2;
        gbc.gridy++;
        MenuPanel.add(new JLabel("               "),gbc);
        gbc.gridy++;
        MenuPanel.add(Login, gbc);
        MenuPanel.add(new JLabel("               "),gbc);
        gbc.gridx++;
        MenuPanel.add(new JLabel("               "),gbc);
        gbc.gridx++;
        MenuPanel.add(Register,gbc);
        MenuPanel.setBorder(BorderFactory.createTitledBorder(""));
        MenuPanel.setPreferredSize(new Dimension(500,500));

        JPanel BottomPanel=new JPanel();
        JLabel result=new JLabel("Log in or Register");
        result.setText("Welcome!");
        BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel.add(result,BorderLayout.CENTER);
        BottomPanel.setBorder(BorderFactory.createTitledBorder(""));
        BottomPanel.setPreferredSize(new Dimension(500,50));

        Login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String User_name=Username.getText();
                String password =String.valueOf(Password.getPassword());
                Call(User_name,password,1,MainFrame,result);
            }
        });
        Register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String User_name="";
                String password="";
                User_name=Username.getText();
                password =String.valueOf(Password.getPassword());
                Call(User_name,password,2,MainFrame,result);
            }
        });


        MainFrame.setPreferredSize(new Dimension(500,1000));
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.setLayout(new BorderLayout());
        MainFrame.pack();
        MainFrame.setLocationRelativeTo(null);
        MainFrame.add(TitlePanel,BorderLayout.NORTH);
        MainFrame.add(MenuPanel,BorderLayout.CENTER);
        MainFrame.add(BottomPanel,BorderLayout.SOUTH);
        MainFrame.setTitle("Foilmaker");
        MainFrame.setVisible(true);


    }
    public void Call(String User_name,String password,int button,JFrame MainFrame,JLabel result)
    {
        String serverIP = "localhost";
        int serverPort = 8080;
        User_name=User_name.trim();
        password=password.trim();
        try {
            Socket socket = new Socket(serverIP, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            if(button==2)
                out.println("CREATENEWUSER--"+User_name+"--"+password);
            else
                out.println("LOGIN--"+User_name+"--"+password);
            String serverMessage = in.readLine();
            Next(serverMessage,button,socket,MainFrame,result,User_name);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Next(String input,int button,Socket socket,JFrame MainFrame,JLabel result,String User_name)
    {
        if(button==2)
            input=input.substring(25);
        else
            input=input.substring(17);
        String output=input;
        input=input.substring(0,input.indexOf('-'));
        output=output.substring(output.indexOf('-')+2);
        if(input.equals("INVALIDMESSAGEFORMAT"))
            result.setText("Unsuccessful please check the format.");
        if(input.equals("INVALIDUSERNAME"))
            result.setText("Unsuccessful please enter the Username.");
        if((input.equals("INVALIDUSERPASSWORD"))&&(button==1))
            result.setText("Unsuccessful please enter the Password.");
        if(input.equals("USERALREADYEXISTS"))
            result.setText("Unsuccessful this username exists please take another.");
        if(input.equals("UNKNOWNUSER"))
            result.setText("Unsuccessful please register.");
        if((input.equals("INVALIDUSERPASSWORD"))&&(button==2))
            result.setText("Unsuccessful Wrong password.");
        if(input.equals("USERALREADYLOGGEDIN"))
            result.setText("Unsuccessful You are logged in from a different device or game.");
        if((input.equals("SUCCESS"))&&(button==2))
            result.setText("Successful Please login.");
        if((input.equals("SUCCESS"))&&(button==1))
        {
            MainFrame.dispose();
            Menu ob=new Menu(output,socket,User_name);
        }

    }
    public static void main(String args[])
    {
        new Login();
    }



}

