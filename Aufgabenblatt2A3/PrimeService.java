package Aufgabenblatt2A3;

import rm.requestResponse.*;

import java.io.IOException;

/**
 * Created by Micha on 29.05.2017.
 */
public class PrimeService extends Thread {
    private long number;
    private Component communication;
    private int sendPort;
    private int port = 1234;
    private Boolean isPrime;
    private ThreadCounter counter;

    public PrimeService(long number, int sendPort){
        this.number = number;
        this.sendPort = sendPort;
    }

    public void run() {
        isPrime = primeService();
        try {
            Component comm = new Component();
            comm.send(new Message("localhost", 4321, isPrime), sendPort, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (PrimeServer.obj) {
            PrimeServer.obj.notify();
            PrimeServer.counter.decrement();
        }
    }

    public boolean primeService() {
        for (long i = 2; i < Math.sqrt(number)+1; i++) {
//            System.out.println(Thread.currentThread().getName() + " : " + i);
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
