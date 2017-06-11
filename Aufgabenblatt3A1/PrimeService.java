package Aufgabenblatt3A1;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

/**
 * Created by Micha on 29.05.2017.
 */
public class PrimeService extends Thread {
    private long number;
    private int sendPort;
    private Boolean isPrime;
    private ThreadCounter counter;
    long waitTimeStart, procTimeStart, procTimeEnd;

    public PrimeService(long number, int sendPort, long waitTimeStart){
        this.number = number;
        this.sendPort = sendPort;
        this.waitTimeStart = waitTimeStart;
    }

    public void run() {
        Long waitTimeEnd = System.currentTimeMillis();
        isPrime = primeService();
        try {
            Component comm = new Component();
            Object arr[] = new Object[3];
            arr[0] = isPrime;
            arr[1] = (procTimeEnd - procTimeStart);
            arr[2] = (waitTimeEnd - waitTimeStart);
            comm.send(new Message("localhost", 4321, arr), sendPort, true);
//            comm.send(new Message("localhost", 4321, isPrime), sendPort, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (PrimeServer.obj) {
            PrimeServer.obj.notify();
            PrimeServer.counter.decrement();
        }
    }

    public boolean primeService() {
        procTimeStart = System.currentTimeMillis();
        for (long i = 2; i < Math.sqrt(number)+1; i++) {
            if (number % i == 0) {
                procTimeEnd = System.currentTimeMillis();
                return false;
            }
        }
        procTimeEnd = System.currentTimeMillis();
        return true;
    }
}
