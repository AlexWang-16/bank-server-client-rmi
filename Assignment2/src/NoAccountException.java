
@SuppressWarnings("serial")
public class NoAccountException extends Exception{
	@Override
	public String toString(){
		return "\nNo accounts found!\n";
	}
}
