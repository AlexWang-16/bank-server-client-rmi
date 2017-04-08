package edu.btp400.w2017.common;

@SuppressWarnings("serial")
public class NoAccountException extends Exception{
	@Override
	public String toString(){
		return "\nNo accounts found!\n";
	}
}
