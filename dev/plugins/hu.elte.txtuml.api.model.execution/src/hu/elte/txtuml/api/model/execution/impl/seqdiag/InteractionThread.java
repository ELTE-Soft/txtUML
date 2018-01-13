package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import static com.google.common.util.concurrent.Uninterruptibles.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.error.PatternNotMetError;
import hu.elte.txtuml.api.model.impl.InteractionRuntime;
import hu.elte.txtuml.api.model.impl.SeqDiagThread;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.utils.Logger;

/**
 * The thread which executes an interaction.
 * <p>
 * See the documentation of this package for an overview about the threads used
 * in the sequence diagram executor.
 * <p>
 * This class keeps the invariants stated in the package documentation using its
 * inner invariants described at {@link #currentMessage}.
 */
@SequenceDiagramRelated
public class InteractionThread extends AbstractModelExecutor.OwnedThread<DefaultSeqDiagExecutor>
		implements InteractionRuntime, SeqDiagThread {

	/*
	 * This class takes the following steps:
	 * 
	 * 1. Blocks the model executor from termination to mark that it is working.
	 * 
	 * 2. Runs user code.
	 * 
	 * 3. When user code arrives to a message, this thread sends the message
	 * (only in case of a message from the actor), then releases the termination
	 * blocker and waits for the message to be consumed. If all threads are
	 * waiting, the model executor may terminate as no one else can send in
	 * messages. (If the model executor thread's mailbox is also empty, of
	 * course. But that is automatically checked by the shutdown mechanism
	 * inherited from the base model executor implementation.)
	 * 
	 * 4. When the model executor thread finds a message, it asks the root
	 * interaction thread to test the message. An interaction thread is either:
	 * 
	 * a) KILLED or TERMINATED. Nothing to do. Returns false.
	 * 
	 * b) WAITING_FOR_CHILDREN. This is possible in case of par fragments at the
	 * moment. Iterates over the children to test them instead.
	 * 
	 * c) ACTIVE. The last message is taken and compared to the actual. Returns
	 * based on the result.
	 * 
	 * 5. If the message was matched and removed, the model executor thread
	 * sends a message through the result queue to wake up the waiting
	 * interaction thread. And before that, it ensures that the termination
	 * blocker is added again to prevent the model executor thread reaching its
	 * loop condition and possibly terminating.
	 * 
	 * 6. This thread returns back to the user code (step 2). The model executor
	 * thread awaits this to ensure that the sequence diagram code after a
	 * message processing action is executed when the model is still in the
	 * exact state as it was when the previous signal has been processed.
	 * 
	 * 7. When the user code is completed, this thread simply terminates. (After
	 * clearing everything up and properly unregistering itself.)
	 * 
	 * For the case of par fragments, see the appropriate method.
	 * 
	 * The interaction threads do not terminate by themselves when they have
	 * 'nothing to do' as they always have something to do until they reach the
	 * end of the user code. Therefore they have to be killed by the kill()
	 * method if an early-stop is required. This is done by the model executor
	 * thread before that terminates.
	 * 
	 * For synchronization details, see the documentation of the currentMessage
	 * queue.
	 */

	// fields

	private static final AtomicLong count = new AtomicLong();

	private final Interaction wrapped;
	private final InteractionThread parent;

	/**
	 * The presence of this termination blocker shows that this thread is
	 * currently active and may send further signals to the model.
	 * 
	 * @see AbstractModelExecutor#addTerminationBlocker(Object)
	 */
	private final Object terminationBlocker = new Object();

	/**
	 * The model executor thread may wait on this queue when this thread is
	 * currently working to acquire the next message from the user code. This
	 * thread is <b>not</b> empty if and only if this thread is currently
	 * waiting or reached a terminal state.
	 * <p>
	 * Contains an empty Optional when this thread is not in {@link State#ACTIVE
	 * ACTIVE} state. (That empty Optional has to be put inside again when taken
	 * out to keep is invariant.)
	 * <p>
	 * Using these two invariants, we must ensure that at most one interaction
	 * thread may ever execute user code and the model executor thread may only
	 * execute user code when all interaction threads are waiting or terminated.
	 * <p>
	 * That is, the method {@link #awaitThisWaiting()} must be called whenever
	 * this thread is waken and has not yet reached a terminal state.
	 * <p>
	 * An interaction thread may be in four situations when
	 * {@link #awaitThisWaiting()} is called:
	 * <ul>
	 * <li>In a terminal state ({@link State#KILLED} or
	 * {@link State#TERMINATED}). See {@link #doRun}, {@link #kill},
	 * {@link #setExpected}.</li>
	 * <li>Waiting on the result thread after a message has been put into this
	 * queue ({@link State#ACTIVE} and non-empty Optional in this queue). See
	 * {@link #setExpected} and {@link #testActual}.</li>
	 * <li>Waiting for its children to finish
	 * ({@link State#WAITING_FOR_CHILDREN}). See {@link #par},
	 * {@link #unregister}, {@link #doRun}.</li>
	 * <li>Heading for one of the above.</li>
	 * </ul>
	 * 
	 * These are the different situations that have to be taken into
	 * consideration.
	 */
	private final BlockingQueue<Optional<Message>> currentMessage = new LinkedBlockingQueue<>();

	/**
	 * This is the result when waiting for the current message to be consumed.
	 */
	private final BlockingQueue<Result> result = new LinkedBlockingQueue<>();

	/**
	 * Contains the interaction threads that are created by this thread.
	 * <p>
	 * May only be accessed when holding the monitor of this list.
	 */
	private final Set<InteractionThread> children = new HashSet<>();

	/**
	 * The current state of this thread.
	 * <p>
	 * May only be accessed when holding the monitor of {@link #children}.
	 */
	private State state;

	private enum Result {
		MESSAGE_CONSUMED, WAKEN_BY_KILL
	}

	private enum State {
		ACTIVE, KILLED, WAITING_FOR_CHILDREN, TERMINATED
	}

	/**
	 * Thrown if {@link #kill} is called. Extends Error to make it less likely
	 * that it is caught by user code.
	 */
	@SuppressWarnings("serial")
	private static class Kill extends Error {
	}

	// constructor

	public InteractionThread(DefaultSeqDiagExecutor executor, Interaction interaction) {
		this(executor, null, interaction);
	}

	public InteractionThread(DefaultSeqDiagExecutor executor, InteractionThread parent, Interaction interaction) {
		super("Interaction-" + count.getAndIncrement(), executor);
		this.parent = parent;
		this.wrapped = interaction;
		this.state = State.ACTIVE;
		executor.addTerminationBlocker(terminationBlocker);
	}

	@Override
	public void start() {
		super.start();
		awaitThisWaiting();
		/*
		 * To make sure that the started thread executes everything it has to
		 * until its first waiting point.
		 */
	}

	@Override
	public void doRun() {
		try {
			getWrapped().run();
		} catch (Kill e) {
			// Nothing to do. Thread terminates.
		} catch (Throwable t) {
			Logger.sys.fatal("Unexpected exception on a sequence executor thread", t);
		}
		getExecutor().removeTerminationBlocker(terminationBlocker);
		// No longer required to block termination.

		synchronized (children) {
			boolean active = state == State.ACTIVE;
			state = State.TERMINATED;
			if (active) {
				currentMessage.offer(Optional.empty()); // Becomes inactive.
			}
		}

		if (parent != null) {
			/*
			 * This method locks on the 'children' list of the parent. For this
			 * reason, it is important to call it after putting an empty
			 * Optional into the currentMessage queue because the parent may
			 * wait on that.
			 */
			parent.unregister(this);
		}

		/*
		 * This thread may safely terminate at this point. It certainly has
		 * nothing more user code to consume. (Unlike the model executor thread
		 * which may get further tasks from asynchronous sends.)
		 */
	}

	@Override
	public void par(Interaction[] operands) {
		List<InteractionThread> copy; // Copying to release lock early.
		synchronized (children) {
			if (state == State.KILLED) {
				return;
			}
			state = State.WAITING_FOR_CHILDREN;
			currentMessage.offer(Optional.empty()); // Becomes inactive.
			for (Interaction interaction : operands) {
				children.add(new InteractionThread(getExecutor(), this, interaction));
			}
			copy = new ArrayList<>(children);
			copy.forEach(InteractionThread::start);
			while (true) {
				try {
					children.wait();
					return;
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * A child who has terminated, calls this thread. If all children are
	 * removed, this may become ACTIVE again.
	 */
	protected void unregister(InteractionThread child) {
		boolean becameActive = false;
		synchronized (children) {
			children.remove(child);
			if (children.isEmpty()) {
				state = State.ACTIVE;
				children.notify();
				becameActive = true;
			}
		}
		if (becameActive) {
			awaitThisWaiting();
		}
	}

	@Override
	public void message(ModelClass sender, Signal signal, ModelClass target) {
		Message message = Message.fromObject(sender, signal, target);
		setExpected(message);
	}

	@Override
	public void messageFromActor(Signal signal, ModelClass target) {
		API.send(signal, target);

		Message message = Message.fromActor(signal, target);
		setExpected(message);
	}

	private void setExpected(Message message) {
		getExecutor().removeTerminationBlocker(terminationBlocker);
		// It is important that the termination blocker here is released after
		// sending the signal in case of a message from actor. Otherwise the
		// model executor thread could terminate before this signal arrives.

		currentMessage.offer(Optional.of(message));

		// It is not represented by a specific state but this is an important
		// waiting point for this thread.

		Result r = takeUninterruptibly(result);

		if (r == Result.WAKEN_BY_KILL) {
			getExecutor().addError(new PatternNotMetError(message));
			throw new Kill();
		}
		// Here: r == Result.MESSAGE_CONSUMED

		// terminationBlocker is already added again, nothing more to do
	}

	/**
	 * Checks if the given actual Message matches this interaction or one of its
	 * children (if any). If it does match, the expected message is removed as
	 * well and the appropriate interaction steps forward.
	 * <p>
	 * Called from the model executor thread.
	 */
	public boolean testActual(Message actual) {
		Optional<Message> expected = takeUninterruptibly(currentMessage);

		if (expected.isPresent()) {
			return testActualWhenMessageIsPresent(expected, actual);
		} else {
			return testActualWhenMessageIsNotPresent(actual);
		}
	}

	private boolean testActualWhenMessageIsPresent(Optional<Message> expected, Message actual) {
		boolean answer = actual.matches(expected.get());
		if (answer) {
			/*
			 * This thread is going back to work. The termination blocker has to
			 * be added here. Otherwise the model executor thread (which is
			 * executing this method) may reach its loop condition and exit
			 * before the termination blocker could be added.
			 */
			getExecutor().addTerminationBlocker(terminationBlocker);

			result.offer(Result.MESSAGE_CONSUMED);

			/*
			 * We have to make sure (because of the sequence diagram semantics
			 * defined in the API) that the code between two sequence actions
			 * (calls to static methods of the Sequence class) are performed
			 * when the model is still in the state in which it was immediately
			 * after it has processed the last signal.
			 * 
			 * Therefore the model executor thread (who calls this method) must
			 * wait here.
			 * 
			 * It is also (for the above reason) required by the invariants
			 * detailed in the documentation of this package.
			 */
			awaitThisWaiting();
		} else {
			// Put it back to check again in the future.
			currentMessage.offer(expected);
		}
		return answer;
	}

	private boolean testActualWhenMessageIsNotPresent(Message actual) {
		List<InteractionThread> copy = null;
		// Copying children to release lock early.
		synchronized (children) {
			if (state == State.KILLED || state == State.TERMINATED) {
				currentMessage.offer(Optional.empty()); // Stays inactive.
				return false;
			}
			if (state == State.WAITING_FOR_CHILDREN) {
				currentMessage.offer(Optional.empty()); // Stays inactive.
				copy = new ArrayList<>(children);
			}
		}

		if (copy != null) {
			for (InteractionThread t : copy) {
				if (t.testActual(actual)) {
					return true;
				}
			}
		}

		/*
		 * Check if state was or became ACTIVE while traversing the children. It
		 * is possible that it was already ACTIVE because when becoming ACTIVE
		 * after WAITING_FOR_CHILDREN, the empty optional is not automatically
		 * removed from the currentMessage queue.
		 */
		synchronized (children) {
			if (state != State.ACTIVE) {
				return false;
			}
		}
		return testActual(actual);
		/*
		 * Recursion is ok here: We may only reach this point once for every
		 * time testActual() is called from the model executor thread. This is
		 * because the currentMessage queue never contain more than one empty
		 * Optional.
		 */
	}

	/**
	 * Use {@link #kill()} to kill this thread; otherwise, this thread will wait
	 * indefinitely.
	 */
	@Override
	public void wake() {
		/*
		 * Nothing to do.
		 * 
		 * On the currentMessage queue only the model executor thread may wait.
		 * However, if wake() is called that means that all interactions are
		 * currently offering a message and therefore the model executor thread
		 * must awake on one of them.
		 * 
		 * If this thread waits on the result queue, that is ok. If the model
		 * thread terminates, that will call kill() for all interactions.
		 * 
		 * If this thread waits for its children, that is ok because (again) the
		 * kill() method will be called on them as well if needed.
		 */
	}

	/**
	 * Use this method to kill this thread and its children; otherwise, this
	 * thread will wait indefinitely.
	 * <p>
	 * Called from the model executor thread.
	 */
	public void kill() {
		List<InteractionThread> copy;
		synchronized (children) {
			boolean active = state == State.ACTIVE;
			state = State.KILLED;
			if (active) {
				currentMessage.offer(Optional.empty()); // Becomes inactive.
			}
			copy = new ArrayList<>(children);
			children.clear();
		}
		copy.forEach(InteractionThread::kill);
		result.offer(Result.WAKEN_BY_KILL);
		// Here, this thread is waken but we do not have to call
		// awaitThisWaiting because it reached a terminal state.
	}

	/**
	 * Waits until this thread reaches a point when it finishes all its current
	 * activities and starts waiting for some other threads to act or reaches a
	 * terminal state.
	 * <p>
	 * Called from the model executor thread or from parent.
	 */
	public void awaitThisWaiting() {
		/*
		 * We wait for the next message to arrive which marks that the
		 * interaction thread has processed the next block of user code or
		 * reached a terminal state. Naturally, we have to put back the message
		 * to not loose any information.
		 */
		currentMessage.offer(takeUninterruptibly(currentMessage));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.elte.txtuml.api.model.impl.Wrapper#getWrapped()
	 */
	@Override
	public Interaction getWrapped() {
		return wrapped;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.elte.txtuml.api.model.impl.SeqDiagThread#getCurrentInteraction()
	 */
	@Override
	public InteractionRuntime getCurrentInteraction() {
		return this;
	}

}
