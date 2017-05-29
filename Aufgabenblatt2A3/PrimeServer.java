package Aufgabenblatt2A3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

import rm.requestResponse.*;

public class PrimeServer extends Thread {
	private final static int PORT=1234;
	private final static Logger LOGGER=Logger.getLogger(PrimeServer.class.getName());
    private static Boolean fixedThreadPool = true;

	private Component communication;
	private int port = PORT;
	private ThreadCounter counter;
	private ExecutorService exec;

    public PrimeServer(int port, Boolean fixed) {
        counter = new ThreadCounter();
        counter.start();
    	communication = new Component();
    	fixedThreadPool = fixed;
    	if(port>0) this.port=port;
//    	setLogLevel(Level.FINER);
    }

    public void run() {
    	listen();
	}

	void setLogLevel(Level level) {
    	for(Handler h : LOGGER.getLogger("").getHandlers())	h.setLevel(level);
    	LOGGER.setLevel(level);
    	LOGGER.info("Log level set to "+level);
    }

    void listen() {

    	LOGGER.info("Listening on port "+port);

    	while (true) {
    		Long request=null;

    		LOGGER.finer("Receiving ...");
    		try {
    		    request = (Long) communication.receive(port, true, true).getContent();
    			System.out.println("ACTIVE THREADS: " + counter.getCounter() + ": " + request);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
    		LOGGER.fine(request.toString()+" received.");

    		LOGGER.finer("Sending ...");
    		if(fixedThreadPool) {
    		    exec = Executors.newFixedThreadPool(10);
            } else {
    		    exec = Executors.newCachedThreadPool();
            }
            PrimeService ps = new PrimeService(request.longValue(), port, communication);
    		exec.execute(ps);
    	}
    }

    public static void main(String[] args) {
    	int port=0;
    	for (int i = 0; i<args.length; i++)  {
			switch(args[i]) {
				case "-port":
					try {
				        port = Integer.parseInt(args[++i]);
				    } catch (NumberFormatException e) {
				    	LOGGER.severe("port must be an integer, not "+args[i]);
				        System.exit(1);
				    }
					break;
				default:
					LOGGER.warning("Wrong parameter passed ... '"+args[i]+"'");
			}
        }
        String input = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("FixedThreadPool [true] > ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!input.equals("")) {
            if(!input.equals("true")) {
                fixedThreadPool = false;
            }
        }
    	new PrimeServer(port, fixedThreadPool).run();
    }
}
