/**
 * This class consists of the fields of a Bank, which includes
 * the name of the bank, a list of accounts, the current number of accounts.
 *
 * This class has built in methods to retrieve and set the information as
 * noted above. In addition, it also contains getter methods for the bank's
 * name. If an attempt is made to set the bank name to null, it will default to
 * "Seneca@York". There are also two overloaded
 * methods implemented in this class: 1) equals(), which is capable of checking
 * if an object passed by the user as argument is the same as the account being
 * referenced. 2) toString(), which formats printing the bank's information
 * followed by its account's information. 3) Search(), allowing the user to
 * search for accounts that pertain to a specified balance. 4) Remove() which
 * will remove the account with the specified account number.
 *
 * @author Alex Wang
 * @version 2.1
 */
import java.util.ArrayList;

public class Bank {
    private String name;
    private ArrayList<Account> accounts;
    private static final String DEFAULT_BANK_NAME = "Seneca@York";

    /**
     * An empty constructor that sets a bank to safe empty state
     */
    public Bank() {
        this(DEFAULT_BANK_NAME);
    }

    /**
     * A constructor that takes a bank name and constructs a bank object.
     *
     * @param bankName          The name of the bank
     */
    public Bank(String bankName) {
      setBankName (bankName);
      accounts = new ArrayList<Account>();
    }

    /**
     * Retrieves the name of the bank
     *
     * @return                  Returns the name of the bank
     */
    public String getBankName() { return name; }

    /**
     * Sets the name of the bank
     * <p>
     * This method will check if the bank name provided is valid. If it is
     * valid, it will assign it to the object's bank name.  Otherwise,
     * it will set it to DEFAULT_BANK_NAME
     */
    private void setBankName (String bankName) {
        if (bankName != null) { name = bankName; }
        else { name = DEFAULT_BANK_NAME;}
     }

    /**
     * Returns an array of Accounts opened in Account[] format.
     * <p>
     * This method will return an array of accounts opened. If there are no
     * accounts opened, it will return an array of zero length.
     *
     * @return                  An array of accounts opened
     */
    public Account[] getAllAccounts() {
      Account[] result = new Account[accounts.size()];
      return accounts.toArray(result);
    }

    /**
     * Adds an account to the bank.
     * <p>
     * This method will return true if the account is added successfully.
     * Otherwise, it will return false. It will also check the memory to see
     * if the new account information already exist on the system.
     *
     * @param newAccount      The account object containing new information.
     */
    public boolean addAccount(Account newAccount) {
      boolean result = false;
      if (newAccount != null && !existAccount(newAccount)){
        result = accounts.add(newAccount); // Returns true if successful
      }
      return result;
    }


    /**
     * Checks to see if the account already exits in memory
     *
     * @param newAccount      The account object that is being added
     * @return                True if account already exists. Otherwise, false.
     */
    private boolean existAccount (Account newAccount) {
      for (Account account : accounts) {
        if (account!= null && account.equals(newAccount)) {
          return true;
        }
      }
      return false;
    }

    /**
     * Removes an account based on the specified accountNumber
     * <p>
     * This method will search for the account by the account number provided.
     * If the account is found, it will remove the account from accounts array
     * list, and return the deleted account to the user.
     *
     * @param accountNumber    The account number to be removed
     * @return                 The account to be removed or null if not found.
     */
    public Account removeAccount(String accountNumber) {

      Account deletedAccount = null;
      int targetAccountIndex = findIndexOfAccount (accountNumber);
      if (targetAccountIndex > -1) {
        deletedAccount = this.accounts.remove(targetAccountIndex);
      }

      return deletedAccount;
    }

    /**
     * Returns the index of the account desired for removal from the account
     * array in memory. If it is not found, return -1 to user.
     *
     * @param accountNumber   The account number being searched
     * @return                The index number of the account or -1 if not found
     */
    private int findIndexOfAccount (String accountNumber) {
      // Returns the index in the array where the specified account number
      // resides. If account not found, return -1
      int currentNumOfAccounts = this.accounts.size();
      for (int i = 0; i < currentNumOfAccounts; i++) {
        Account accountAtIndexI = accounts.get(i);
        if (accountAtIndexI.getAccountNumber().equals(accountNumber)){
          return i;
        }
      }
      return -1;
    }

    /**
     * Return all accounts with the balance specified by the user.
     * <p>
     * This method will return an account array containing accounts with the
     * user specified balance. If no account is found, return an empty account
     * array.
     *
     * @param balance           The balance to search for in accounts
     * @return                  An array of accounts that match the balance
     * @throws					NoAccountException if no accounts found
     */
    public ArrayList<Account> search(double balance) throws NoAccountException {
      ArrayList<Account> searchResults = new ArrayList<Account>();
      
      for (Account account : accounts) {
        if (account.getBalance() == balance) {
          searchResults.add(account);
        }
      }
      
      if (searchResults.isEmpty()){
    	  throw new NoAccountException();
      }
      
      return searchResults;
    }
    
