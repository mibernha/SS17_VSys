public class MyThread extends Thread {
    private static final int threadMax = 10;
    private static int runCount = 0;
    private static Object blocker;

    public void run() {
        synchronized (blocker) {
            while (runCount++ < 10) {
                doSth();
            }
        }
    }

    public static synchronized void doSth() {
        System.out.println(runCount + ": " + Thread.currentThread().getName());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        for(int i = 0; i < threadMax; i++) {
            new MyThread().start();
        }
    }
}