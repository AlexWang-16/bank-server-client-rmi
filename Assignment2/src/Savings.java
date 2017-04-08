
/**
 * The Savings class emulates a savings bank account by extending the Account
 * superclass. It is taxable and therefore, implements the taxable interface.
 * It implements the following methods from Taxable interface: calculateTax(),
 * createTaxStatement(), and getTaxAmount().
 *
 * @author awang05
 * @version 1.3
 */
import java.math.BigDecimal;
import java.io.Serializable;

 public class Savings extends Account implements Taxable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8017038525303298031L;
	
	public static final String ACCOUNT_TYPE = "SAV";
	private double annualInterestRate;
	private BigDecimal interestIncome;
	private BigDecimal taxAmount;
	private int taxRate;
	
	// For internal hash code generation only
	private double bogusAnnualInterestRate;	

	/**
	 * Default constructor
	 */
	public Savings() {
		this ("", "", 0, 0.10);
	}

	/**
	 * 4 Argument constructor, which uses use defined
	 * parameters to setup a savings account.
	 *
	 * @param fullName			The account holder's full name
	 * @param accountNumber		The account's number
	 * @param startingBalance	The account's starting balance
	 * @param interestRate		The interest rate for the account
	 */
	public Savings(String fullName, String accountNumber, double startingBalance, double interestRate) {
		super(fullName, accountNumber, startingBalance);
		setAnnualInterestRate(interestRate);
		setInterestIncome(0);
		setTaxAmount(0);
		this.calculateTax(15);
	}

	/**
	 * Validates the incoming interest rate.
	 * If interest rate is 0.1 or higher, set the user
	 * specified interest rates. Otherwise, default to
	 * 0.1 as interest rate.
	 *
	 * @param interestRate		The interest rate being set
	 */
	private void setAnnualInterestRate(double interestRate){
		if (annualInterestRateIsValid(interestRate)){
			this.annualInterestRate = interestRate;
		}else{
			this.annualInterestRate = 0.1;
			setBogusAnnualInterestRate(interestRate);
		}
	}

	/**
	 * Returns true if the annual interest rate is greater
	 * or equal to 0.1 and less than the maximum value of
	 * a Double. Otherwise, return false.
	 *
	 * @param interestRate		The interest rate being verified
	 * @return	True, if interest rate >= 0.1 &&
	 * 			interest rate < Double.MAX_VALUE.
	 * 			Otherwise false.
	 *
	 */
	private boolean annualInterestRateIsValid(double interestRate){
		if (interestRate >= 0.1 && interestRate < Double.MAX_VALUE){
			return true;
		}
		return false;
	}

	/**
	 * Saves the bogus interest rate. If overflow/underflow occurs,
	 * set the rate to -1. If the rate does not overflow or underflow,
	 * set it to the specified bogus rate. If the interest rate is
	 * actually valid, set the bogus interest rate to 0 to indicate
	 * no errors.
	 *
	 * @param interestRate		The bogus interest rate being set
	 */
	private void setBogusAnnualInterestRate(double interestRate){
		if (interestRate < SMALLEST_DOUBLE_VALUE || interestRate > LARGEST_DOUBLE_VALUE){
			this.bogusAnnualInterestRate = -1;
		}
		else if (interestRate > SMALLEST_DOUBLE_VALUE && interestRate < 0.1){
			this.bogusAnnualInterestRate = interestRate;
		}
		else{
			this.bogusAnnualInterestRate = 0;
		}
	}

	/**
	 * Returns the annual interest rate of the savings account.
	 *
	 * @return	Annual interest rate in percentage
	 */
	public double getAnnualInterestRate(){
		return this.annualInterestRate;
	}

	/**
	 * Validates the income. If income is valid, set the income.
	 * Otherwise, set the income to default value of 0.
	 *
	 * @param income		The income being set
	 */
	private void setInterestIncome(double income){
		if (income > 0 && income < LARGEST_DOUBLE_VALUE){
			this.interestIncome = BigDecimal.valueOf(income);
		}else{
			this.interestIncome = BigDecimal.valueOf(0);
		}
	}

	/**
	 * Returns the interest income of the current savings account.
	 *
	 * @return	Interest income
	 */
	public double getInterestIncome(){
		return this.interestIncome.doubleValue();
	}

	/**
	 * Validates the amount of tax being set. If valid, set
	 * the amount of tax. Otherwise, set the amount to default value 0.
	 *
	 * @param amount	The taxable amount
	 */
	private void setTaxAmount(double amount){
		if (amount > 0 && amount < LARGEST_DOUBLE_VALUE){
			this.taxAmount = BigDecimal.valueOf(amount);
		}else{
			this.taxAmount = BigDecimal.valueOf(0);
		}
	}

	/**
	 * Returns the amount of tax deducted from income.
	 *
	 * @return amount of tax deducted
	 */
	public double getTaxAmount() {
		calculateAmountOfTax();
		return this.taxAmount.doubleValue();
	}

	/**
	 * Calculates the amount of tax based on provided tax rate
	 * and interest income.
	 *
	 * @param taxRate	The tax rate used to calculate tax
	 */
	public void calculateTax(int taxRate) {
		setTaxRate(taxRate);
		calculateInterestIncome();
		calculateAmountOfTax();
	}

	/**
	 * Validates the incoming tax rate. If valid, set the tax
	 * rate of the savings account.
	 *
	 * @param taxRate	The tax rate being set
	 */
	private void setTaxRate(int taxRate){
		if (taxRate > 0 && taxRate <= 100){
			this.taxRate = taxRate;
		}else{
			this.taxRate = 15;
		}
	}

	/**
	 * Returns the current tax rate.
	 *
	 * @return	The current tax rate
	 */
	public int getTaxRate(){
		return this.taxRate;
	}

	/**
	 * Calculates interest income based on tax rate set
	 * and save the results to interestIncome field.
	 *
	 */
	private void calculateInterestIncome(){
		BigDecimal taxRate = new BigDecimal((this.annualInterestRate / 100));
		BigDecimal currentBalance = new BigDecimal(super.getBalance());
		
		currentBalance = currentBalance.multiply(taxRate);
		
		this.interestIncome = currentBalance; 
	}

	/**
	 * Calculates the amount of tax paid and saves the results
	 * to amountOfTax. Tax is only applied for interest income over $50.
	 * Interest income must be calculated first
	 * to obtain accurate results.
	 *
	 */
	private void calculateAmountOfTax(){
		if (this.interestIncome.doubleValue() > 50){
			BigDecimal taxRate = new BigDecimal((double) this.taxRate / 100);
			this.taxAmount = this.interestIncome.multiply(taxRate);
		} else {
			this.taxAmount = BigDecimal.valueOf(0);
		}
	}

	/**
	 * Generates and returns a string containing a formatted tax statement.
	 *
	 * @return	The tax statement
	 */
	public String createTaxStatement() {
		// Formatting currency
		String interestIncome = currencyFormat.format(this.getInterestIncome());
		String amountOfTax = currencyFormat.format(this.getTaxAmount());

		StringBuffer output = new StringBuffer("");
		output.append("Tax rate: " + this.getTaxRate() + "%\n");
		output.append("Account number: " + this.getAccountNumber() + "\n");
		output.append("Interest income: " + interestIncome + "\n");
		output.append("Amount of tax: " + amountOfTax + "\n");
		return output.toString();
	}


	/**
	 * Return true if the incoming object has the same contents as
	 * this savings account.
	 *
	 * @return	True, if incoming object is the same savings account.
	 * 			False, otherwise.
	 * @override
	 */
	public boolean equals (Object foreignObject){
		if (foreignObject instanceof Savings){
			Savings savingsAccount = (Savings) foreignObject;

			return (this.hashCode() == savingsAccount.hashCode());
		}

		return false;
	}

	/**
	 * Returns a string containing the details of
	 * the savings account.
	 *
	 * <p>
	 * This method will display the full name, account number, 
	 * current balance, account type, interest rate, interest income,
	 * and the final balance.
	 * 
	 * @return		Savings account details
	 * @override
	 */
	public String toString(){

		// Formatting Currency
		String interestIncome = currencyFormat.format(this.getInterestIncome());
		String finalBalance = currencyFormat.format(this.getBalance());

		StringBuffer output = new StringBuffer("");
		output.append(super.toString());
		output.append("Type: " + Savings.ACCOUNT_TYPE + "\n");
		output.append("Interest Rate: " + this.getAnnualInterestRate() + "%\n");
		output.append("Interest Income: " + interestIncome + "\n");
		output.append("Final Balance: " + finalBalance + "\n");
		return output.toString();
	}

	/**
	 * Computes the most to date interest income, then
	 * calculates and returns the final balance.
	 *
	 * @return The final balance
	 * @override
	 */
	public double getBalance(){
		this.calculateInterestIncome();
		BigDecimal finalBalance = new BigDecimal(super.getBalance()); 
		finalBalance = finalBalance.add(this.interestIncome);
		return finalBalance.doubleValue() ;
	}

	/**
	 * Generates a unique hash code for the savings account.
	 *
	 * @return	The unique hashCode for the savings account
	 */
	public int hashCode(){
		int hash = 52;
		hash *= 8 + super.hashCode();
		hash += this.getInterestIncome();
		hash += 500 + this.getTaxAmount();
		hash += this.getTaxRate();
		hash += this.getInterestRateHashCode();

		return hash;
	}

	/**
	 * Generates and returns the interest rate hash code
	 *
	 * @return Interest rate hashCode
	 */
	public double getInterestRateHashCode(){
		if (this.bogusAnnualInterestRate < 0){ 		//A bogus interest rate has been received
			return (this.bogusAnnualInterestRate * -1) * 20000;
		}
		else{
			return this.getAnnualInterestRate();
		}
	}

}
