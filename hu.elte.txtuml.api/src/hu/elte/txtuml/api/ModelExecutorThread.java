package hu.elte.txtuml.api;

import java.util.concurrent.LinkedBlockingQueue;

class ModelExecutorThread extends Thread {

	private final LinkedBlockingQueue<QueueEntry> mailbox;

	ModelExecutorThread() {
		this.mailbox = new LinkedBlockingQueue<>();

		start();
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
				entry.target.process(entry.signal);
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
