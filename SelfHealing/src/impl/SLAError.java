package impl;

public class SLAError {
	public final SLAField field;
	public final String message;
	
	public SLAError(SLAField field, String message) {
		super();
		this.field = field;
		this.message = message;
	}
	
	@Override
	public String toString(){
		return field + ": " + message;
	}
}
