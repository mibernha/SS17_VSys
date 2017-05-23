package Aufgabenblatt2A1;

import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.Random;

public class ClientComm extends Thread {
	private static MySocketClient client;
	public int port;
	public String hostname;
	public static boolean running = true;
	public int nr = 0;
	public String pid;
	
	public ClientComm(int port, String hostname, String pid){
		this.port = port;
		this.hostname = hostname;
		this.pid = pid;
	}
	
	public void schlafen(){
		Random rnd = new Random();
		try {
			Thread.sleep(rnd.nextInt(2)*5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			while(running) {
				client = new MySocketClient(hostname, port);
				client.sendAndReceive("Test-" + nr++ + " PID: " + pid);
				schlafen();
				client.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
