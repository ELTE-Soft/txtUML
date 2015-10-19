package hu.elte.txtuml.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class NotifierOfTermination {

	private final List<Runnable> list = new ArrayList<>();
	private boolean terminated = false;

	public synchronized boolean isAlreadyTerminated() {
		return terminated;
	}

	public synchronized boolean addTerminationListener(Runnable r) {
		if (terminated) {
			return false;
		}
		list.add(r);
		return true;
	}

	public synchronized void removeTerminationListener(Runnable r) {
		list.remove(r);
	}

	protected synchronized void notifyAllOfTermination() {
		if (terminated) {
			return;
		}
		terminated = true;
		list.forEach(Runnable::run);
		list.clear();
	}

}
