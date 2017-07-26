package hu.elte.txtuml.api.stdlib.impl.timers;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Runtime;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.stdlib.timers.Timer;

public class TimerImpl implements Timer {

	private final Callable<Void> action;
	private ScheduledFuture<Void> future;

	/**
	 * Creates a new {@code TimerImpl}.
	 * 
	 * @param targetObj
	 *            the target of the delayed send operation
	 * @param signal
	 *            the signal to send after the timeout
	 * @param millisecs
	 *            millisecs to wait before the timeout
	 */
	public TimerImpl(ModelClass targetObj, Signal signal, int millisecs) {
		this.action = () -> {
			Action.send(signal, targetObj);
			return null;
		};
		this.future = schedule(millisecs);
	}

	@Override
	public int query() {
		return (int) future.getDelay(TimeUnit.MILLISECONDS);
	}

	@Override
	public void reset(int millisecs) {
		ScheduledFuture<Void> newFuture = schedule(millisecs);
		cancel();
		this.future = newFuture;
	}

	@Override
	public void add(int millisecs) {
		reset(query() + millisecs);
	}

	@Override
	public boolean cancel() {
		boolean isDone = future.isDone();
		future.cancel(false);
		return isDone;
	}

	private ScheduledFuture<Void> schedule(int millisecs) {
		return Runtime.currentRuntime().schedule(action, millisecs, TimeUnit.MILLISECONDS);
	}

}
