/**
 * This class consists of the fields of an account, which includes
 * fullName, firstName, lastName, accountNumber, and the account's currentBalance.
 *
 * This class has built in methods to retrieve and set the information as
 * noted above. User can only provide full names, not first name or last
 * name independently. Full names provided by user will be checked for
 * format conformity. Format is "Last name, First name". Account numbers are
 * numbers provided in strings. Names and account numbers that do not conform
 * to standard will display as an empty string. There are also two overloaded
 * methods implemented in this class: 1) equals(), which is capable of checking
 * if an object passed by the user as argument is the same as the account being
 * referenced. 2) hashCode(), which generates a custom hashCode for each account
 * This method can be used later on to verify if two accounts contain the same
 * information.
 *
 * @author Alex Wang
 * @version 3.2
 */
import java.text.*;
import java.util.*;
import java.io.Serializable;
import java.math.BigDecimal;

public class Account implements Serializable{

  /**
	 * 
	 */
	private static final long serialVersionUID = -1113605699626469692L;
	
protected NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);
  protected static final double SMALLEST_DOUBLE_VALUE = -1 * Double.MAX_VALUE;
  protected static final double LARGEST_DOUBLE_VALUE = Double.MAX_VALUE;
  private String fullName;
  private String firstName;
  private String lastName;
  private String accountNumber;
  private BigDecimal currentBalance;

  /**
   * An empty constructor that generates an empty account
   */
  public Account() {
	  this ("", "", 0);
  }

  /**
   * A constructor that takes the user's information and creates an account.
   * <p>
   * The constructor will perform checking to ensure a valid balance is provided.
   * If the amount is less than 0, the amount will be set to a safe empty state.
   * The assumption made on fullName is that the user will provide his/her
   * first and last name. The constructor will not check if the user has only
   * provided his/her first name or that they haven't provided anything at all.
   *
   * @param fullName            The user's full name
   * @param accountNumber       The user's account number
   * @param currentBalance      The account's current balance
   */
  public Account(String fullName, String accountNumber, double startingBalance) {
    setFullName(fullName);
    setFirstNameAndLastName();
    setAccountNumber(accountNumber);
    setCurrentBalance(startingBalance);
  }

  /**
   * Provides the user with account holder's first name.
   *
   * @return    The user's first name
   */
  public String getFirstName() { return firstName; }

  /**
   * Provides the user with the account holder's last name.
   *
   * @return    The user's last name
   */
  public String getLastName() { return lastName; }

  /**
   * Returns the account holder's full name. If it is null, it will return
   * null.
   *
   * @return    The account holder's full name
   */
  public String getFullName() { return fullName; }

  /**
   * Retrieves the account number.
   *
   * @return The account number
   */
  public String getAccountNumber() { return accountNumber; }

  /**
   * Retrieves the current balance in the account.
   *
   * @return    the current balance of the account
   */
  public double getBalance() { return this.currentBalance.doubleValue(); }

/**
 * Stores an account holder's name into the account.
 *
 * @param fullName        Account holder's first and last name
 */
public void setFullName(String fullName) {
  this.fullName = fullName;
  if (!firstNameAndLastNameIsValid()) {
    this.fullName = "";
  }
}

/**
 * Checks to see if the first name and last name
 * is valid.
 * <p>
 * If valid, set the first name and last name otherwise, set both to empty
 * string.
 *
 */
private void setFirstNameAndLastName() {
  if (firstNameAndLastNameIsValid()) {
    setFirstName();
    setLastName();
  }
  else {
    this.firstName = "";
    this.lastName = "";
  }
}

/**
 * Checks to see if first name and last name is
 * valid.
 * <p>
 * This method will return true if the first and last name is valid based on
 * the current object's full name.
 *
 * @see       setFirstNameAndLastName()
 * @see       setFullName()
 * @see       Account() constructor
 * @return    True if valid, false if not valid.
 *
 */
private boolean firstNameAndLastNameIsValid () {
  boolean result = false;

  if (fullName != null) {
    String[] splitName = fullName.split(",");
    if (splitName.length == 2) {
      String firstName = splitName[1];
      String lastName = splitName[0];

      if (lastNameIsValid(lastName) && firstNameIsValid(firstName)) {
                  result = true;
      }
    }
  }
  return result;
}

/**
 * Extracts the first name from the full name and assign it
 * as the field for the first name.
 *
 */
private void setFirstName() {
    this.firstName = extractFirstName();
}

/**
 * Extracts the lastName from the full name and assign it as the
 * last name
 */
