package txtuml.api;

import java.util.concurrent.LinkedBlockingQueue;

//WARNING: file is in a completely temporary state.

//currently singleton, will have more instances in the future

class ModelExecutorThread extends Thread {
	
	private ModelExecutor<?> executor;
	private LinkedBlockingQueue<QueueEntry> mailbox;
		
	ModelExecutorThread(Group group) {
		super(group, null, "ExecutorThread");
		this.executor = group.getExecutor();
		this.mailbox = new LinkedBlockingQueue<>(); 
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
		try { // TODO component
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
	
	static class Group extends ThreadGroup {

		private ModelExecutor<?> executor;
		
		public Group(ModelExecutor<?> executor) {
			super("ModelExecutorThreadGroup");
			this.executor = executor;
		}

		ModelExecutor<?> getExecutor() {
			return executor;
		}
	}
}
