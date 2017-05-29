package Aufgabenblatt2A3;

/**
 * Created by Micha on 29.05.2017.
 */
public class PrimeService extends Thread {
    private long number;

    public PrimeService(long number){
        this.number = number;
    }

    public void run() {
        primeService();
    }
    public boolean primeService() {
        for (long i = 2; i < Math.sqrt(number)+1; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}
