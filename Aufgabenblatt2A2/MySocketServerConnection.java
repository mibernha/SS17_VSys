package Aufgabenblatt2A2;
import java.io.*;
import java.net.*;
 
public class MySocketServerConnection extends Thread {
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private BufferedReader filereader;
	private PrintWriter serverlog;
	private String logfile;
	
	public MySocketServerConnection(Socket socket, String logfile) throws IOException {
		this.logfile = logfile;
		this.socket = socket;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream());
		serverlog = new PrintWriter(new BufferedWriter(new FileWriter(logfile)));
		serverlog.println("Server: incoming connection from " + socket.getRemoteSocketAddress().getClass().getName() + " accepted.");
		System.out.println("Server: incoming connection from " + socket.getRemoteSocketAddress().getClass().getName() + " accepted.");
	}
	
	public void run() {
		serverlog.println("Server: waiting for message ...");
		System.out.println("Server: waiting for message ...");	
		try {
			boolean error = false;
			String in;
			String path = "C:\\Users\\Micha\\SoTe2\\ReKo_Uebung02\\src\\Aufgabe3";
			String url;
			output.println("HTTP/1.1 200 OK");
			output.println("Content-Type: text/html");
			output.println("");
			
			in = input.readLine();
			url = in.substring(4,in.length()-9);
			
			if(url.equals("/users/test")){
				path += url + ".html";
			} else if(url.equals("/test")){
				path += url + ".html";
			} else {
				error = true;
			}
			if(!error) {
				filereader = new BufferedReader(new FileReader(path));
				String line;
				while((line = filereader.readLine()) != null){
					output.println(line);
				}
				filereader.close();
			} else {
				output.println("<h1>Error 404: Page with url " + url + " not found!</h1>");
				output.println("<p>Hier k�nnte Ihre Werbung stehen!</p>");
				serverlog.println("Error 404 - Seite konnte nicht gefunden werden.");
			}
			serverlog.println("Page on url " + path +  "printed.");
		}
		catch(Exception e) {
			serverlog.println("Error: " + e.getMessage());
		}
		try {
			serverlog.flush();
			serverlog.close();
			output.flush();
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
