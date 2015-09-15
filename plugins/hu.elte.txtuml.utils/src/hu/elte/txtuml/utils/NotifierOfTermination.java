package hu.elte.txtuml.utils;

import java.util.LinkedList;
import java.util.Queue;

public abstract class NotifierOfTermination {

	private final Queue<Runnable> queue = new LinkedList<>();
	private boolean terminated = false;

	public synchronized boolean isAlreadyTerminated() {
		return terminated;
	}

	public synchronized boolean addTerminationListener(Runnable r) {
		if (terminated) {
			return false;
		}
		queue.add(r);
		return true;
	}

	public synchronized void removeTerminationListener(Runnable r) {
		queue.remove(r);
	}

	protected synchronized void notifyAllOfTermination() {
		if (terminated) {
			return;
		}
		terminated = true;
		queue.forEach(Runnable::run);
		queue.clear();
	}

}
