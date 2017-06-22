package Aufgabenblatt3A2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import rm.serverAdmin.ServerConfig;

public class ServerAdmin {
	
	List<ServerConfig> serverList = new ArrayList<ServerConfig>();
	int[] requestCounter;
	//static int counter;
	
	public ServerAdmin(String filePath) {
		File file = new File(filePath);
		readFile(file);
		requestCounter = new int[serverList.size()];
		for (int i = 0; i < requestCounter.length; i++) {
			requestCounter[i] = 0;
		}
		//counter = 0;
	}
	
	private void readFile(File file) {
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(input.hasNext()) {
		    String nextLine = input.nextLine();
		    String hostname;
		    String receivePort;
		    String sendPort;
		    int index_1 = 0;
		    int index_2 = 0;
		    int index_3 = 0;
		    int counter = 0;
		    for (int i = 0; i < nextLine.length(); i++) {
		    	char c = nextLine.charAt(i);
		    	if (c == ' ' && counter == 0) {
		    		index_1 = i;
					counter++;
				}
		    	if (c == ' ' && counter == 1) {
					index_2 = i;
				}
		    	index_3 = i;
		    }
		    hostname = nextLine.substring(0, index_1);
		    receivePort = nextLine.substring(index_1 + 1, index_2);
		    sendPort = nextLine.substring(index_2 + 1, index_3 + 1);
		    ServerConfig serverConfig = new ServerConfig(hostname, new Integer(receivePort), new Integer(sendPort));
		    serverList.add(serverConfig);
		}
		input.close();
	}
	
	public ServerConfig bind() {
//		int index = 0;
//		ServerConfig serverConfig = null;
//		for (ServerConfig serverList : serverList) {
//			if (index == counter) {
//				
//			}
//			index++;
//		}
//		counter++;
//		if (counter >= serverList.size()) {
//			counter = 0;
//		}
		
		int min = Integer.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < requestCounter.length; i++) {
			if(requestCounter[i] <= min) {
				min = requestCounter[i];
				index = i;
			}
		}
		System.out.println("MIN: " + min);
		System.out.println("INDEX: " + index);
		
		int counter = 0;
		ServerConfig temp = null;
		for (ServerConfig serverList : serverList) {
			if (counter == index) {
				index = counter;
				temp = serverList;
			}
			counter ++;
		}
		requestCounter[index]++;
		return temp;
	}
	
	public void release(ServerConfig config) {
		int counter = 0;
		for (ServerConfig serverList : serverList) {
			if (serverList.getHostname().equals(config.getHostname()) &&
					serverList.getReceivePort() == config.getReceivePort() &&
					serverList.getSendPort() == config.getSendPort()) {
				requestCounter[counter]--;
			}		
			counter ++;
		}
	}
}
