/**
 * Remote Method Invocation interface for Bank application
 *
 * <p>
 * The methods declared by this interface are: addAccount(Account account), deleteAccount(string accountNumber),
 * searchAccountByBalance(double balance), searchAccountByName(string accountName).
 *
 * @author Alex Wang
 */
 
package edu.btp400.w2017.common;
import java.rmi.*;
import java.util.ArrayList;

public interface RemoteBank extends Remote {

	public boolean addAccount(Account account) throws RemoteException;

	public Account deleteAccount(String AccountNumber) throws NoAccountException, RemoteException;

	public ArrayList<Account> searchAccountByBalance(double balance) throws RemoteException, NoAccountException;

	public Account[] searchByAccountName (String accountName) throws RemoteException, NoAccountException;
}
