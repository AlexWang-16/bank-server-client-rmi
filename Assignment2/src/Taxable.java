/**
 * A taxable interface for bank accounts that are taxable
 * 
 * @author awang05
 * @version 0.1
 */

interface Taxable {
	public void calculateTax(int taxRate);
	double getTaxAmount();
	String createTaxStatement();
}
