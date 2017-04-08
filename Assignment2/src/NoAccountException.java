import java.rmi.*;
@SuppressWarnings("serial")
public class NoAccountException extends Exception{
	public String toString(){
		return "\nNo accounts found!\n";
	}
}
