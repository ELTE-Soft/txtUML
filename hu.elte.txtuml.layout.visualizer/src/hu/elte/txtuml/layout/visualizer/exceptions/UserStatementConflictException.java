package hu.elte.txtuml.layout.visualizer.exceptions;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;

/**
 * Exception indicating that conflicts were detected as a result of the user
 * statements.
 * 
 * @author Balázs Gregorics
 */
public class UserStatementConflictException extends ConflictException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Conflicted Statement.
	 */
	public Statement ConflictStatement;
	
	/**
	 * Create ConflictException.
	 */
	public UserStatementConflictException()
	{
		super();
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            The conflicted statement.
	 */
	public UserStatementConflictException(Statement s)
	{
		super();
		ConflictStatement = new Statement(s);
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public UserStatementConflictException(String m)
	{
		super(m);
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param m
	 *            Message of the exception.
	 * @param s
	 *            The conflicted statement.
	 */
	public UserStatementConflictException(String m, Statement s)
	{
		super(m);
		ConflictStatement = new Statement(s);
	}
}
