package hu.elte.txtuml.api;

import java.util.concurrent.LinkedBlockingQueue;

// TODO check

/*
 * Currently singleton, will have more instances in the future.
 */
class ModelExecutorThread extends Thread {

	private static final ModelExecutorThread instance = new ModelExecutorThread();;
	private LinkedBlockingQueue<QueueEntry> mailbox;

	private ModelExecutorThread() {
		this.mailbox = new LinkedBlockingQueue<>();
		this.start();
	}

	static ModelExecutorThread getSingletonInstance() {
		return instance;

	}

	void send(ModelClass target, Signal signal) {
		try {
			mailbox.put(new QueueEntry(target, signal));
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				QueueEntry entry = mailbox.take();
				entry.target.processSignal(entry.signal);
			}
		} catch (InterruptedException e) { // do nothing (finish thread)
		}
	}

	private static class QueueEntry {

		ModelClass target;
		Signal signal;

		QueueEntry(ModelClass target, Signal signal) {
			this.target = target;
			this.signal = signal;
		}
	}
}
