package Aufgabenblatt3A2;

import java.io.IOException;

import rm.requestResponse.Component;
import rm.requestResponse.Message;
import rm.serverAdmin.ServerConfig;

public class LoadBalancer extends Thread{
	public static ServerAdmin serverAdmin;
	
    public final static int PORT = 1234;
    
    private Component communication;
    private ServerConfig serverConfig;
    private int recievePort = PORT;
    private int sendPort = PORT;
    
    public LoadBalancer () {
    	super();
    	communication = new Component();
    }

    void listenAndDistribute() {
        while (true) {
            try {
                Message msg = communication.receive(recievePort, true, true);
                sendPort = msg.getPort();
                serverConfig = serverAdmin.bind();
                communication.send(msg, serverConfig.getReceivePort(), true);
                //serverAdmin.release(serverConfig);
            } catch (ClassNotFoundException | IOException e) {
            	e.printStackTrace();
            }
        }
    }

    public void run() {
        listenAndDistribute();
    }

    public static void main(String[] args) {
        System.out.println("This Balancer listens on port [" + PORT + "]");
        //try {
        	serverAdmin = new ServerAdmin("Z:\\workspace\\REKO_Aufgabenblatt_3\\src\\Aufgabe_2\\server.conf.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.start();
    }
}
