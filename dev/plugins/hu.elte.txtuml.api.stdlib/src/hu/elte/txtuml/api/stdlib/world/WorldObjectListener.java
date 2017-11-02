package hu.elte.txtuml.api.stdlib.world;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.runtime.BaseModelExecutor;
import hu.elte.txtuml.utils.Logger;

/**
 * A world object listener can be registered to listen for signals sent to a
 * specific world object in a model execution. A subclass should create specific
 * signal handler methods that accept certain kind of signals and call the
 * {@link register} method to start listening.
 * <p>
 * <b>Signal handler methods</b>
 * <ul>
 * <li>must have the {@link SignalHandler annotation},</li>
 * <li>must be {@code public}</li>
 * <li>must have exactly one formal parameter the type of which is a subclass of
 * {@link SignalToWorld},</li>
 * <li>may have an arbitrary name.</li>
 * </ul>
 * <p>
 * If multiple signal handler methods can be applied for a signal, then the one
 * with the most specific parameter type will be invoked. If more handlers exist
 * with the same parameter, then an unspecified one will be chosen. If no
 * handlers can be applied, the {@link #notHandledSignal} method is called which
 * shows a warning message by default.
 * <p>
 * <i>Note:</i> Instead of extending this class, create a subclass of its
 * predefined subclasses in the <i>txtUML World</i> model.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world} package
 * or further details and examples about the services provided by the <i>txtUML
 * World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 * 
 * @see AbstractWorldObjectListener
 * @see SwingWorldObjectListener
 * @see QueueWorldObjectListener
 * @see ImmediateWorldObjectListener
 */
@External
public abstract class WorldObjectListener {

	/**
	 * Instead of extending {@link WorldObjectListener}, create a subclass of
	 * its predefined subclasses.
	 * 
	 * @see AbstractWorldObjectListener
	 * @see SwingWorldObjectListener
	 * @see QueueWorldObjectListener
	 * @see ImmediateWorldObjectListener
	 */
	WorldObjectListener() {
	}

	/**
	 * Called by the world object this listener is registered for when a new
	 * signal arrives to that object.
	 * <p>
	 * Naturally, this is called on the model executor thread.
	 * <p>
	 * <i>Note:</i> this method is <i>not</i> called "accept" to avoid a warning
	 * that would be shown if the user also defines an "accept" method with a
	 * {@link SignalToWorld} parameter.
	 */
	abstract void acceptAny(SignalToWorld signal);

	/**
	 * Finds and invokes the signal handler method that satisfies the rules
	 * described in the documentation of {@link WorldObjectListener}. If no
	 * applicable signal handler methods can be found, calls
	 * {@link #notHandledSignal}.
	 */
	protected final void invokeHandler(SignalToWorld signal) {
		Method toBeInvoked = null;
		Class<?> typeOfToBeInvoked = null;
		for (Method m : getClass().getMethods()) {
			if (!m.isAnnotationPresent(SignalHandler.class)) {
				continue;
			}

			Class<?>[] parameterTypes = m.getParameterTypes();
			if (parameterTypes.length != 1) {
				Logger.executor.error("Number of parameters of the signal handler method " + m + " is not 1.");
				continue;
			}

			Class<?> type = parameterTypes[0];

			if (!type.isInstance(signal)) {
				continue;
			}

			if (toBeInvoked == null || typeOfToBeInvoked.isAssignableFrom(type)) {
				toBeInvoked = m;
				typeOfToBeInvoked = type;
			}
		}
		if (toBeInvoked == null) {
			notHandledSignal(signal);
			return;
		}

		try {

			try {
				toBeInvoked.invoke(this, signal);
			} catch (IllegalAccessException e) {
				// set accessible and try again
				toBeInvoked.setAccessible(true);
				try {
					toBeInvoked.invoke(this, signal);
				} catch (IllegalAccessException e2) {
					// nothing more to do
					Logger.executor.fatal("Illegal access", e);
				}
			}

		} catch (InvocationTargetException e) {
			Logger.executor.error("Exception in world object listener " + toString()
					+ " while calling signal handler method " + toBeInvoked + ".", e.getCause());
		}
	}

	/**
	 * Called by {@link invokeHandler} if no signal handler methods could be
	 * applied.
	 * <p>
	 * Logs a warning message by default.
	 */
	public void notHandledSignal(SignalToWorld signal) {
		Logger.executor.warn("A signal arrived to the world object listener " + this
				+ " but it did not handle it. The signal was: " + signal);
	}

	/**
	 * Registers this listener to receive signals sent to the world object that
	 * belongs to the given model executor and has the specified identifier.
	 * Only one listener can be registered for a world object at any given time,
	 * that is, if any other listener has previously registered for the same
	 * world object in the same model execution, this method overwrites that
	 * registration.
	 * <p>
	 * Note that the registration is valid and completed even if the specific
	 * world object does not yet exist. In this case, the world object will be
	 * connected automatically to this listener whenever that is created.
	 */
	public void register(BaseModelExecutor forExecutor, String identifier) {
		WorldHelper.getOrCreateHelperInstance(forExecutor).getOrCreateObjectHelper(identifier).setListener(this);
	}

	/**
	 * If any listener is registered for the world object that belongs to the
	 * given model executor and has the specified identifier, this method
	 * deletes that registration. Does nothing otherwise.
	 */
	public static void unregister(BaseModelExecutor forExecutor, String identifier) {
		WorldHelper.getOrCreateHelperInstance(forExecutor).getOrCreateObjectHelper(identifier).setListener(null);
	}

	@Override
	public String toString() {
		return "wol:" + getClass().getSimpleName();
	}

	/**
	 * Marker annotation for signal handler methods. Such methods also have to
	 * be {@code public} and they must have exactly one formal parameter the
	 * type of which is a subclass of {@link SignalToWorld}.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Documented
	protected @interface SignalHandler {
	}

}
