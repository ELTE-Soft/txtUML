package txtuml.importer;

@SuppressWarnings("serial")
public class ImportException extends Exception {
	public ImportException(String msg) {
		message = msg;
	}
	public String getMessage() {
		return message;
	}
	private String message;
}
