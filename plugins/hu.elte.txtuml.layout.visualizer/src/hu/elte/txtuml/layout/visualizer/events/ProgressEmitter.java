package hu.elte.txtuml.layout.visualizer.events;

import java.util.Observable;

/**
 * This class emits the events the algorithm fires.
 * 
 * @author Balázs Gregorics
 *
 */
public class ProgressEmitter extends Observable
{
	/**
	 * Emits an event at the start of the arrangement of boxes.
	 */
	public void OnBoxArrangeStart()
	{
		setChanged();
		notifyObservers(10);
	}
	
	/**
	 * Emits an event at the half of the arrangement of boxes.
	 */
	public void OnBoxArrangeHalf()
	{
		setChanged();
		notifyObservers(20);
	}
	
	/**
	 * Emits an event at the start of the arrangement of overlapped boxes.
	 */
	public void OnBoxOverlapArrangeStart()
	{
		setChanged();
		notifyObservers(30);
	}
	
	/**
	 * Emits an event at the end of the arrangement of overlapped boxes.
	 */
	public void OnBoxOverlapArrangeEnd()
	{
		setChanged();
		notifyObservers(50);
	}
	
	/**
	 * Emits an event at the start of the arrangement of links.
	 */
	public void OnLinkArrangeStart()
	{
		setChanged();
		notifyObservers(60);
	}
	
	/**
	 * Emits an event at the first quarter of the arrangement of links.
	 */
	public void OnLinkArrangeFirstQuarter()
	{
		setChanged();
		notifyObservers(70);
	}
	
	/**
	 * Emits an event at the half of the arrangement of links.
	 */
	public void OnLinkArrangeHalf()
	{
		setChanged();
		notifyObservers(80);
	}
	
	/**
	 * Emits an event at the third quarter of the arrangement of links.
	 */
	public void OnLinkArrangeThirdQuarter()
	{
		setChanged();
		notifyObservers(90);
	}
	
	/**
	 * Emits an event at the end of the arrangement of links.
	 */
	public void OnLinkArrangeEnd()
	{
		setChanged();
		notifyObservers(100);
	}
	
	/**
	 * Constructor of ProgressEmitter.
	 */
	public ProgressEmitter()
	{
		super();
	}
}
