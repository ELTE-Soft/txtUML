package hu.elte.txtuml.validation;



/**
 * Instances of the subclasses are referenced from
 * validation error messages to be able to provide
 * information about error location and file name.
 */
public abstract class SourceInfo {
	
	/**
	 * Computes the line index of the given character index in the 
	 * associated source file.
	 *  
	 * @param charIndex	Index of the character.
	 * @return	Line number of the referenced character.
	 */
	public abstract int getSourceLineNumber(int charIndex);
	
	/**
	 * Provides the name of the file containing the source.
	 * 
	 * @return	Name of the file.
	 */
	public abstract String getOriginatingFileName();
}
