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
	private final LinkedBlockingQueue<IMailboxEntry> mailbox = new LinkedBlockingQueue<>();

	/**
	 * A queue of checks which are to be performed at the beginning of the next
	 * <i>execution step</i>.
	 * <p>
	 * See the documentation of {@link Model} for information about execution
	 * steps.
	 */
	private final Queue<ICheckQueueEntry> checkQueue = new ConcurrentLinkedQueue<>();

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

	/**
	 * Sets the model executor to be shut down the moment when {@link #mailbox}
	 * becomes empty.
	 */
	void shutdown() {
		try {
			class ShutdownAction implements IMailboxEntry {
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
				while (true) {
					ICheckQueueEntry entry = checkQueue.poll();
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
interface IMailboxEntry {

	/**
	 * Executes the asynchronous operation stored in this entry.
	 */
	void execute();
}

/**
 * The default implementation of {@link IMailboxEntry}. Stores and prepares a
 * send operation to be executed in the future.
 * <p>
 * Unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 * 
 */
class MailboxEntry implements IMailboxEntry {

	/**
	 * The target object of the stored send operation.
	 */
	private final ModelClass target;

	/**
	 * The signal of the stored send operation.
	 */
	private final Signal signal;

	/**
	 * Creates a new <code>QueueEntry</code> to store a send operation for
	 * future execution.
	 * 
	 * @param target
	 *            the target object of the stored send operation
	 * @param signal
	 *            the signal of the stored send operation
	 */
	MailboxEntry(ModelClass target, Signal signal) {
		this.target = target;
		this.signal = signal;
	}

	/**
	 * Executes the send operation stored in this entry.
	 */
	@Override
	public void execute() {
		target.process(signal);
	}
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
interface ICheckQueueEntry {

	/**
	 * Performs the prepared check.
	 */
	void performCheck();

}

/**
 * A kind of entry of {@link ModelExecutorThread}<code>.checkList</code>. Stores
 * a check of the lower bound of an association end's multiplicity to be
 * performed in the future.
 * <p>
 * Unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 */
class LowerBoundOfMultiplicityCheck implements ICheckQueueEntry {

	/**
	 * The object on the opposite end of the association.
	 */
	private final ModelClass obj;

	/**
	 * The association end which's multiplicity is to be checked.
	 */
	private final Class<? extends AssociationEnd<?>> assocEnd;

	/**
	 * Creates a new instance which stores a check of the lower bound of an
	 * association end's multiplicity to be performed in the future.
	 * 
	 * @param obj
	 *            the object on the opposite end of the association
	 * @param assocEnd
	 *            the association end which's multiplicity is to be checked
	 */
	LowerBoundOfMultiplicityCheck(ModelClass obj,
			Class<? extends AssociationEnd<?>> assocEnd) {
		this.obj = obj;
		this.assocEnd = assocEnd;
	}

	/**
	 * Performs the prepared lower bound check.
	 */
	@Override
	public void performCheck() {
		obj.checkLowerBound(assocEnd);
	}

}