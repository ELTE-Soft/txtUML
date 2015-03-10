package txtuml.api;

import java.util.LinkedList;
import java.util.Queue;

class ModelExecutorThread extends Thread {

	private final Object lockOnMailbox = new Object();
	private LinkedBlockingQueue<QueueEntry> mailbox;
	private ModelExecutor<?> executor;

	ModelExecutorThread(ModelExecutor<?> executor) {
		this.mailbox = new LinkedBlockingQueue<>();
		this.executor = executor;
	}

	ModelExecutor<?> getExecutor() {
		return executor;
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