    /**
     * Return all accounts containing the account name specified by the user.
     * <p>
     * This method will return an account array containing accounts with the user
     * specified account name. If no account is found, return an empty account
     * array.
     * 
     * @param accountName		The name of the account to search for
     * @return					An array of accounts that match accountName
     * @throws 					NoAccountException if no account is found
     */
    public Account[] searchByAccountName(String accountName) throws NoAccountException{
    	ArrayList<Account> searchResults = new ArrayList<Account>();
    	for(Account account : accounts){
    		if (account.getFullName().contains(accountName)){
    			searchResults.add(account);
    		}
    	}
    	if (searchResults.size() > 0){
    		return searchResults.toArray(new Account[searchResults.size()]);
    	}else{
    		throw new NoAccountException();
    	}
    }
    
    /**
     * Reports if the incoming foreign object is the same Bank object
     * as the current bank object.
     * <p>
     * This method will return true if the two bank objects are identical and
     * false otherwise.
     *
     * @param foreignObject     The incoming object to be checked
     * @return                  True if both objects match or false otherwise
     */
    public boolean equals (Object foreignObject) {
      // Returns true if the foreignObject and the instance object invoking
      // it are the same account objects
      boolean thisBankEqualForeignObject = false;

      if (foreignObject instanceof Bank) {
        Bank otherBank = (Bank) foreignObject;
        int numOfMismatch = 0;

        if (this.name.toLowerCase().equals(otherBank.name.toLowerCase()))
             {
              for (int i = 0; i < this.accounts.size(); i++) {
                Account thisBankAccountAtIndexI = this.accounts.get(i);
                Account otherBankAccountAtIndexI = otherBank.accounts.get(i);
                if (thisBankAccountAtIndexI.hashCode() !=
                    otherBankAccountAtIndexI.hashCode()) {
                      numOfMismatch++;
                }
              }

              if (numOfMismatch == 0) { thisBankEqualForeignObject = true; }
        }
      }
      return thisBankEqualForeignObject;
    }

    /**
     * The toString method to print a bank object.
     * <p>
     * This method will print the information regarding the bank and list
     * each account's information that it has.
     *
     * @return                 The bank's information
     */
    public String toString() {
      StringBuffer output = new StringBuffer("");
      output.append(writeToStringheader());
      output.append(writeAccountsDetails());
      return output.toString();
    }

    /**
     * Prints the header.append( containing the bank's information
     *
     * @return                 The header containing bank info
     */
    private String writeToStringheader() {
      StringBuffer header = new StringBuffer();

      header.append("*********************************\n");
      header.append("*         Bank Details          *\n");
      header.append("*********************************\n");
      header.append("Bank Name: " + name + '\n');
      header.append("*********************************\n");
      header.append("*        Account Details        *\n");
      header.append("*********************************\n");

      return header.toString();
    }

    /**
     * Sends account details of a bank to toString method
     *
     * @return              Each account's information in the bank object
     */
    private String writeAccountsDetails() {
      // Returns the account details to toString()

      StringBuffer accountDetails = new StringBuffer("");
      int currentNumberOfAccounts = this.accounts.size();

      for (int i = 0; i < currentNumberOfAccounts; i++) {
        Account accountAtIndexI = accounts.get(i);
        if (accountAtIndexI != null) {
          accountDetails.append("Listing Number: " + (i + 1) + '\n');
          accountDetails.append(accountAtIndexI.toString());
          accountDetails.append(addNewLineIfNotLastAccount(i));
        }
      }
      accountDetails.append("*********************************\n");
      return accountDetails.toString();
    }

    /**
     * Prints new line at the bottom of account details if the account printed
     * was not the last account in the list.
     * <p>
     * This method will check to see if the last account's information has been
     * printed. If true, it will add a new line to give space for the next
     * account details. Otherwise, it will not give space so a line filled with
     * asterisks (*) will follow right after to complete the look of the
     * interface.
     *
     * @return              A new line character if the account printed was
     *                      not the last account, otherwise an empty string
     */
    private String addNewLineIfNotLastAccount(int i) {
      int currentNumOfAccounts = this.accounts.size();
      return ((i + 1) < currentNumOfAccounts? "\n" : "");
    }
    
    /**
     * Returns the number of accounts in the bank.
     * @return	Number of accounts
     */
    public int getNumberOfAccounts(){
    	return accounts.size();
    }
}
