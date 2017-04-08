/**
 * This class emulates a GIC account by extending the account class.
 * The GIC account is taxable and implements the following methods
 * from taxable interface: calculateTax, getTaxAmount(), and 
 * createTaxStatement().
 * 
 * @author awang05
 * @version 0.1
 */
import java.math.BigDecimal;
import java.io.Serializable;

public class GIC extends Account implements Taxable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8353971742285662870L;
	
	public static final String ACCOUNT_TYPE = "GIC"; 
	private static final int defaultTaxRate = 15;
	private int investmentPeriod;
	private double annualInterestRate;
	private int taxRate;
	private double taxAmount;
	private BigDecimal interestIncome;
	
	// For internal hash code generation only
	private double bogusAnnualInterestRate;
	private int bogusInvestmentPeriod;
	
	/**
	 * Default constructor initializes to default values.
	 */
	public GIC() {
		this ("", "" ,0, 1, 1.25);
	}

	public GIC(String fullName, String accountNumber, double startingBalance, 
				int investmentPeriod, double annualInterestRate) {
		super(fullName, accountNumber, startingBalance);
		setInvestmentPeriod(investmentPeriod);
		setAnnualInterestRate(annualInterestRate);
		calculateTax(defaultTaxRate);
	}

	/**
	 * Sets the investment period, after validation.
	 * 
	 * @param investmentPeriod
	 */
	private void setInvestmentPeriod(int investmentPeriod){
		if (investmentPeriodIsValid(investmentPeriod)){
			this.investmentPeriod = investmentPeriod;
		}else{
			this.investmentPeriod = 1;
			setBogusInvestmentPeriod(investmentPeriod);
		}
	}
	
	/**
	 * Validates the investment period.
	 * 
	 * @param investmentPeriod
	 * @return	True if the investment period is valid, false otherwise.
	 */
	private boolean investmentPeriodIsValid(int investmentPeriod){
		return (investmentPeriod > 0) && (investmentPeriod < 120);
	}
	
	/**
	 * Sets the bogus investment period, based on provided value
	 * for hashCode processing. If the investment period causes
	 * an overflow/underflow, set the bogus period to -1. If the value
	 * is within Integer range but negative, set the bogus period to 
	 * (bogus period - 2). If the value is within Integer range but
	 * higher than spec range, divide the bogus period by 2. If the
	 * value provided is within valid range, set the bogus period
	 * to default to 0.
	 *  
	 * @param investmentPeriod
	 */
	private void setBogusInvestmentPeriod(int investmentPeriod){
		if (investmentPeriod < Integer.MIN_VALUE || 
			investmentPeriod > Integer.MAX_VALUE){
			this.bogusInvestmentPeriod = -1;
		}
		else if (investmentPeriod > Integer.MIN_VALUE && 
				  investmentPeriod < 1){
			this.bogusInvestmentPeriod = investmentPeriod - 2;
		}
		else if (investmentPeriod > 120 && 
				 investmentPeriod < Integer.MAX_VALUE){
			this.bogusInvestmentPeriod = investmentPeriod / 2;
		}
		else{
			this.bogusInvestmentPeriod = 0;
		}
	}
	
	/**
	 * Validates and sets the annual interest rate. This is for
	 * internal use by the GIC class only.
	 */
	private void setAnnualInterestRate(double interestRate){
		if (annualInterestRateIsValid(interestRate)){
			this.annualInterestRate = interestRate;
		}else{
			this.annualInterestRate = 1.25;
			setBogusAnnualInterestRate(interestRate);
		}
	}
	
	/**
	 * Returns true if interest rate is between 1 and 100.
	 * 
	 * @param interestRate	The interest rate in question
	 * @return	True if interest rate is valid
	 */
	private boolean annualInterestRateIsValid(double interestRate){
		return (interestRate > 0 && interestRate <= 100);
	}
	
	/**
	 * Sets the bogus annual interest rate to the value
	 * provided for hashCode generation purposes. 
	 * If the rate is out of range of Double, set value to -5.
	 * If value is within range but is 0 or less, set bogus 
	 * annual interest rate to (bogus interest rate + 1).
	 * If the bogus interest rate is a valid interest rate,
	 * set bogus annual interest rate to 0 as default. 
	 * 
	 * @param interestRate	The interest rate in question
	 */
	private void setBogusAnnualInterestRate(double interestRate){
		if (interestRate < SMALLEST_DOUBLE_VALUE || 
			interestRate > LARGEST_DOUBLE_VALUE){
			this.bogusAnnualInterestRate = -1;
		}
		else if (interestRate > SMALLEST_DOUBLE_VALUE && 
				  interestRate <= 0){
			this.bogusAnnualInterestRate = interestRate + 
										(LARGEST_DOUBLE_VALUE / 4);
		}
		else{
			this.bogusAnnualInterestRate = 0;
		}
	}
	
	/**
	 * Sets the tax rate, if tax rate is valid.
	 * 
	 * @param taxRate	The user-defined tax rate
	 */
	private void setTaxRate(int taxRate){
		if (taxRateIsValid(taxRate)){
			this.taxRate = taxRate;
		}
		else{
			this.taxRate = 15;
		}
	}
	
	/**
	 * Validates if tax rate is valid. Tax rate
	 * is valid when it is greater than 0, but less
	 * than 101.
	 * 
	 * @return	True, if tax rate is valid.
	 */
	private boolean taxRateIsValid(int taxRate){
		return (taxRate > 0 && taxRate <= 100);
	}
	
	/**
	 * Calculate the amount of interest income based
	 * on maturity
	 */
	private void calculateInterestIncome(){
		double balanceAtMaturity = this.getBalance();
		
		BigDecimal interestIncome = new BigDecimal(balanceAtMaturity);
		BigDecimal startingBalanceBigDecimal = new BigDecimal(super.getBalance());
		
		interestIncome = interestIncome.subtract(startingBalanceBigDecimal);
		
		this.interestIncome = interestIncome;
	}
	
	/**
	 * Calculates the balance at maturity
	 * @return	Balance at maturity
	 */
	@Override
	public double getBalance(){
		
		BigDecimal startingBalance = new BigDecimal(super.getBalance());
		BigDecimal interest = new BigDecimal(Math.pow(1 
							+ (this.annualInterestRate / 100), 
								this.investmentPeriod));
		
		startingBalance = startingBalance.multiply(interest);
				
		startingBalance = startingBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return startingBalance.doubleValue();
	}
	
	/**
	 * Returns a string with the generated tax statement
	 * @return	Tax statement
	 */
	@Override
	public String createTaxStatement() {
		String interestIncome = currencyFormat.format(this.getInterestIncome());
		
		StringBuffer output = new StringBuffer("");
		output.append("Tax rate: " + this.getTaxRate() + "%\n");
		output.append("Account number: " + this.getAccountNumber() + "\n");
		output.append("Interest income: " + interestIncome + "\n");
		output.append("Amount of tax: $" + this.getTaxAmount() + "\n");
		return output.toString();
	}
	
	/**
	 * Returns the interest income.
	 * 
	 * @return	Interest income
	 */
	public double getInterestIncome(){
		calculateInterestIncome();
		return this.interestIncome.doubleValue();
	}
	
	/**
	 * Returns the calculated tax amount
	 * @return	The tax amount
	 */
	@Override
	public double getTaxAmount() {
		calculateTax(this.taxRate);
		return this.taxAmount;
	}
	
	/**
	 * Calculates tax amount based on provided tax rate
	 * 
	 * @param taxRate	The tax rate
	 */
	@Override
	public void calculateTax(int taxRate) {
		setTaxRate(taxRate);
		calculateInterestIncome();
		
		//Decimal Formatting
		BigDecimal taxRateBigDecimal = new BigDecimal((double) this.taxRate / 100);
		BigDecimal tax = this.interestIncome;
		tax = tax.multiply(taxRateBigDecimal);
		
		tax = tax.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		this.taxAmount = tax.doubleValue();
	}
	
	/**
	 * Returns the current tax rate.
	 * @return	taxRate		The current tax rate
	 */
	public int getTaxRate(){
		return this.taxRate;
	}

	/**
	 * Returns a string containing details of 
	 * the GIC account.
	 * 
	 * <p>
	 * This method will display the full name, account number, 
	 * current balance, account type, annual interest rate,
	 * period of investment, and the interest income at maturity.
	 * 
	 * @return		GIC account details
	 * @override 
	 */
	@Override
	public String toString(){
		StringBuffer output = new StringBuffer("");
		String balanceAtMaturity = currencyFormat.format(getBalance());
		BigDecimal interestIncome = new BigDecimal(getInterestIncome())
									.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal annualInterestRate = new BigDecimal(this.annualInterestRate).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		output.append(super.toString());
		output.append("Type: " + ACCOUNT_TYPE + "\n");
		output.append("Annual interest Rate: " + annualInterestRate + "%\n");
		output.append(getPeriodOfInvestmentString());
		output.append("Interest income at Maturity: " + interestIncome + "\n");
		output.append("Balance at Maturity: " + balanceAtMaturity + "\n");
		
		return output.toString();
	}
	
	public int getPeriodOfInvestment(){
		return this.investmentPeriod;
	}
	
	/**
	 * Generates the appropriate string for toString() when
	 * printing period of investment
	 * 
	 * @return	Pre-formatted period of investment 
	 */
	public String getPeriodOfInvestmentString(){
		StringBuffer output = new StringBuffer("Period of Investment: ");
		
		if (this.investmentPeriod > 1){
			output.append(this.investmentPeriod + " years\n");
		}else if (this.investmentPeriod == 1){
			output.append("1 year\n");
		}
		return output.toString();
	}
	
	/**
	 * Deposits the user specified amount, if the amount is valid.
	 * @override
	 */
	@Override
	public void deposit(double amount){
		if (transactionIsValid(amount)) {
			super.setCurrentBalance(super.getBalance() + amount);
		}
	}
	
	/**
	 * Returns true if deposit transaction is possible with
	 * the provided amount.
	 * 
	 * @param amount	The amount in question
	 * @return	True if the amount is within valid range
	 */
	private boolean transactionIsValid(double amount){
		return (amount > 0 && amount < LARGEST_DOUBLE_VALUE);
	}
	
	/**
	 * Return false to refuse allowing withdraw.
	 * @return	False
	 */
	@Override
	public boolean withdraw(double amount){
		return false;
	}

	/**
	 * Returns true if the incoming object is the same as
	 * the current GIC account, false otherwise.
	 * 
	 * @param	foreignObject	The incoming object to be 
	 * 							inspected
	 * 
	 * @return	True, if foreignObject is the same as this
	 * 			account. Otherwise return false.
	 */
	@Override
	public boolean equals(Object foreignObject){
		if (foreignObject instanceof GIC){
			GIC account = (GIC) foreignObject;
			return (this.hashCode() == account.hashCode());
		}
		return false;
	}
	
	/**
	 * Generates ad returns a unique GIC hash code
	 * 
	 * @return	A 32bit unique hash code
	 * @override
	 */
	@Override
	public int hashCode(){
		int hash = 18;
		hash *= 10 + super.hashCode();
		hash *= getPeriodOfInvestmentHashCode();
		hash += getAnnualInterestRateHashCode();
		return hash;
	}
	
	/**
	 * Generates the periods of investment hash
	 * code.
	 * 
	 * <p>
	 * If a bogus investment period was provided,
	 * use the bogus rate to generate the hash 
	 * code. Otherwise, use the valid period of
	 * investment.
	 * 
	 * @return
	 */
	private int getPeriodOfInvestmentHashCode(){
		if (this.bogusInvestmentPeriod != 0){
			return this.bogusInvestmentPeriod;
		}
		else{
			return getPeriodOfInvestment();
		}
	}
	
	/**
	 * Generates Annual interest rate hash code
	 * for hashCode()
	 * 
	 * <p>
	 * If a bogus annual interest rate was provided,
	 * use the bogus rate to generate the hash 
	 * code. Otherwise, use the valid annual 
	 * interest rate.
	 * 
	 * @return	Annual interest rate hash code
	 */
	private double getAnnualInterestRateHashCode(){
		if (this.bogusAnnualInterestRate != 0){
			return this.bogusAnnualInterestRate;
		}
		else{
			return this.annualInterestRate * 10000;
		}
	}

	/**
	 * Returns the annual interest rate.
	 * @return	The annual interest rate
	 */
	public double getAnnualInterestRate(){
		return this.annualInterestRate;
	}
}
