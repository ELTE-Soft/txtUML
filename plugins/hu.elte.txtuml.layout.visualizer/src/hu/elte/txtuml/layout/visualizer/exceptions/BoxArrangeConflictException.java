package hu.elte.txtuml.layout.visualizer.exceptions;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;

import java.util.List;

/**
 * Exception indicating that conflicts were detected as a result of the user
 * statements during Box arrangement.
 * 
 * @author Balázs Gregorics
 */
public class BoxArrangeConflictException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Gets or sets the Statement that caused conflict.
	 */
	public List<Statement> ConflictStatements;
	
	/**
	 * Create ConflictException.
	 * 
	 * @param m
	 *            Message to show.
	 */
	public BoxArrangeConflictException(String m)
	{
		super(m);
		ConflictStatements = null;
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            Statement that caused conflict.
	 */
	public BoxArrangeConflictException(List<Statement> s)
	{
		super();
		ConflictStatements = s;
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            Statement that caused conflict.
	 * @param m
	 *            Message to show.
	 */
	public BoxArrangeConflictException(List<Statement> s, String m)
	{
		super(m);
		ConflictStatements = s;
	}
}
