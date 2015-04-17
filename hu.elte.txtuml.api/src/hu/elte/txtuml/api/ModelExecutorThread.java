package hu.elte.txtuml.api;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * The type of threads on which the model execution will run. By the current
 * implementation, this class might be considered singleton as it is
 * instantiated only once by the {@link ModelExecutor} class' singleton
 * instance.
 * <p>
 * Unusable by the user.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
class ModelExecutorThread extends Thread {

	/**
	 * The global mailbox in which the to-be-sent signals are gathered and later
	 * processed (asynchronously).
	 */
	private final LinkedBlockingQueue<QueueEntry> mailbox;

	/**
	 * Sole constructor of package private <code>ModelExecutorThread</code>.
	 * Creates and also starts the thread.
	 */
	ModelExecutorThread() {
		this.mailbox = new LinkedBlockingQueue<>();

		start();
	}

	/**
	 * Sends the specified signal to the specified target object asynchronously.
	 * This method puts the signal into its global
	 * {@link ModelExecutorThread#mailbox mailbox} from where it will be taken
	 * and processed later.
	 * 
	 * @param target
	 *            the target of this send operation
	 * @param signal
	 *            the signal to send
	 */
	void send(ModelClass target, Signal signal) {
		try {
			mailbox.put(new QueueEntry(target, signal));
		} catch (InterruptedException e) {
		}
	}

	/**
	 * This method starts an infinite loop which might only be exited if an
	 * exception is raised inside. In every iteration, it takes an entry from
	 * the {@link ModelExecutorThread#mailbox mailbox} and gives it to the
	 * target object to process. Blocks if the <code>mailbox</code> is empty.
	 */
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

	/**
	 * The entry type of {@link ModelExecutorThread#mailbox}. Represents a send
	 * operation.
	 */
	private static class QueueEntry {

		/**
		 * The target object of the represented send operation.
		 */
		final ModelClass target;

		/**
		 * The signal of the represented send operation.
		 */
		final Signal signal;

		/**
		 * Creates a new <code>QueueEntry</code> to represent a send operation.
		 * 
		 * @param target
		 *            the target object of the represented send operation
		 * @param signal
		 *            the signal of the represented send operation
		 */
		QueueEntry(ModelClass target, Signal signal) {
			this.target = target;
			this.signal = signal;
		}
	}
}
