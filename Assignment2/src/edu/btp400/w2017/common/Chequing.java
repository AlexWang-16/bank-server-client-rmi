/**
/ * This class emulates a chequing bank account by extending the Account base class.
 * The chequing account type is not taxable.
 *
 * @author awang05
 * @version 0.7
 */

package edu.btp400.w2017.common;
import java.io.Serializable;
import java.math.BigDecimal;

public class Chequing extends Account implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8056657011706478157L;

	public static final String ACCOUNT_TYPE = "CHQ";
	private BigDecimal serviceChargePerTransaction;
	private BigDecimal totalServiceCharges;
	private int numberOfTransactionsAllowed;
	private double[] transactions;
	private int numberOfTransactionsUsed;

	// For internal hashCode() generation only
	private int bogusNumberOfTransactionsAllowed;
	private double bogusServiceChargePerTransaction;

	public enum Transaction {
		DEPOSIT, WITHDRAW
	}


	/**
	 * Default constructor
	 */
	public Chequing() {
		this("", "", 0, 0.25, 3);
	}

	/**
	 * 5 Argument constructor. It takes user defined parameters to setup
	 * the chequing account.

	 *
	 * @param fullName
	 * @param accountNumber
	 * @param startingBalance
	 * @param ServiceChargePerTransaction
	 * @param numberOfTransactionsAllowed
	 */
	public Chequing(String fullName, String accountNumber, double startingBalance,
			double serviceChargePerTransaction, int numberOfTransactionsAllowed) {
		super(fullName, accountNumber, startingBalance);

		setServiceChargePerTransaction(serviceChargePerTransaction);
		setTotalServiceCharge(0);
		setNumberOfTransactionsAllowed(numberOfTransactionsAllowed);

		transactions = new double[this.numberOfTransactionsAllowed];
	}

	/**
	 * Sets the per transaction service charge.
	 *
	 * @param serviceCharge
	 */
	private void setServiceChargePerTransaction(double serviceCharge){
		if (serviceChargeIsValid(serviceCharge)){
			this.serviceChargePerTransaction = BigDecimal.valueOf(serviceCharge);
		}else{
			this.serviceChargePerTransaction = BigDecimal.valueOf(0.25);
			this.setBogusServiceChargePerTransaction(serviceCharge);
		}
	}

	/**
	 * Returns true if the service charge provided is within valid range.
	 * Otherwise, return false.
	 *
	 * @param serviceCharge
	 * @return	True, if service charge is valid
	 */
	private boolean serviceChargeIsValid(double serviceCharge){
		if (serviceCharge > 0 && serviceCharge < LARGEST_DOUBLE_VALUE){
			return true;
		}
		return false;
	}

	/**
	 * Sets the bogus service charge per transaction based on the provided
	 * value.If the value is out of range, set the bogus value to -1. If the
	 * value is within range, but is 0 or less, set value to (serviceCharge +2).
	 * If the value is not bogus, set bogus service charge per transaction to 0.
	 *
	 * @param serviceCharge
	 */
	private void setBogusServiceChargePerTransaction(double serviceCharge){
		if (serviceCharge < SMALLEST_DOUBLE_VALUE || serviceCharge > LARGEST_DOUBLE_VALUE){
			this.bogusServiceChargePerTransaction = -2;
		}else if (serviceCharge > SMALLEST_DOUBLE_VALUE && serviceCharge <= 0){
			this.bogusServiceChargePerTransaction = serviceCharge + 2.5;
		}else{
			this.bogusServiceChargePerTransaction = 0;
		}
	}

	/**
	 * Returns the service charge per transaction amount.
	 * @return	Service charge per transaction.
	 */
	public double getServiceChargePerTransaction(){
		return this.serviceChargePerTransaction.doubleValue();
	}

	/**
	 * Sets the number of transactions allowed in the checking account.
	 *
	 * @param numberOfTransactions
	 */
	private void setNumberOfTransactionsAllowed(int numberOfTransactions){
		if (numberOfTransactionsIsValid(numberOfTransactions)){
			this.numberOfTransactionsAllowed = numberOfTransactions;
		}
		else{
			this.numberOfTransactionsAllowed = 3;
			this.setBogusNumberOfTransactionsAllowed(numberOfTransactions);
		}
	}

	/**
	 * Return true if number of transactions is valid. False, otherwise.
	 *
	 * @param numberOfTransactions
	 * @return True, if number of transactions is valid. Otherwise, false.
	 */
	private boolean numberOfTransactionsIsValid(int numberOfTransactions){
		return (numberOfTransactions > 0 && numberOfTransactions < Integer.MAX_VALUE);
	}

	/**
	 * Sets bogus number of transactions allowed based on the value provided.
	 * If the value is out of Integer range, set it to the value of -1.
	 * If the value is within Integer range, set value to (bogus value + 2).
	 * If the value is a legitimate value, set the bogus value to a default of 0.
	 *
	 * @param numberOfTransactions
	 */
	private void setBogusNumberOfTransactionsAllowed(int numberOfTransactions){
		if (numberOfTransactions < Integer.MIN_VALUE || numberOfTransactions > Integer.MAX_VALUE){
			this.bogusNumberOfTransactionsAllowed = -1;
		}else if (numberOfTransactions > Integer.MIN_VALUE && numberOfTransactions < 1){
			this.bogusNumberOfTransactionsAllowed = numberOfTransactions + 2;
		}else{
			this.bogusNumberOfTransactionsAllowed = 0;
		}
	}

	/**
	 * Validates if the transaction can be made successfully.
	 * If transaction is successful, store the amount to the
	 * array and update the balance. If transaction is not
	 * successful
	 *
	 * @param	amount		The deposit amount
	 * @override
	 */
	@Override
	public void deposit (double amount){
		if (transactionIsValid(amount, Transaction.DEPOSIT)){
			storeAmountToArray(amount);
			updateBalance();
		}

	}

	/**
	 * Validates if the the withdraw transaction can be made
	 * successfully with the the specified amount. If successful,
	 * store the amount to transaction[] and update the balance.
	 * If transaction is not successful, do nothing.
	 *
	 * @param	amount	The deposited amount
	 * @return	True if withdraw is successful. False otherwise.
	 * @override
	 */
	@Override
	public boolean withdraw(double amount){
		if (transactionIsValid(amount, Transaction.WITHDRAW)){
			storeAmountToArray(-amount);
			updateBalance();
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if an transaction is possible. If possible, store
	 * the amount to transaction[]. Otherwise do nothing.
	 * @param amount
	 */
	private void storeAmountToArray(double amount){
		if (transactionsNotMaxed()){
			transactions[this.numberOfTransactionsUsed++] = amount;
		}
	}


	/**
	 * Return true if the number of transactions
	 * available is not yet maxed out.
	 *
	 * @return True, if transaction is available
	 */
	private boolean transactionsNotMaxed(){
		return (this.numberOfTransactionsUsed < this.numberOfTransactionsAllowed);
	}

	/**
	 * Updates total service charge
	 *
	 */
	private void updateBalance(){
		double amount = transactions[this.numberOfTransactionsUsed - 1];
		this.setCurrentBalance(super.getBalance() + amount);
		setTotalServiceCharge(this.totalServiceCharges.doubleValue() +
								  this.serviceChargePerTransaction.doubleValue());
	}

	/**
	 * Return true if transactions in transaction[] is
	 * made successfully.
	 *
	 * @return True, if transactions were successful.
	 */
	private boolean transactionIsValid(double amount, Transaction type ){
		double finalAmount = (type == Transaction.WITHDRAW)?
				preprocessAmount(-amount):preprocessAmount(amount);

		return (amount >= 0 && transactionsNotMaxed() && finalAmount >= 0);
	}

	/**
	 * Simulates processing the transaction on the current balance.
	 * Returns the simulated amount.
	 *
	 * @param amount
	 * @return	The resulting balance after applying a transaction.
	 */
	private double preprocessAmount(double amount){
		if (amount < SMALLEST_DOUBLE_VALUE || amount > LARGEST_DOUBLE_VALUE){
			return -1;
		}else {
			return (super.getBalance() + amount);
		}
	}

	/**
	 * Sets the total service charge. If an out of range service charge
	 * is provided, set the total service charge to 0 as default.
	 *
	 * @param serviceCharge
	 */
	private void setTotalServiceCharge(double serviceCharge){
		if (serviceCharge >= 0 && serviceCharge < LARGEST_DOUBLE_VALUE){
			this.totalServiceCharges = BigDecimal.valueOf(serviceCharge);
		}else{
			this.totalServiceCharges = BigDecimal.valueOf(0);
		}

		//Setup decimal formatting
		this.totalServiceCharges.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Returns the final balance after deducting total service charges.
	 *
	 * @return	Final balance
	 * @Override
	 *
	 */
	@Override
	public double getBalance(){
		BigDecimal currentBalance = new BigDecimal(super.getBalance());
		currentBalance = currentBalance.subtract(this.totalServiceCharges);
		return (currentBalance.doubleValue());
	}

	/**
	 * Validates if the incoming object is the same Chequing
	 * account as the current account. Return true, if it is
	 * the same. Otherwise, return false.
	 *
	 * @return	True, if both accounts are identical
	 */
	@Override
	public boolean equals(Object foreignObject){
		if (foreignObject instanceof Chequing){
			Chequing chequingAccount = (Chequing) foreignObject;

			return (this.hashCode() == chequingAccount.hashCode());
		}

		return false;
	}

	/**
	 * Returns a unique generated hash code for each
	 * Chequing account.
	 *
	 * @return	Chequing account hash code
	 */
	@Override
	public int hashCode(){
		int hash = 63;
		hash += super.hashCode();
		hash *= this.getServiceChargePerTransactionHashCode();
		hash += this.getNumberOfTransactionsAllowedHashCode();
		return hash;
	}

	/**
	 * Detects if there a bogus service charge was provided. If true,
	 * return a special generated value. If false, return the valid
	 * service charge per transaction as hashCode.
	 *
	 * @return	Number of transaction hash code
	 */
	private double getServiceChargePerTransactionHashCode(){
		// Bogus service charge is present
		if (this.bogusServiceChargePerTransaction != 0){
			return (this.bogusServiceChargePerTransaction + 2.25) * -0.6;
		}else{
			return this.serviceChargePerTransaction.doubleValue();
		}
	}

	/**
	 * Detects if there a bogus number of transactions were provided. If true,
	 * return a special generated value. If false, return the valid
	 * number allowed transactions as hashCode.
	 *
	 * @return	Number of transaction hash code
	 */
	private double getNumberOfTransactionsAllowedHashCode(){
		// Bogus number of transactions are present
		if (this.bogusNumberOfTransactionsAllowed != 0){
			return (this.bogusNumberOfTransactionsAllowed - 1) * -0.85;
		}else{
			return this.numberOfTransactionsAllowed;
		}
	}

	/**
	 * Returns the number of transactions used at the moment.
	 * @return	Number of transactions used
	 */
	public int getNumberOfTransactionUsed(){
		return this.numberOfTransactionsUsed;
	}

	/**
	 * Returns the max number of transactions allowed.
	 * @return	Max number of transactions
	 */
	public int getNumberOfTransactionsAllowed(){
		return this.numberOfTransactionsAllowed;
	}

	/**
	 * Returns a string containing the details of
	 * the Chequing account.
	 *
	 * <p>
	 * This method will display the full name, account number,
	 * current balance, account type, service charge per transaction,
	 * total service charges, number of transactions allowed,
	 * the list of transactions, and the final balance.
	 *
	 * @return		Chequing account details
	 * @override
	 */
	@Override
	public String toString(){
		// Formatting currency
		String serviceChargePerTransaction = currencyFormat.format(this.serviceChargePerTransaction.doubleValue());
		String totalServiceCharges = currencyFormat.format(this.totalServiceCharges.doubleValue());
		String finalBalance = currencyFormat.format(this.getBalance());

		String listOfTransactions = getTransactionsList();

		StringBuffer output = new StringBuffer("");
		output.append(super.toString());
		output.append("Type: " + ACCOUNT_TYPE + "\n");
		output.append("Service Charge: " + serviceChargePerTransaction + "\n");
		output.append("Total Service Charges: " + totalServiceCharges + "\n");
		output.append("Number of Transactions Allowed: " +
						this.numberOfTransactionsAllowed + "\n");
		output.append(listOfTransactions);


		output.append("Final Balance: " + finalBalance + "\n");
		return output.toString();
	}

	/**
	 * Generates and returns a string containing a list of transactions
	 * for toString()
	 *
	 * @return	The list of transactions. "None", if no transactions.
	 */
	private String getTransactionsList(){
		StringBuffer output = new StringBuffer("List of Transactions: ");
		String transactionAmount;
		int lastElement = this.numberOfTransactionsUsed - 1;

		if (this.numberOfTransactionsUsed > 0){
			for (int i = 0; i < this.numberOfTransactionsUsed; i++){

				transactionAmount = formatAmount(transactions[i]);

				if (i == lastElement){
					output.append(transactionAmount + "\n");
				}else{
					output.append(transactionAmount + ", ");
				}
			}
		}
		else{
			output.append("None\n");
		}

		return output.toString();
	}

	/**
	 * Formats an amount to use two decimal places.
	 * @param amount	The amount to convert
	 * @return			The converted amount to two decimal places
	 */
	private String formatAmount(double amount){
		StringBuffer output = new StringBuffer("");
		BigDecimal formattedAmount = new BigDecimal(amount);

		//Formatting amount
		formattedAmount = formattedAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

		// Formatting output
		if (amount > 0) {
			output.append("+" + formattedAmount);
		}
		else{
			output.append(formattedAmount);
		}

		return output.toString();
	}
}
