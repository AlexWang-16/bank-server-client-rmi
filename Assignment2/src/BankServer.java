/**
 * BankServer is an RMI server application which allows multiple clients
 * to connect and obtain data in a secure manner. It uses Remote Method Invocation
 * technology so programming is at an object level instead of the lower level sockets.
 * 
 * @author Alex Wang
 * @version 0.1
 */


public class BankServer {

	public static void main(String[] args) {
		try{
			System.out.println("starting server..");
			
			java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.createRegistry(5678);
			
			Bank serverBank = new Bank();
			loadBank(serverBank);
			BankImpl bankServer = new BankImpl(serverBank);
			
			registry.rebind("bank", bankServer);
			
		}
		catch(Exception e){
			System.out.println("Error: " + e);
		}
		
		System.out.println("Main thread is put into wait state");

	}

	/**
	 * Preloads the bank with 6 accounts. 2 Savings account, 2 Chequing, and 2 GIC accounts.
	 * The accounts are divided between two owners "Doe, Joe" and "Ryan, Mary". The GIC 
	 * accounts have a 2 and 4 year maturity and 1.5 and 2.5 percent annual interest rate.
	 * Doe, John has $6000 starting account balance while Ryan, Mary has a starting balance 
	 * of $15,000.
	 * 
	 * @param bank		The bank to load the accounts to
	 */
	private static void loadBank(Bank bank){
		GIC gic = new GIC("Doe, John","D1234", 6000, 2, 1.5);
		Chequing chq = new Chequing("Doe, John", "D2345", 6000, 0.15, 5);
		Savings sav = new Savings("Doe, John","D5678", 6000, 0.15);
		
		
		GIC gic2 = new GIC("Ryan, Mary","A1234", 15000, 4, 2.5);
		Chequing chq2 = new Chequing("Ryan, Mary", "A2345", 15000, 0.5, 5);
		Savings sav2 = new Savings("Ryan, Mary","A5678", 15000, 0.25);
		
		bank.addAccount(gic);
		bank.addAccount(gic2);
		bank.addAccount(chq);
		bank.addAccount(chq2);
		bank.addAccount(sav);
		bank.addAccount(sav2);
		
	}
}
