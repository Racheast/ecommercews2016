package exceptions;

public class EdgeFailureException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8617120493240346232L;

	public EdgeFailureException(String message){
		super(message);
		System.out.println(message);
	}
}
