package Server;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Created by Sripath Mishra  on 11/3/2016.
 * This code is for the server implementation of Foilmaker game.
 */

public class Server implements Runnable {
    public String password;
    public int score=0;
    public int fool=0;
    public String fooledby;
    public String right="You got it Right!";
    public int righ;
    public String fooleed;
    public int fooled=0;
    public String gtok;
    public String choice;
    public String suggest;
    public int quesnum=0;
    public Socket socket;
    public PrintWriter out;
    public String Usernaname112;
    public String Tocken111;
    public static ArrayList<Server> listOfLists1 = new ArrayList<Server>();
    public static ArrayList<List<String>> listOfLists = new ArrayList<List<String>>();
    public Server(Socket socket) {
        this.socket=socket;
    }

    public static void main(String args [])throws IOException
    {
        listOfLists.add(new ArrayList<String>());
        ServerSocket listener = new ServerSocket(8080);
        try {

            while (true) {
                Socket socket = listener.accept();
                Server s=new Server(socket);
                Thread t=new Thread(s);
                listOfLists1.add(s);
                t.start();
            }
        } finally {
            listener.close();
        }
    }

    private String input(String clientMessage) {
        String response = "";
        if (clientMessage.contains("CREATENEWUSER")) {
            response = "RESPONSE--CREATENEWUSER--";
            String data[] = clientMessage.split("--");
            if (data.length > 3)
                return response + "INVALIDMESSAGEFORMAT";
            for (int i = 0; i < data.length; i++)
                if (data[i] == null || data[i].length() < 1)
                    return response + "INVALIDMESSAGEFORMAT";
            if (data[1].length() > 10)
                return response + "INVALIDUSERNAME";
            for (int i = 0; i < data[1].length(); i++) {
                char c1 = data[1].charAt(i);
                int c = (int) c1;
                if (c < 48 || c > 123 || c == 91 || c == 92 || c == 93 || c == 94 || c == 96)
                    return response + "INVALIDUSERNAME";
                if (c > 57 && c < 65)
                    return response + "INVALIDUSERNAME";
            }
            if (data[2].length() > 10)
                return response + "INVALIDPASSWORD";
            int u = 0, n = 0;
            for (int i = 0; i < data[2].length(); i++) {
                char c = data[2].charAt(i);
                if (c < 48 || c > 123 || c == 91 || c == 92 || c == 93 || c == 94 || c == 96 || c == 95)
                    if (c != 35 || c != 38 || c != 36 || c != 42)
                        return response + "INVALIDPASSWORD";
                if (c > 57 && c < 65)
                    return response + "INVALIDPASSWORD";
                if (c < 65)
                    n += 1;
                if (c < 95)
                    u += 1;
            }
            if (u == 0 || n == 0)
                return response + "INVALIDPASSWORD";
            if (getdata(data[1]).length() < 2) {
                writeuser(data[1] + ":" + data[2] + ":0:0:0");
                return response + "SUCCESS";
            } else
                return response + "USERALREADYEXISTS";
        }
        if (clientMessage.contains("LOGIN")) {
            response = "RESPONSE--LOGIN--";
            String data[] = clientMessage.split("--");
            if (data.length > 3)
                return response + "INVALIDMESSAGEFORMAT";
            for (int i = 0; i < data.length; i++)
                if (data[i] == null || data[i].length() < 1)
                    return response + "INVALIDMESSAGEFORMAT";
            String data1 = getdata(data[1]);
            if (data1.length() < 2)
                return response + "UNKNOWNUSER";
            data1 = data1.substring(data1.indexOf(':') + 1);
            data1 = data1.substring(0, data1.indexOf(':'));
            if (data1.equals(data[2]) == false)
                return response + "INVALIDUSERPASSWORD";
            for (int i = 0; i < listOfLists.size(); i++)
                for (int j = 0; j < listOfLists.get(i).size(); j++)
                    if (listOfLists.get(i).get(j).contains(data[1]))
                        return response + "USERALREADYLOGGEDIN";
            password = data[2];
            String tocken = "";
            Random r = new Random();
            for (int i = 0; i < 10; i++) {
                int n = 0;
                while (n < 64 || n == 91 || n == 92 || n == 93 || n == 94 || n == 95 || n == 96) {
                    n = r.nextInt(123);
                }
                char c = (char) n;
                tocken += String.valueOf(c);
            }
            Tocken111 = tocken;
            listOfLists.get(0).add(data[1] + ":" + tocken);
            return response + "SUCCESS--" + tocken;
        }
        if (clientMessage.contains("STARTNEWGAME")) {
            response = "RESPONSE--STARTNEWGAME--";
            String data[] = clientMessage.split("--");
            for (int i = 0; i < listOfLists.get(0).size(); i++) {
                if (listOfLists.get(0).get(i).contains(data[1])) {
                    String tocken = "";
                    Random r = new Random();
                    for (int j = 0; j < 3; j++) {
                        int n = 0;
                        while (n < 64 || n == 91 || n == 92 || n == 93 || n == 94 || n == 95 || n == 96) {
                            n = r.nextInt(123);
                        }
                        char c = (char) n;
                        tocken += String.valueOf(c);
                    }
                    listOfLists.add(new ArrayList<String>());
                    listOfLists.get(listOfLists.size() - 1).add(tocken);
                    listOfLists.get(listOfLists.size() - 1).add(Usernaname112 + ":" + Tocken111);
                    listOfLists.get(0).remove(i);
                    gtok = tocken;
                    return response + "SUCCESS--" + tocken;
                }
            }
            for (int i = 0; i < listOfLists.size(); i++)
                for (int j = 0; j < listOfLists.get(i).size(); j++)
                    if (listOfLists.get(i).get(j).contains(data[1]))
                        return response + "Failure";
            return response + "USERNOTLOGGEDIN";
        }
        if (clientMessage.contains("JOINGAME")) {
            response = "RESPONSE--JOINGAME--";
            int n = -1;
            int game = -1;
            String data[] = clientMessage.split("--");
            for (int i = 0; i < listOfLists.get(0).size(); i++) {
                if (listOfLists.get(0).get(i).contains(data[1])) {
                    n = i;
                }
            }
            if (n == -1)
                return response + "USERNOTLOGGEDIN";
            listOfLists.get(0).remove(n);
            for (int i = 1; i < listOfLists.size(); i++)
                if (listOfLists.get(i).get(0).equals(data[2]))
                    game = i;
            if (game == -1)
                return response + "GAMEKEYNOTFOUND";
            for (int i = 0; i < listOfLists.get(game).size(); i++)
                if (listOfLists.get(game).get(i).equals(data[1]))
                    return response + "FAILURE";
            listOfLists.get(game).add(Usernaname112 + ":" + Tocken111);
            gtok = data[2];

            for (int i = 0; i < listOfLists1.size(); i++) {
                Server s1 = listOfLists1.get(i);
                if (listOfLists.get(game).get(1).contains(s1.Tocken111)) {
                    s1.out.println("NEWPARTICIPANT--" + Usernaname112 + "--0");
                    return response + "SUCCESS--" + data[2];
                }
            }
        }
        if (clientMessage.contains("ALLPARTICIPANTSHAVEJOINED")) {
            response = "RESPONSE--ALLPARTICIPANTSHAVEJOINED---";
            int n = -1;
            String data[] = clientMessage.split("--");
            for (int i = 1; i < listOfLists.size(); i++) {
                if (listOfLists.get(i).get(0).contains(data[2]))
                    n = i;
            }
            if (n == -1)
                return response + "INVALIDGAMETOKEN";
            if (listOfLists.get(n).get(1).contains(data[1]) == false)
                return response + "USERNOTGAMELEADER";
            int pn = -1;
            for (int i = 0; i < listOfLists1.size(); i++) {
                if (listOfLists1.get(i).Tocken111.equals(data[1]))
                    pn = i;
            }
            if (pn == -1)
                return response + "USERNOTLOGGEDIN";
            String ques = getques(quesnum);
            response = "NEWGAMEWORD--" + ques;
            for (int k = 0; k < listOfLists1.size(); k++) {
                if (listOfLists1.get(k).gtok.equals(gtok)) {
                    listOfLists1.get(k).out.println(response);
                }
            }
            return "wait";
        }
        if (clientMessage.contains("PLAYERSUGGESTION")) {
            response = "RESPONSE--PLAYERSUGGESTION---";
            String data[] = clientMessage.split("--");
            if (data.length != 4)
                return response + "UNEXPECTEDMESSAGETYPE";
            for (int i = 0; i < data.length; i++)
                if (data[i].length() < 2)
                    return response + "INVALIDMESSAGEFORMAT";
            int n = -1;
            for (int i = 1; i < listOfLists.size(); i++)
                if (listOfLists.get(i).get(0).contains(data[2]))
                    n = i;
            if (n == -1)
                return response + "INVALIDGAMETOKEN";
            int np = -1;
            for (int i = 1; i < listOfLists.get(n).size(); i++) {
                if (listOfLists.get(n).get(i).contains(data[1]))
                    np = i;
            }
            if (np == -1)
                return response + "USERNOTLOGGEDIN";
            String old = listOfLists.get(n).get(np);
            listOfLists.get(n).remove(np);
            suggest = data[3];
            listOfLists.get(n).add(np, old + ":" + data[3] + ":-999");
            if (np == (1)) {
                int count = listOfLists.get(n).size() - 2;
                while (count != 0) {
                    count = 0;
                    for (int i = 0; i < listOfLists1.size(); i++)
                        if (listOfLists1.get(i).suggest == null)
                            count += 1;
                }
                String suggestion[] = new String[listOfLists.get(n).size() - 1];
                for (int i = 1; i < listOfLists.get(n).size(); i++) {
                    String pp = listOfLists.get(n).get(i);
                    pp = pp.substring(pp.indexOf(':') + 1);
                    pp = pp.substring(pp.indexOf(':') + 1);
                    pp = pp.substring(0, pp.indexOf(':'));
                    suggestion[i - 1] = pp;
                }
                for (int i = 1; i < listOfLists.get(n).size(); i++) {
                    String old1 = listOfLists.get(n).get(i);
                    old1 = old1.substring(0, old1.lastIndexOf(':'));
                    listOfLists.get(n).remove(i);
                    listOfLists.get(n).add(i, old1);
                }
                Random rnd = new Random();
                for (int i = suggestion.length - 1; i > 0; i--) {
                    int index = rnd.nextInt(i + 1);
                    String a = suggestion[index];
                    suggestion[index] = suggestion[i];
                    suggestion[i] = a;
                }
                response = "ROUNDOPTIONS";
                for (int i = 0; i < suggestion.length; i++)
                    response += "--" + suggestion[i];
                String quesan[] = getques(quesnum).split("--");
                response += "--" + quesan[1];
                for (int k = 0; k < listOfLists1.size(); k++) {
                    if (listOfLists1.get(k).gtok.equals(gtok)) {
                        listOfLists1.get(k).out.println(response);
                    }
                }
                return "wait";
            }
        }
        if (clientMessage.contains("PLAYERCHOICE")) {
            righ = 0;
            fooledby = "You were fooled by";
            fooleed = "you fooled";
            response = "RESPONSE--PLAYERCHOICE----";
            String data[] = clientMessage.split("--");
            if (data.length != 4)
                return response + "UNEXPECTEDMESSAGETYPE";
            for (int i = 0; i < data.length; i++)
                if (data[i].length() < 2)
                    return response + "INVALIDMESSAGEFORMAT";
            int n = -1;
            for (int i = 1; i < listOfLists.size(); i++)
                if (listOfLists.get(i).get(0).contains(data[2]))
                    n = i;
            if (n == -1)
                return response + "INVALIDGAMETOKEN";
            int np = -1;
            for (int i = 1; i < listOfLists.get(n).size(); i++) {
                if (listOfLists.get(n).get(i).contains(data[1]))
                    np = i;
            }
            if (np == -1)
                return response + "USERNOTLOGGEDIN";
            choice = data[3];
            if (np == (1)) {
                int count = listOfLists.get(n).size() - 2;
                while (count != 0) {
                    count = 0;
                    for (int i = 0; i < listOfLists1.size(); i++)
                        if (listOfLists1.get(i).choice == null)
                            count += 1;
                }
                for (int i = 0; i < listOfLists1.size(); i++) {
                    if (listOfLists1.get(i).gtok.equals(gtok)) {
                        String data1[] = getques(quesnum).split("--");
                        if (listOfLists1.get(i).choice.equals(data1[1])) {
                            listOfLists1.get(i).score += 10;
                            listOfLists1.get(i).righ = 1;
                        }
                        for (int k = 0; k < listOfLists1.size(); k++) {
                            if (listOfLists1.get(i).gtok.equals(gtok))
                                if (listOfLists1.get(i).suggest.equals(listOfLists1.get(k).choice)) {
                                    listOfLists1.get(i).score += 5;
                                    listOfLists1.get(i).fooled += 1;
                                    listOfLists1.get(k).fool += 1;
                                    listOfLists1.get(i).fooleed += " " + listOfLists1.get(k).Usernaname112;
                                    listOfLists1.get(k).fooledby += " " + listOfLists1.get(i).Usernaname112;
                                }
                        }
                    }
                }
                response = "ROUNDRESULT";
                for (int k = 0; k < listOfLists1.size(); k++) {
                    if (listOfLists1.get(k).gtok.equals(gtok)) {
                        writeuser1(listOfLists1.get(k).Usernaname112 + ":" + listOfLists1.get(k).password + ":" + listOfLists1.get(k).score + ":" + listOfLists1.get(k).fooled + ":" + listOfLists1.get(k).fool, listOfLists1.get(k).Usernaname112);
                        if (listOfLists1.get(k).righ > 0) {
                            if (listOfLists1.get(k).fooled > 0)
                                response += "--" + listOfLists1.get(k).Usernaname112 + "--" + listOfLists1.get(k).right + ". " + listOfLists1.get(k).fooleed + "--" + listOfLists1.get(k).score + "--" + listOfLists1.get(k).fool + "--" + listOfLists1.get(k).fooled;
                            else
                                response += "--" + listOfLists1.get(k).Usernaname112 + "--" + listOfLists1.get(k).right + ". " + "--" + listOfLists1.get(k).score + "--" + listOfLists1.get(k).fool + "--" + listOfLists1.get(k).fooled;
                            listOfLists1.get(k).righ = 0;
                        } else {
                            if (listOfLists1.get(k).fooled > 0)
                                response += "--" + listOfLists1.get(k).Usernaname112 + "--" + listOfLists1.get(k).fooledby + ". " + listOfLists1.get(k).fooleed + "--" + listOfLists1.get(k).score + "--" + listOfLists1.get(k).fool + "--" + listOfLists1.get(k).fooled;
                            else
                                response += "--" + listOfLists1.get(k).Usernaname112 + "--" + listOfLists1.get(k).fooledby + ". " + "--" + listOfLists1.get(k).score + "--" + listOfLists1.get(k).fool + "--" + listOfLists1.get(k).fooled;

                        }
                        listOfLists1.get(k).suggest=null;
                    }

                    }
                    for (int k = 0; k < listOfLists1.size(); k++) {
                        if (listOfLists1.get(k).gtok.equals(gtok)) {
                            listOfLists1.get(k).out.println(response);
                        }
                    }
                    quesnum += 1;
                    String ques = getques(quesnum);
                    if (ques.length() < 3)
                        response = "GAMEOVER";
                    else
                        response = "NEWGAMEWORD--" + ques;
                    for (int k1 = 0; k1 < listOfLists1.size(); k1++) {
                        if (listOfLists1.get(k1).gtok.equals(gtok)) {
                            listOfLists1.get(k1).out.println(response);
                        }
                    }
                }
            }
            return "wait";
    }

