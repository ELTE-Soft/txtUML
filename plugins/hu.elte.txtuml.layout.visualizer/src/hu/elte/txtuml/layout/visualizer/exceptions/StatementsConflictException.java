package hu.elte.txtuml.layout.visualizer.exceptions;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;

import java.util.ArrayList;

/**
 * Exception indicating that conflicts were detected in the statements.
 * 
 * @author Balázs Gregorics
 */
public class StatementsConflictException extends ConflictException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Conflicted Statements.
	 */
	public ArrayList<Statement> ConflictStatements;
	
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
	public StatementsConflictException(ArrayList<Statement> ss)
	{
		super();
		ConflictStatements = new ArrayList<Statement>(ss);
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
	public StatementsConflictException(String m, ArrayList<Statement> ss)
	{
		super(m);
		ConflictStatements = new ArrayList<Statement>(ss);
	}
}
