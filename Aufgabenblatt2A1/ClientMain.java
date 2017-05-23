package Aufgabenblatt2A1;

import java.lang.management.ManagementFactory;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
import java.util.Scanner;

public class ClientMain extends Thread {
	private static final int port=1234;
	private static final String hostname="localhost";
	public boolean goon = true;


	public static void main(String args[]) {
		new ClientComm(port,hostname, ManagementFactory.getRuntimeMXBean().getName()).start();
		new ClientMain().start();
		
	}
	
	public void run() {
		while(goon) {
			System.out.println("Enter (e)xit to end the program");
			Scanner scanner = new Scanner(System.in);
			String abort = scanner.nextLine();
			if(abort.equals("e")){
				goon = false;
				ClientComm.running = false;
				scanner.close();
			}
		}
	}
}
