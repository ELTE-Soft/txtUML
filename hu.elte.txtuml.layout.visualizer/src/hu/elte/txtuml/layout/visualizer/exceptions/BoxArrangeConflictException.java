package hu.elte.txtuml.layout.visualizer.exceptions;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;

import java.util.ArrayList;

/**
 * Exception indicating that conflicts were detected as a result of the user
 * statements during Box arrangement.
 * 
 * @author Balázs Gregorics
 */
public class BoxArrangeConflictException extends ConflictException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Gets or sets the Statement that caused conflict.
	 */
	public ArrayList<Statement> ConflictStatement;
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            Statement that caused conflict.
	 */
	public BoxArrangeConflictException(ArrayList<Statement> s)
	{
		super();
		ConflictStatement = s;
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            Statement that caused conflict.
	 * @param m
	 *            Message to show.
	 */
	public BoxArrangeConflictException(ArrayList<Statement> s, String m)
	{
		super(m);
		ConflictStatement = s;
	}
}
