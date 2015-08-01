package hu.elte.txtuml.layout.visualizer.exceptions;

import hu.elte.txtuml.layout.visualizer.statements.Statement;

import java.util.List;

/**
 * Exception indicating that conflicts were detected in the statements.
 * 
 * @author Balázs Gregorics
 */
public class StatementsConflictException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Conflicted Statements.
	 */
	public List<Statement> ConflictStatements;
	
	/**
	 * Create ConflictException.
	 */
	public StatementsConflictException()
	{
		super();
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param ss
	 *            The conflicted statement.
	 */
	public StatementsConflictException(List<Statement> ss)
	{
		super();
		ConflictStatements = ss;
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public StatementsConflictException(String m)
	{
		super(m);
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param m
	 *            Message of the exception.
	 * @param ss
	 *            The conflicted statement.
	 */
	public StatementsConflictException(String m, List<Statement> ss)
	{
		super(m);
		ConflictStatements = ss;
	}
}
