import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import rm.requestResponse.Component;

public class RmiPrimeServer implements RmiPrimeServerInterface {
	private static final int PORT=1234;
	
	private static RmiPrimeServer server;
	
//	private Component communication;
	protected static ThreadCounter counter;
	private ExecutorService exec;
	protected static Object obj;
	
	public RmiPrimeServer() {
	    	obj = new Object();
	        counter = new ThreadCounter(obj);
	        counter.start();
    }
	
	public static void main(String args[]) throws RemoteException, InterruptedException {
		(server=new RmiPrimeServer()).start(PORT);
		Thread.sleep(200);
	}
	
	public void start(int port) throws RemoteException {
		System.out.println("PrimServer started on port " + port);
		RmiPrimeServerInterface serverStub = (RmiPrimeServerInterface)java.rmi.server.UnicastRemoteObject.exportObject(server, 0);
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind("PrimeServer", serverStub);
	}
	
	@Override
	public boolean isPrim(long candidate) throws RemoteException {
		synchronized (obj) {
			obj.notify();
			counter.increment();
		}
		for (long i = 2; i < Math.sqrt(candidate)+1; i++) {
			if (candidate % i == 0 ){
				synchronized (obj) {
					obj.notify();
					counter.decrement();
				}
				return false;
			}
		}
		synchronized (obj) {
			obj.notify();
			counter.decrement();
		}
		return true;
		
	}
	
}
