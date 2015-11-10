package hu.elte.txtuml.api.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Multiple classes and interfaces defined in this file.
 */

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

	/**
	 * A queue of checks which are to be performed at the beginning of the next
	 * <i>execution step</i>.
	 * <p>
	 * See the documentation of {@link Model} for information about execution
	 * steps.
	 */
	private final Queue<CheckQueueEntry> checkQueue = new ConcurrentLinkedQueue<>();

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
	void send(Region target, Signal signal) {
		newMailboxEntry(() -> target.process(signal));
	}

	/**
	 * Puts the specified entry into the {@code mailbox}.
	 * 
	 * @param entry
	 *            the entry to put into the {@code mailbox}.
	 */
	private void newMailboxEntry(MailboxEntry entry) {
		try {
			mailbox.put(entry);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Sets the model executor to be shut down the moment when {@link #mailbox}
	 * becomes empty.
	 */
	void shutdown() {
		try {
			class ShutdownAction implements MailboxEntry {
				@Override
				public void execute() {
					if (mailbox.isEmpty()) {
						ModelExecutor.shutdownNow();
					} else {
						try {
							mailbox.put(new ShutdownAction());
						} catch (InterruptedException e) {
						}
					}
				}
			}

			mailbox.put(new ShutdownAction());
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Registers a check of the lower bound of the specified association end's
	 * multiplicity to be performed in the next <i>execution step</i>.
	 * <p>
	 * The lower bound of multiplicities might be offended temporarily but has
	 * to be restored before returning from the current <i>execution step</i>.
	 * <p>
	 * See the documentation of {@link Model} for information about execution
	 * steps.
	 * 
	 * @param obj
	 *            the object on the opposite end of the association
	 * @param assocEnd
	 *            the association end which's multiplicity is to be checked
	 */
	void checkLowerBoundOfMultiplcitiy(ModelClass obj,
			Class<? extends AssociationEnd<?, ?>> assocEnd) {
		checkQueue.add(() -> obj.checkLowerBound(assocEnd));
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
				while (true) {
					CheckQueueEntry entry = checkQueue.poll();
					if (entry == null) {
						break;
					}
					entry.performCheck();
				}

				mailbox.take().execute();
			}
		} catch (InterruptedException e) { // do nothing (finish thread)
		}
	}

}

/**
 * The entry type of {@link ModelExecutorThread}<code>.mailbox</code>. Stores
 * and prepares an asynchronous operation to be executed in the future.
 * <p>
 * Unusable by the user.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
@FunctionalInterface
interface MailboxEntry {

	/**
	 * Executes the asynchronous operation stored in this entry.
	 */
	void execute();
}

/**
 * The entry type of {@link ModelExecutorThread}<code>.checkList</code>. Stores
 * and prepares a check to be performed in the future.
 * <p>
 * Unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 * 
 */
@FunctionalInterface
interface CheckQueueEntry {

	/**
	 * Performs the prepared check.
	 */
	void performCheck();

}
