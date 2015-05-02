package hu.elte.txtuml.export.uml2.utils;

public final class ImportWarningProvider {

	/**
	 * Writes an import warning with the given warning message to the standard output.
	 * @param msg The warning message.
	 *
	 * @author Adam Ancsin
	 */
	public static void createWarning(String msg)
	{
		System.out.println("Warning: " + msg);
	}

}
