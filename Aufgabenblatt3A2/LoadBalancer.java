package Aufgabenblatt3A2;

import com.sun.security.ntlm.Server;
import rm.requestResponse.Component;
import rm.requestResponse.Message;
import rm.serverAdmin.ServerAdmin;
import rm.serverAdmin.ServerConfig;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by MB_SP on 13.06.2017.
 */

public class LoadBalancer extends Thread {
    public static ServerAdmin sa;
    public static int PORT = 1234;
    public static int SENDPORT = 1234;

    private int port = PORT;
    private int sendPort = SENDPORT;
    private Component communication;

    public LoadBalancer() {

    }

    void listenAndDistribute() {
        while (true) {
            try {
                Message msg = communication.receive(port, true, true);
                sendPort = msg.getPort();
            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
            }
        }
    }


    public void run() {
        listenAndDistribute();
    }

    public static void main(String[] args) {
        System.out.println("This Balancer listens on port [" + PORT + "]");
        try {
            sa = new ServerAdmin("C:\\Users\\MB_SP\\Desktop\\VSys\\SS17_VSys\\Aufgabenblatt3A2\\server.conf.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoadBalancer lb = new LoadBalancer();
        lb.start();

    }
}
