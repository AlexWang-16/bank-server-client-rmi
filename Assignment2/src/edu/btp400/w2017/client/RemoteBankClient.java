/**
 * RemoteBankClient application that acts as as a user interface and uses RMI.
 *
 * @author awang05
 * @version 0.4
 */
package edu.btp400.w2017.client;
import java.util.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import edu.btp400.w2017.common.*;

public class RemoteBankClient {

	public enum TransactionType{
		WITHDRAW, DEPOSIT
	}

	private static final double SMALLEST_DOUBLE_VALUE = -1 * Double.MAX_VALUE;
	private static final double LARGEST_DOUBLE_VALUE = Double.MAX_VALUE;
	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);
	public static Scanner input = new Scanner(System.in);

	/**
	 * Displays the menu to the user.
	 * @param bankName		The name of the bank to construct welcome message
	 */
	public static void displayMenu(String bankName){
		StringBuffer output = new StringBuffer("Welcome to " + bankName + " Bank!\n");
		output.append("1. Open an account.\n");
		output.append("2. Close an account.\n");
		output.append("3. Deposit money.\n");
		output.append("4. Withdraw money.\n");
		output.append("5. Display accounts.\n");
		output.append("6. Display a tax statement.\n");
		output.append("7. Exit\n");

		System.out.println(output.toString());
	}

	/**
	 * Prompts the user to select a menu choice
	 * @return		The choice as an integer
	 */
	public static int menuChoice(){
		int choice = 0;

		do{
			System.out.println("Please enter your choice: ");

			while (!input.hasNextInt()) {
			        System.out.println("Error: Inputs can only be integers!\n");
			        System.out.println("Please enter your choice: ");
			        input.nextLine();
			 }

			choice = input.nextInt();

			if (choice < 1 || choice > 7){
				System.out.println("Error: Input is out of range. "
									+ "Please try again.\n");
			}

		}while(choice < 1 || choice > 7);

		return choice;
	}

	/**
	 * Prompts the user to enter a type of account to create and takes the parameters
	 * necessary to open an account.
	 *
	 * @param bank			The bank to add accounts to
	 * @throws RemoteException
	 */
	public static void openAccount(RemoteBank bank) throws RemoteException{
		input.nextLine();
		String accountType = "";
		String[] accountData;

		while (!accountType.equals("SAV")
				&& !accountType.equals("CHQ")
				&& !accountType.equals("GIC")){
			System.out.println("Please enter the account type (SAV/CHQ/GIC): ");
			accountType = input.nextLine().trim().toUpperCase();
		}

		accountData = getUserAccountInformation(accountType);

		if (accountType.equals("SAV")){
			String fullName = accountData[0].trim();
			String accountNumber = accountData[1].trim();
			double startingBalance = Double
									.parseDouble(accountData[2].trim());
			double interestRate = Double
									.parseDouble(accountData[3].trim());

			Savings savingsAccount = new Savings(fullName, accountNumber,
					startingBalance, interestRate);
			try{
				bank.addAccount(savingsAccount);
			}catch (RemoteException e){
				throw e;
			}

			displayAccount(savingsAccount);
		}
		else if (accountType.equals("CHQ")){
			String fullName = accountData[0].trim();
			String accountNumber = accountData[1].trim();
			double startingBalance = Double
									.parseDouble(accountData[2].trim());
			double serviceChargePerTransaction = Double
												.parseDouble(accountData[3].trim());
			int numberOfTransactions = Integer
										.parseInt(accountData[4].trim());

			Chequing chequingAccount = new Chequing(fullName, accountNumber,
					startingBalance, serviceChargePerTransaction, numberOfTransactions);

			try {
				bank.addAccount(chequingAccount);
			}catch(RemoteException e){
				throw e;
			}
			System.out.println("\n");
			displayAccount(chequingAccount);
		}
		else if (accountType.equals("GIC")){
			String fullName = accountData[0].trim();
			String accountNumber = accountData[1].trim();
			double startingBalance = Double.parseDouble(accountData[2].trim());
			int periodOfInvestment = Integer.parseInt(accountData[3].trim());
			double annualInterestRate = Double.parseDouble(accountData[4].trim());


			GIC GICAccount = new GIC(fullName, accountNumber,
					startingBalance, periodOfInvestment, annualInterestRate);
			try{
				bank.addAccount(GICAccount);
			}catch (RemoteException e){
				throw e;
			}

			System.out.println("\n");
			displayAccount(GICAccount);
		}
		System.out.println("Account successfully added!\n");

	}

	/**
	 * Validates and ensures that the proper number of arguments are provided
	 * for each account type
	 *
	 * @param accountType	The account type the user has requested to open
	 * @return				The user's data with the proper amount of arguments
	 */
	public static String[] getUserAccountInformation(String accountType){
		String[] accountData;
		String rawData = new String("");

		if (accountType.equals("SAV")){
			do{
				askForAccountInfo("SAV");
				rawData = input.nextLine();
				accountData = rawData.split(";");

				if (accountData.length != 4){
					System.out.println("Error: Please provide 4 arguments.\n");
				}
			}while (accountData.length != 4);

		}else if (accountType.equals("CHQ")){
			do{
				askForAccountInfo("CHQ");
				rawData = input.nextLine();
				accountData = rawData.split(";");

				if (accountData.length != 5){
					System.out.println("Error: Please provide 5 arguments.\n");
				}
			}while (accountData.length != 5);
		}
		else if (accountType.equals("GIC")){
			do{
				askForAccountInfo("GIC");
				rawData = input.nextLine();
				accountData = rawData.split(";");

				if (accountData.length != 5){
					System.out.println("Error: Please provide 5 arguments.\n");
				}
			}while (accountData.length != 5);
		}
		else{
			accountData = new String[1];
		}
		return accountData;
	}

	/**
	 * Prompts the user to enter account infomration used when opening accounts.
	 * It also provides an example of paramters for the user to enter for each
	 * type of account.
	 *
	 * @param accountType		The type of account the user is trying to open
	 */
	private static void askForAccountInfo(String accountType){
		System.out.println("Please enter account information at one line");
		if (accountType.equals("SAV")){
			System.out.println("(e.g. Doe, John;A1234;1000.00;3.65):");
		}else if (accountType.equals("CHQ")){
			System.out.println("(e.g. Doe, John;A1234;1000.00;0.25;3):");
		}else if (accountType.equals("GIC")){
			System.out.println("(e.g. Doe, John;A1234;1000.00;3;1.5):");
		}
	}

	/**
	 * Prompts the user to enter the account number of the account to
	 * close.
	 * @return		A string containing the account number
	 */
	private static String getRemovalAccount(){
		String account = "";
		input.nextLine();
		do{
			System.out.println("Please enter the account number to close: ");
			account = input.nextLine().trim();
		}while(account.isEmpty());

		return account;
	}

	/**
	 * Provides the user with feedback on the account removal.
	 * @param account		The account that was removed
	 */
	private static void displayRemovalFeedback(Account account){
		if (account == null){
			System.out.print("Error: delete was not successful. ");
			System.out.println("Please ensure account number is correct.\n");
		}
		else{
			displayAccount(account);
			System.out.println("Account deleted successfully.\n");
		}
	}

	/**
	 * Displays the details of the account.
	 * @param account		The account to display
	 */
	public static void displayAccount(Account account){
		System.out.println("\nAccount Details");
		System.out.println("====================");
		System.out.println(account);
	}

	/**
	 * Requests the user for the amount to withdraw/deposit.
	 *
	 * @param style				The transaction type
	 * @return					The amount for the transaction
	 */
	private static double requestForAmount(TransactionType style){
		input.nextLine();
		double amount = 0;
		String transactionType = "";

		if (style == TransactionType.DEPOSIT){
			transactionType = "deposit";
		}
		else if (style == TransactionType.WITHDRAW){
			transactionType = "withdraw";
		}

			do{
				 System.out.println("Please enter " + transactionType + " amount: ");

				while (!input.hasNextDouble()){
					System.out.println("\nError: " + transactionType + " amount can only be in doubles!\n");
			        System.out.println("Please enter deposit amount: ");
			        input.nextLine();
				}
				amount = input.nextDouble();

				if (amount < 1){
					System.out.println("\nError: amount must be greater than 0.\n");
				}

			 }while (amount < 1 || amount < SMALLEST_DOUBLE_VALUE || amount > LARGEST_DOUBLE_VALUE);


		return amount;
	}

	/**
	 * Returns the account selected for withdraw or deposit.
	 *
	 * @param bank			The bank which the account belongs to
	 * @return				The account selected
	 */
	private static Account selectAccount(Bank bank){
		Account[] accountList = bank.getAllAccounts();
		int selectedAccount = 1;
		if (accountList.length > 1){
			listAccountsWithOptions (accountList);
			selectedAccount = selectedAccountOption(accountList);
		}
		else{
			displayAccount(accountList[0]);
		}
		return accountList[selectedAccount - 1];
	}

	/**
	 * Lists all the account options and each account's details
	 * for selection during a deposit/withdraw transaction.
	 *
	 * @param accounts			An array of all accounts held
	 */
	private static void listAccountsWithOptions(Account[] accounts){
		for(int i = 0; i < accounts.length; i++){
			System.out.println("Option " + (i + 1) + ":");
			displayAccount(accounts[i]);
		}

	}

	/**
	 * Prompts user to select an account option during a deposit/withdraw
	 * transaction.
	 *
	 * @param accounts			The array of all accounts held
	 * @return					The option number associated with the account
	 */
	private static int selectedAccountOption(Account[] accounts){
		input.nextLine();
		int option = 0;

		 do{
			 System.out.println("Please enter option number to choose account: ");

			while (!input.hasNextInt()){
				System.out.println("Error: Inputs can only be integers!\n");
		        System.out.println("Please enter option number to choose account: ");
		        input.nextLine();
			}
			option = input.nextInt();

		 }while(option < 1 || option > accounts.length);
		 return option;
	}

	/**
	 * Prompts the user to select a style to display accounts
	 * and returns the option selected.
	 *
	 * @return		The display option the user has selected
	 */
	private static int getDisplaySelection(){
		input.nextLine();
		int option = 0;

		do{
			System.out.println("Display options");
			System.out.println("===============");
			System.out.println("Option 1: Display all accounts by name");
			System.out.println("Option 2: Display all accounts by balance");
			System.out.println("Option 3: Display all accounts\n");
			System.out.println("Please enter option number to choose account: ");

			while (!input.hasNextInt()){
				System.out.println("Error: Inputs can only be integers!\n");
		        System.out.println("Please enter option number: ");
		        input.nextLine();
			}
			option = input.nextInt();

		 }while(option < 1 || option > 3);
		 return option;
	}
	/**
	 * Prompts the user for the name of the account holder and displays
	 * accounts belonging to the account holder.
	 *
	 * @param bank			The bank which the account belongs to
	 */
	private static void displayAccountsByName(RemoteBank bank) throws RemoteException{
		input.nextLine();
		String accountName = "";
		Account[] searchResults = null;
		do{
			System.out.println("Please enter the name of the account holder: ");
			accountName = input.nextLine().trim();
		}while(accountName.isEmpty());

		//Search
		try{
			searchResults = bank.searchByAccountName(accountName);

			//Display the entries
			for (Account account: searchResults){	// ATTN - Potential nullptr exception
				displayAccount(account);
			}
		}catch (NoAccountException e){
			System.out.println(e);
		}catch (RemoteException e){
			throw e;
		}
	}

	/**
	 * Prompts the user to enter a balance to search accounts by. Then
	 * list the accounts with matching balance.
	 *
	 * @param bank		The bank to load the accounts from
	 */
	private static void displayAccountsByBalance(RemoteBank bank) throws RemoteException{
		input.nextLine();
		ArrayList<Account> searchResults = new ArrayList<Account>();
		double balance = 0;

		//Validate balance
		 do{
			 System.out.println("Please enter the balance for search: ");

			while (!input.hasNextDouble()){
				System.out.println("Error: balance can only be double!\n");
		        System.out.println("Please enter the balance for search: ");
		        input.nextLine();
			}
			balance = input.nextDouble();
		 }while(balance < 1);

		 //Search
		 try{
			 searchResults = bank.searchAccountByBalance(balance);

			 //Display
			 for (int i = 0; i < searchResults.size(); i++){
				 displayAccount(searchResults.get(i));
			 }
		 }catch (NoAccountException e){
			 System.out.println(e);
		 }catch (RemoteException e){
			 throw e;
		 }
	}

	/**
	 * Displays all accounts in the bank.
	 * @param bank				The bank which accounts are found
	 */
	private static void displayAllAccounts(Bank bank){
		Account[] accounts = bank.getAllAccounts();
		if (accounts.length < 1){
			System.out.println("\nThere are currently no accounts in this bank\n");
		}
		for(Account account : accounts){
			displayAccount(account);
		}
	}

	/**
	 * Prompts the user for the name of the account holder.  Then, search
	 * the bank for the accounts with the matching account holder's name
	 * and print the accounts.
	 *
	 * @param bank			The bank which holds the accounts
	 */
	private static void displayTaxStatement(RemoteBank bank){
		input.nextLine();
		String accountName = "";
		Account[] searchResults = new Account[0];
		int resultCounter = 1;
		//Ask for user name
		do{
			System.out.println("Please provide account holder name: ");
			accountName = input.nextLine().trim();
		}while(accountName.isEmpty());



		//Get all accounts by name
		try{
			searchResults = bank.searchByAccountName(accountName);
			int numOfStatements = 0;

			for (int i = 0; i < searchResults.length; i++){
				if (searchResults[i] instanceof GIC || searchResults[i] instanceof Savings){
					numOfStatements++;
				}
			}

			if (numOfStatements > 0){
				System.out.println("\nTax Statement(s) of " + searchResults[0].getFullName() + '\n');
			}else{
				System.out.printf("\n%s does not have any taxable accounts.\n\n", searchResults[0].getFullName());
			}

				//Check if account instaneof GIC/Savings
				for (int i = 0; i < searchResults.length; i++){
					if (searchResults[i] instanceof GIC){
						System.out.println(resultCounter + ".");
						GIC gic = (GIC) searchResults[i];
						System.out.println(gic.createTaxStatement());
						resultCounter++;
					}
					else if(searchResults[i] instanceof Savings){
						System.out.println(resultCounter + ".");
						Savings saving = (Savings) searchResults[i];
						System.out.println(saving.createTaxStatement());
						resultCounter++;
					}
				}
		}catch (NoAccountException e ){
			System.out.println(e);
		}catch (RemoteException e){
			System.out.println("Error: Remote exception occured while searching accounts with name " + accountName);
			System.out.println("Standby, printing stack trace...");
			e.printStackTrace();
		}

	}

	public static void main(String[] args){
		int choice = 0;
		Account account = null;
		double amount;
		RemoteBank serverBank = null;
		try{
			serverBank = (RemoteBank) Naming.lookup("rmi://localhost:5678/bank");

			do{
				displayMenu("Seneca@York");
				choice = menuChoice();
				switch (choice){
				case 1:					//Open an account
					try{
						openAccount(serverBank);
					}catch (RemoteException e){
						throw e;
					}

					break;
				case 2:					//Close an account
					String accountNumber = getRemovalAccount().trim();
					try{
						account = serverBank.deleteAccount(accountNumber);
					}catch (RemoteException e){
						throw e;
					}
					displayRemovalFeedback(account);
					account = null;
					break;
				case 3:					//Deposit money
					System.out.println("Deposit money function is currently disabled.\n");
					break;
				case 4:					//Withdraw money
					System.out.println("Withdraw function is currently disabled.\n");
					break;
				case 5:					// Display Accounts
					int selection = getDisplaySelection();
					if (selection == 1){
						displayAccountsByName(serverBank);
					}else if (selection == 2){
						displayAccountsByBalance(serverBank);
					}else if (selection == 3){
						//displayAllAccounts(seneca);
						System.out.println("Display all accounts current disabled.\n");
					}
					break;
				case 6:					//Display a tax statement
					displayTaxStatement(serverBank);
					break;
				case 7:
					System.out.println("Goodbye!\n");
					input.close();
					break;
				}
			} while (choice != 7);
		}catch (RemoteException e){
			System.out.println("\nError: No connection to server. Please ensure server is still active.\n");
		}catch (MalformedURLException e){
			System.out.println("\nError: Malformed URL exception occured while connecting to bank server."
					+ " Please check server URL and try again.\n");
		}catch (Exception e){
			System.out.println("An exception has occured while communicating with bank.");
			System.out.println("Standby, printing stack trace...");
			e.printStackTrace();
		}
	}
}
