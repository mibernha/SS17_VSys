package Aufgabenblatt1A2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import rm.requestResponse.*;

public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final String REQUEST_MODE = "SYNCHRONIZED";

    private Component communication;
    String hostname, requestMode;
    int port;
    long initialValue, count;

    public PrimeClient(String hostname, int port, String requestMode, long initialValue, long count) {
        this.hostname = hostname;
        this.port = port;
        this.requestMode = requestMode;
        this.initialValue = initialValue;
        this.count = count;
    }

    public void run() {

        try {
            executeProcessNumber();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //System.err.println(e);
        }
    }

    public void executeProcessNumber() throws ClassNotFoundException, IOException {
        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            processNumber(i);
        }
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {

        Boolean blocking = true;
        Boolean isNull = false;
        String points = "";

        if (requestMode.equals("NOT")) {
            blocking = false;
        }

        communication.send(new Message(hostname, port, new Long(value)), false);
        System.out.print(value + ": ");

        do {

            try {
                Boolean isPrime = (Boolean) communication.receive(port, blocking, true).getContent();
                System.out.println((isPrime.booleanValue() ? " prime" : " not prime"));
                System.out.println(Thread.currentThread().getName());

                isNull = false;
            } catch (NullPointerException e) {

                try {
                    Thread.sleep(1000);
                    isNull = true;
                    points = points + ".";
                    System.out.print(points);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
        } while (isNull);
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        String requestMode = REQUEST_MODE;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
            System.out.print("Server hostname [" + hostname + "] > ");
            input = reader.readLine();
            if (!input.equals("")) hostname = input;

            System.out.print("Server port [" + port + "] > ");
            input = reader.readLine();
            if (!input.equals("")) port = Integer.parseInt(input);

            System.out.print("Request mode [" + requestMode + "] > ");
            input = reader.readLine();
            if (!input.equals("")) requestMode = input;

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);

            if (requestMode.equals("SYNCHRONIZED")) {
                System.out.println("-----> SYNCHRONIZED <-----");

                int threads = Runtime.getRuntime().availableProcessors();
                System.out.println("availableProcessors: " + threads);
                ExecutorService service = Executors.newFixedThreadPool(threads);
                for (int i = 0; i < count; i++) {
                    service.execute(new PrimeClient(hostname, port, requestMode, initialValue, count));
                }

                service.shutdown();
                try {
                    service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }

            } else {
                new PrimeClient(hostname, port, requestMode, initialValue, count).run();
            }

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }

    }
}



