package Aufgabenblatt2A3;

/**
 * Created by MB_SP on 24.05.2017.
 */
public class LoadProgress extends Thread {
    private static Boolean running = true;

    public LoadProgress() {};

    public void showProgress(Boolean running) {
        while(running) {
            try {
                Thread.sleep(500);
                System.out.print(".");
            } catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setRunning(Boolean running) {
        LoadProgress.running = running;
    }
}
