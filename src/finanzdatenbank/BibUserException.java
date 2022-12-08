package finanzdatenbank;

public class BibUserException extends Exception {
	
	public BibUserException(Exception e) {
		super(e.getMessage());
	}
	
}
