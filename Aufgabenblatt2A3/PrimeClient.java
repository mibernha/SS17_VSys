package Aufgabenblatt2A3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import rm.requestResponse.*;

public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final int CLIENTS = 10;

    private Component communication;
    String hostname;
    int port, clients;
    long initialValue, count;

    public PrimeClient(String hostname, int port, long initialValue, long count) {
        this.hostname = hostname;
        this.port = port;
        this.initialValue = initialValue;
        this.count = count;
    }

    public void run() {
        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            try {
                processNumber(i);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {

        Boolean blocking = false;
        Boolean isNull = false;

        communication.send(new Message(hostname, port, new Long(value)), port, true);

        System.out.print(CLIENT_NAME + " " + value + " ");

        //TODO An dieser Stelle neuen Port festlegen und Kommunikation dann darüber
        do {
            try {
//                System.out.println(communication.receive(1234,blocking,true).getContent());
                Boolean isPrime = (Boolean) communication.receive(port, blocking, true).getContent();
                System.out.print((isPrime.booleanValue() ? " prime" : " not prime"));
                isNull = false;
            } catch (NullPointerException e) {
                try {
                    Thread.sleep(1000);
                    isNull = true;
                    System.out.print(".");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
        } while (isNull);
        System.out.println();
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;
        int clients = CLIENTS;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
//            System.out.print("Server hostname [" + hostname + "] > ");
//            input = reader.readLine();
//            if (!input.equals("")) hostname = input;

//            System.out.print("Server port [" + port + "] > ");
//            input = reader.readLine();
//            if (!input.equals("")) port = Integer.parseInt(input);

//            System.out.print("Prime search initial value [" + initialValue + "] > ");
//            input = reader.readLine();
//            if (!input.equals("")) initialValue = Integer.parseInt(input);

//            System.out.print("Prime search count [" + count + "] > ");
//            input = reader.readLine();
//            if (!input.equals("")) count = Integer.parseInt(input);

//            System.out.print("Number of Clients: [" + clients + "] > ");
//            input = reader.readLine();
//            if (!input.equals("")) clients = Integer.parseInt(input);

//            for(int i = 0; i < clients; i++) {
//                new PrimeClient(hostname, port, initialValue, count).start();
//            }
            new PrimeClient(hostname, port, initialValue, count).start();

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }

    }
}