    private void writeuser1(String s, String usernaname112) {
        File f = new File("C:\\Users\\sripa\\Desktop\\CS 180BLK\\Project 5\\src","Userdata.txt");
        try {
            String write="";
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if(line.contains(usernaname112))
                    write+="\n"+s;
                else
                    write+="\n"+line;
            }
            f.delete();
            f.createNewFile();
            FileOutputStream out2 = new FileOutputStream(f,false);
            PrintWriter p=new PrintWriter(out2);
            p.println(write);
            p.close();
            out2.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getques(int quesnum) {
        File f = new File("C:\\Users\\sripa\\Desktop\\CS 180BLK\\Project 5\\src","Quesdata.txt");
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (quesnum==0)
                    return line;
                quesnum-=1;
            }
            in.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return"";
    }

    private String getdata(String s) {
        File f = new File("C:\\Users\\sripa\\Desktop\\CS 180BLK\\Project 5\\src","Userdata.txt");
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains(s))
                    return line;
            }
            in.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return"";
    }

    private void writeuser(String s) {
        File f = new File("C:\\Users\\sripa\\Desktop\\CS 180BLK\\Project 5\\src","Userdata.txt");
        try {
            FileOutputStream out2 = new FileOutputStream(f,true);
            PrintWriter p=new PrintWriter(out2);
            p.println(s);
            p.close();
            out2.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            out = new PrintWriter(socket.getOutputStream(), true);
            String clientMessage = in.readLine();
            String output = input(clientMessage);
            out.println(output);
            String user[]=clientMessage.split("--");
            Usernaname112=user[1];
        while (true) {
            String clientMessage1 = in.readLine();
            String output1 = input(clientMessage1);
            if(output1.equals("wait")==false)
                out.println(output1);
            if (clientMessage1.contains("LOGOUT"))
                break;
        }
        in.close();
            out.close();
        } catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
