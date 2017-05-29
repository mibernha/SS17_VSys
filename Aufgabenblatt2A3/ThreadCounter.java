package Aufgabenblatt2A3;

/**
 * Created by MB_SP on 29.05.2017.
 */
public class ThreadCounter extends Thread {
    private int counter = 1;

    public void run() {

    }

    public void increment() {
        counter++;
//        System.out.println("ACTIVE THREADS: " + counter);
    }

    public void decrement() {
        counter--;
//        System.out.println("ACTIVE THREADS: " + counter);
    }

    public int getCounter() {
        return counter;
    }

    public int cheatCounter() {
        return Thread.activeCount();
    }
}
