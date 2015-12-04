package hu.elte.txtuml.layout.visualizer.events;

import java.util.Observer;

/**
 * A static class that handles the event emitter.
 * 
 * @author Balázs Gregorics
 *
 */
public class ProgressManager
{
	private static ProgressEmitter _progressEmitter;
	
	/**
	 * Returns the Event Emitter.
	 * 
	 * @return the Event Emitter.
	 */
	public static ProgressEmitter getEmitter()
	{
		return _progressEmitter;
	}
	
	/**
	 * Method to start the Event Emitter
	 */
	public static void start()
	{
		_progressEmitter = new ProgressEmitter();
	}
	
	/**
	 * Method to add an observer to the Event Emitter.
	 * 
	 * @param o
	 *            Observer to add.
	 */
	public static void addObserver(Observer o)
	{
		_progressEmitter.addObserver(o);
	}
	
	/**
	 * Method to shut down the Event Emitter.
	 */
	public static void end()
	{
		_progressEmitter.deleteObservers();
	}
}
