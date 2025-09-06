package in.gov.egramswaraj.exception;

public class SomeUsersNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SomeUsersNotFoundException(String message) {
		super(message);
	}
}