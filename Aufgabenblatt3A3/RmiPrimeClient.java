import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;

public class RmiPrimeClient {
	private static final int PORT=1234;
	private static final String HOSTNAME="localhost";
    private static final long INITIAL_VALUE = (long) 1e17; //1e17
    private static final long COUNT = 100;
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(HOSTNAME, PORT);
		RmiPrimeServerInterface server = (RmiPrimeServerInterface)registry.lookup("PrimeServer");
		
		for (long i = INITIAL_VALUE; i < INITIAL_VALUE + COUNT; i++) {
            try {
    			System.out.println(i + ": " + (server.isPrim(i)?"prim":"not prim"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		System.out.println("--DONE--");
		
	}

}