private void setLastName() {
  this.lastName = extractLastName();
}

/**
 * Reports if last name within the full name is valid
 *
 * @param fullName      Account holder's first name and last name
 * @return              True if the last name is in valid format
 */
private Boolean lastNameIsValid(String lastName) {
  Boolean result = false;
  if (lastName.length() > 1) {
    result = true;
  }
  return result;
}

/**
 * Extracts the account holder's first name from the full name provided.
 * <p>
 * This method assumes the full name has already been validated and attempts
 * to perform the extraction after being called by setLastName().
 *
 * @see                 setLastName()
 * @return              The account holder's last name
 */
public String extractLastName() {
  String[] splitName = this.fullName.split(",");
  String lastName = splitName[0].trim();
  return lastName;
}

/**
 * Reports if first name within the full name is valid
 * <p>
 * This method will check for an empty space before the first name
 * and a length of greater than 1
 *
 * @param firstName     Account holder's first name
 * @return              True if the first name is in valid format
 */
public Boolean firstNameIsValid(String firstName) {
  Boolean result = false;
  if (firstName.indexOf(' ') == 0 && firstName.length() > 1) {
      result = true;
  }
  return result;
}

/**
 * Extracts the account holder's first name from the full name provided.
 * This method assumes the full name has already been validated.
 *
 * @return              The account holder's first name
 */
public String extractFirstName() {
  String[] splitName = this.fullName.split(",");
  String firstName = splitName[1].trim();
  return firstName;
}

  /**
   * Stores the account number into the account.
   *
   * @param accountNumber   Account holder's account number
   */
  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * Modifies the account's current balance.
   *
   * @param balance         The target balance in the account
   */
  protected void setCurrentBalance(double balance) {
    if (balance >= 0 && balance < Double.MAX_VALUE) {
    		this.currentBalance = BigDecimal.valueOf(balance);
    	} else {
    		this.currentBalance = BigDecimal.valueOf(0);
    	}
  }

  /**
   * Prints account details.
   * <p>
   * This method will display the full name, account number, and the current
   * balance in the account.
   *
   * @return 		The account's details in a formatted string
   * @override
   */
  public String toString (){
    String formattedBalance = currencyFormat.format(this.currentBalance);

    StringBuffer details = new StringBuffer("");
    details.append("Name: " + getFullName() + "\n");
    details.append("Number: " + getAccountNumber() + "\n");
    details.append("Current Balance: " + formattedBalance + "\n");

    return details.toString();
  }

  /**
   * Compares each field of two objects to determine if the information they
   * hold are identical.
   * <p>
   * If null is passed into the method as argument, the result will be false.
   * This allows reliable checking of two objects whether or not it is
   * instantiated.
   *
   * @param foreignObject    The incoming object that is to be compared
   * @return      true if the two objects are equal and false if it is not
   * @override
   *
   */
  public boolean equals (Object foreignObject) {

    boolean result = false;

    if (foreignObject instanceof Account) {

      Account userAccount = (Account) foreignObject;

      if ((this.getFullName().equals(userAccount.getFullName())) &&
          (this.accountNumber == userAccount.accountNumber) &&
          (this.currentBalance.equals(userAccount.currentBalance))) {

          result = true;
      }
    }
    return result;
  }

  /**
   * Generates a 32 bit hash code for each account.
   *
   * @return  The account's unique 32-bit hash code
   * @override
   */
  public int hashCode() {
    int hash = 47;
    hash *= 3 + firstName.hashCode();
    hash *= 5 + lastName.hashCode();
    hash /= 2 + accountNumber.hashCode();
    hash *= 9 + currentBalance.doubleValue();
    return hash;
  }

  /**
   * Checks to see if balance is sufficient for withdrawal. If yes, update
   * account balance.
   *
   * @param  amount    The amount to withdraw
   * @return boolean   True if withdraw operation is successful, false otherwise
   */
   public boolean withdraw(double amount){
    if (amount > 0.0 && amount <= this.currentBalance.doubleValue()){
      setCurrentBalance(this.currentBalance.doubleValue() - amount);
      return true;
    }
    else {
      return false;
    }
   }

   /**
    * Checks to see if amount is positive. If yes, update account balance.
    *
    * @param  amount    The amount to deposit
    */
    public void deposit(double amount){
     if (amount > 0 && amount < LARGEST_DOUBLE_VALUE){
       setCurrentBalance(this.currentBalance.doubleValue() + amount);
     }
    }
}
