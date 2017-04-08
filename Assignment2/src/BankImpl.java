/**
 * RemoteBank interface implementation
 * 
 * <p>
 * This class implements the methods addAccount(Account account), deleteAccount(string accountNumber), 
 * searchAccountByBalance(double balance), searchAccountByName(string accountName).
 * 
 * @author Alex Wang
 * @version 0.2
 */
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class BankImpl extends UnicastRemoteObject implements RemoteBank {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8536951011655483981L;
	
	private Bank bank;
	
	public BankImpl(Bank bank) throws RemoteException{
		if (bank != null){
			this.bank = bank;
		}
	}
	
	/**
	 * Invokes addAccount(Account) from the bank object.
	 * 
	 * <p>
	 * Takes account object provided by the user and adds it to the 
	 * current bank object.
	 * 
	 * @param	account			The account to be added
	 * @return					True if account successfully added
	 * @throws					RemoteException
	 */
	@Override
	public boolean addAccount(Account account) throws RemoteException
	{
			return bank.addAccount(account);
	}
	
	/**
	 * Invokes removeAccount(String) from the bank object
	 * 
	 * <p>
	 * Takes the account number from the user as a string, deletes
	 * the account if found and returns the account that was deleted.
	 * 
	 * @param	accountNumber		The account number in string format
	 * @return						The deleted account 
	 * @throws						Remote Exception
	 */
	@Override
	public Account deleteAccount(String accountNumber) throws NoAccountException, RemoteException
	{
		return bank.removeAccount(accountNumber);
	}
	
	/**
	 * Invokes search(double) from the bank object.
	 * 
	 * <p>
	 * Takes a balance of type double and searches for all accounts in the bank.
	 * It returns the accounts with the specified balance in an ArrayList<Account>
	 * 
	 * @param	balance				The balance to search for as a double
	 * @return						An ArrayList of Accounts that match the balance
	 * @throws						Remote Exception
	 */
	@Override
	public ArrayList<Account> searchAccountByBalance(double balance) throws RemoteException, NoAccountException
	{
		return bank.search(balance);
	}
	
	/**
	 * Invokes searchByAccountName(String) from the bank object.
	 * 
	 * <p>
	 * Takes the account name from the user as a string and searches the bank
	 * for accounts matching the account name. Results found are returned as Account[].
	 * 
	 * @param	accountName			The name of the account
	 * @return						Account[] with accounts found with specified name
	 * @throws						Remote Exception
	 */
	@Override
	public Account[] searchByAccountName (String accountName) throws RemoteException, NoAccountException
	{
		return bank.searchByAccountName(accountName);
	}
}
