package exceptions;

public class PMFailureException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2965478413550637649L;

	public PMFailureException(String message){
		super(message);
		System.out.println(message);
	}
}
