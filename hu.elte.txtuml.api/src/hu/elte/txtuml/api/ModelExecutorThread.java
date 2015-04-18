package hu.elte.txtuml.api;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Multiple classes and interfaces defined in this file.
 */

// TODO check

/**
 * The type of threads on which the model execution will run. By the current
 * implementation, this class might be considered singleton as it is
 * instantiated only once by the {@link ModelExecutor} class.
 * <p>
 * Unusable by the user.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
class ModelExecutorThread extends Thread {

	/**
	 * The mailbox in which the to-be-sent signals are gathered and later
	 * processed (asynchronously).
	 */
	private final LinkedBlockingQueue<MailboxEntry> mailbox = new LinkedBlockingQueue<>();

	private final Queue<CheckQueueEntry> checkQueue = new LinkedList<>();

	/**
	 * Sole constructor of package private <code>ModelExecutorThread</code>.
	 * Creates and also starts the thread.
	 */
	ModelExecutorThread() {
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
			mailbox.put(new MailboxEntry(target, signal));
		} catch (InterruptedException e) {
		}
	}

	void checkLowerBoundOfMultiplcitiy(ModelClass obj,
			Class<? extends AssociationEnd<?>> assocEnd) {
		checkQueue.add(new LowerBoundOfMultiplicityCheck(obj, assocEnd));
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
				checkQueue.forEach(entry -> entry.performCheck());
				mailbox.take().processSignal();
			}
		} catch (InterruptedException e) { // do nothing (finish thread)
		}
	}

}

/**
 * The entry type of {@link ModelExecutorThread#mailbox}. Represents a send
 * operation.
 * <p>
 * Unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 * 
 */
class MailboxEntry {

	/**
	 * The target object of the represented send operation.
	 */
	private final ModelClass target;

	/**
	 * The signal of the represented send operation.
	 */
	private final Signal signal;

	/**
	 * Creates a new <code>QueueEntry</code> to represent a send operation.
	 * 
	 * @param target
	 *            the target object of the represented send operation
	 * @param signal
	 *            the signal of the represented send operation
	 */
	MailboxEntry(ModelClass target, Signal signal) {
		this.target = target;
		this.signal = signal;
	}

	void processSignal() {
		target.process(signal);
	}
}

/*
 * <p>
 * Unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 * 
 */
interface CheckQueueEntry {

	void performCheck();

}

/*
 * <p>
 * Unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 * 
 */
class LowerBoundOfMultiplicityCheck implements CheckQueueEntry {

	private final ModelClass obj;

	private final Class<? extends AssociationEnd<?>> assocEnd;

	LowerBoundOfMultiplicityCheck(ModelClass obj,
			Class<? extends AssociationEnd<?>> assocEnd) {
		this.obj = obj;
		this.assocEnd = assocEnd;
	}

	@Override
	public void performCheck() {
		obj.checkLowerBound(assocEnd);
	}

}