package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import static com.google.common.util.concurrent.Uninterruptibles.joinUninterruptibly;
import static com.google.common.util.concurrent.Uninterruptibles.takeUninterruptibly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

/**
 * @see RootInteractionThread
 * @see ChildInteractionThread
 */
@SequenceDiagramRelated
public class InteractionThread extends AbstractModelExecutor.OwnedThread<DefaultSeqDiagExecutor>
		implements InteractionRuntime, SeqDiagThread {

	/*
	 * This class uses the following:
	 * 
	 * 1. Blocks the model executor from termination.
	 * 
	 * 2. Runs user code.
	 * 
	 * 3. When user code arrives to a message, this thread (after sending the
	 * signal in case of a message from the actor) releases the termination
	 * blocker and waits for the message to be consumed. If all threads are
	 * waiting, the model executor may terminate as no one else can send in
	 * messages. (If the model executor thread's mailbox is also empty, of
	 * course.)
	 * 
	 * 4. When the model executor thread finds a message, it asks the root
	 * thread to test the message. The root thread is either
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
	 * blocker is added again before the model executor thread could reach its
	 * loop condition and possibly terminate.
	 * 
	 * 6. This thread returns back to the user code (step 2).
	 * 
	 * 7. When the user code is completed, this thread simply terminates. (After
	 * clearing everything up and properly unregistering itself, of course.)
	 * 
	 * For the case of par fragments, see the appropriate method.
	 * 
	 * The interaction threads do not terminate by themselves when they have
	 * 'nothing to do' as they always have something to do until they reach the
	 * end of the user code. Therefore they have to be killed by the kill()
	 * method if an early-stop is required. This is done by the model executor
	 * thread before that terminates.
	 */

	// fields

	private static final AtomicLong count = new AtomicLong();

	private final Interaction wrapped;

	/**
	 * The presence of this termination blocker shows that this thread is
	 * currently active and may send further signals to the model.
	 * 
	 * @see AbstractModelExecutor#addTerminationBlocker(Object)
	 */
	private final Object terminationBlocker = new Object();

	/**
	 * Another thread may wait on this queue when this thread is currently
	 * working to gain it from the user code. Contains an empty Optional when
	 * this thread is inactive. It is to ensure that the model executor may
	 * never wait on this thread when it is inactive.
	 */
	private final BlockingQueue<Optional<Message>> currentMessage = new LinkedBlockingQueue<>();

	/**
	 * This is the result when waiting for the current message to be consumed.
	 */
	private final BlockingQueue<Result> result = new LinkedBlockingQueue<>();

	/**
	 * May only be accessed when holding the monitor of this list.
	 */
	private final List<InteractionThread> children = new ArrayList<>();

	private final InteractionThread parent;

	/**
	 * May only be accessed when holding the monitor of {@link #children}.
	 */
	private State state = State.ACTIVE;

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
		executor.addTerminationBlocker(terminationBlocker);
	}

	@Override
	public void doRun() {
		try {
			getWrapped().run();
		} catch (Kill e) {
			// Nothing to do. Thread terminates.
		}
		synchronized (children) {
			state = State.TERMINATED;
		}
		getExecutor().removeTerminationBlocker(terminationBlocker);
		// No longer required to block termination.

		currentMessage.offer(Optional.empty()); // Becomes inactive.

		if (parent != null) {
			parent.unregister(this);
		}

		/*
		 * This thread may safely terminate at this point. It certainly has
		 * nothing more user code to consume. (Unlike model executor threads
		 * which may take work based from asynchronous sends.)
		 */
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

	@Override
	public void par(Interaction[] operands) {
		List<InteractionThread> copy; // Copying to release lock.
		synchronized (children) {
			if (state == State.KILLED) {
				return;
			}
			state = State.WAITING_FOR_CHILDREN;
			for (Interaction interaction : operands) {
				children.add(new InteractionThread(getExecutor(), this, interaction));
			}
			copy = new ArrayList<>(children);
		}
		copy.forEach(InteractionThread::start);
		currentMessage.offer(Optional.empty()); // Becomes inactive.
		copy.forEach(t -> joinUninterruptibly(t));
	}

	private void setExpected(Message message) {
		getExecutor().removeTerminationBlocker(terminationBlocker);
		// It is important that the termination blocker here is released after
		// sending the signal in case of a message from actor. Otherwise the
		// model executor thread could terminate before this signal arrives.

		currentMessage.offer(Optional.of(message));

		Result r = takeUninterruptibly(result);

		if (r == Result.WAKEN_BY_KILL) {
			getExecutor().addError(new PatternNotMetError(message));
			throw new Kill();
		}
		// Here: r == Result.MESSAGE_CONSUMED

		// terminationBlocker is already added again, nothing more to do
	}

	/**
	 * Called from the model executor thread.
	 */
	public boolean testActual(Message actual) {
		Optional<Message> expected = takeUninterruptibly(currentMessage);

		if (expected.isPresent()) {
			if (actual.matches(expected.get())) {
				getExecutor().addTerminationBlocker(terminationBlocker);
				result.offer(Result.MESSAGE_CONSUMED);
				// This thread is going back to work.
				// The termination blocker has to be added here. Otherwise the
				// model
				// executor thread (which is executing this method) may reach
				// its loop
				// condition and exit before the termination blocker could be
				// added.
				return true;
			} else {
				currentMessage.offer(expected);
				// Put it back to check again in the future.
				return false;
			}
		}

		// message is not present
		List<InteractionThread> copy = null;
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

		// If state was or became ACTIVE while traversing the children.
		// It is possible that it was already ACTIVE because when becoming
		// ACTIVE after WAITING_FOR_CHILDREN, the empty optional is not
		// automatically removed from the currentMessage queue.
		synchronized (children) {
			if (state != State.ACTIVE) {
				return false;
			}
		}
		return testActual(actual);
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
		 * must awake.
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
		synchronized (children) {
			state = State.KILLED;
			children.forEach(InteractionThread::kill);
			children.clear();
		}
		result.offer(Result.WAKEN_BY_KILL);
	}

	@Override
	public Interaction getWrapped() {
		return wrapped;
	}

	@Override
	public InteractionRuntime getCurrentInteraction() {
		return this;
	}

	protected void unregister(InteractionThread child) {
		synchronized (children) {
			children.remove(child);
			if (children.isEmpty()) {
				state = State.ACTIVE;
			}
		}
	}

}
