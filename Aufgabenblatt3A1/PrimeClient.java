package Aufgabenblatt3A1;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
@SuppressWarnings("Duplicates")
public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17; //1e17
    private static final long COUNT = 50;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static int sendPort = (int) (Math.random()*1000+1);

    private Component communication;
    String hostname;
    int port;
    int counter = 1;
    long initialValue, count;
    long procTimeMed = 0;
    long waitTimeMed = 0;
    long commTimeMed = 0;

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
                counter++;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {

        Boolean blocking = false;
        Boolean isNull = false;

        long commTimeStart = System.currentTimeMillis();
        communication.send(new Message(hostname, sendPort, new Long(value)), port, true);

        System.out.print(CLIENT_NAME + " " + value + " ");

        do {
            try {
                Object obj[] = (Object[]) communication.receive(sendPort, blocking, true).getContent();
//                Boolean isPrime = (Boolean) communication.receive(sendPort, blocking, true).getContent();
//                long procTime = (Long) communication.receive(sendPort, blocking, true).getContent();
//                long waitTime = (Long) communication.receive(sendPort, blocking, true).getContent();
                Boolean isPrime = (Boolean) obj[0];
                long procTime = (Long) obj[1];
                long waitTime = (Long) obj[2];
                long commTimeEnd = System.currentTimeMillis();
                long commTime = (commTimeEnd - commTimeStart) - waitTime - procTime;
                procTimeMed += procTime;
                waitTimeMed += waitTime;
                commTimeMed += commTime;
                System.out.print((isPrime.booleanValue() ? " prime    " : " not prime"));
                System.out.print(" | p: " + procTime + " (" + procTimeMed/counter + ") ms");
                System.out.print(" | w: " + waitTime + " (" + waitTimeMed/counter + ") ms");
                System.out.print(" | c: " + commTime + " (" + commTimeMed/counter + ") ms");
                isNull = false;
            } catch (NullPointerException e) {
//                try {
//                    Thread.sleep(1000);
                    isNull = true;
//                    System.out.print(".");
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
            }
        } while (isNull);
        System.out.println();
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;

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

            new PrimeClient(hostname, port, initialValue, count).start();

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }

    }
}



