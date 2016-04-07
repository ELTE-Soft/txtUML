package hu.elte.txtuml.api.stdlib.impl.timers;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.stdlib.timers.Timer;

public class TimerImpl implements Timer {

	/**
	 * Creates a new {@code Timer}.
	 * 
	 * @param obj
	 *            the target of the delayed send operation
	 * @param s
	 *            the signal to send after the timeout
	 * @param millisecs
	 *            millisecs to wait before the timeout
	 */
	public TimerImpl(ModelClass targetObj, Signal signal, int millisecs) {
		// TODO Auto-generated constructor stub

	}

	@Override
	public int query() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reset(int millisecs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(int millisecs) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}
}
