/**
 * A taxable interface for bank accounts that are taxable
 *
 * @author awang05
 * @version 0.2
 */
package edu.btp400.w2017.common;

interface Taxable {
	public void calculateTax(int taxRate);
	double getTaxAmount();
	String createTaxStatement();
}
