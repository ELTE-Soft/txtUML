package hu.elte.txtuml.api.model.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.external.BaseModelExecutor;
import hu.elte.txtuml.utils.Logger;

/**
 * This interface provides the simplest API to execute txtUML models. Its sole
 * abstract method is used as the {@linkplain #initialization} of the model
 * execution. That is, it contains the model code which creates, links and
 * starts the model class instances that should exist at that beginning of the
 * execution.
 * <p>
 * After the {@linkplain #initialization} method is implemented, there are three
 * possible ways to run the execution:
 * 
 * <ul>
 * <li>Instantiate the implementer class and call its {@link #run()}
 * method.</li>
 * <li>Call the static {@link #run(Class)} method with the implementer class as
 * its parameter.</li>
 * <li>Let this interface be the main type of a Java program and provide the
 * fully qualified name of the implementor class as a command line
 * argument.</li>
 * </ul>
 * 
 * For a bit more complicated cases, other methods of this interface might be
 * overridden as well. The {@link #before()}, {@link #during()} and
 * {@link #after()} are executed before, in parallel with, and after the model
 * execution, respectively. The {@link #name()} method can be used to give a
 * name to the execution which will then appear in logs. The
 * {@link #configure(Settings)} method enables the user of this interface to
 * alter the basic settings of this execution. See the documentation of the
 * {@link Settings} class for details about these settings.
 * <p>
 * For even more sophisticated cases, the {@link #start()} method can be called
 * instead of {@link #run()} which lets the execution run indefinitely but
 * returns a special {@link Handle} object which provides access to the (base)
 * model executor that runs the execution (for external libraries to use) and
 * also a method which initiates and awaits the termination of the execution.
 * <p>
 * Note that the {@link run()} and {@link start()} methods of this interface are
 * <b>not</b> intended to be overridden.
 * <p>
 * If the features provided by this interface are not satisfactory, please use
 * the more detailed and advanced {@link ModelExecutor} interface (which is used
 * by the {@linkplain #run} and {@linkplain #start} methods of this interface as
 * well).
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@FunctionalInterface
public interface Execution extends Runnable {

	/**
	 * Provides a name for the execution which will appear in logs. Returning
	 * {@code null} will result in an unnamed execution.
	 * <p>
	 * Does <b>not</b> contain model code.
	 * <p>
	 * Default implementation returns {@code null}.
	 * 
	 * @return the name of the execution or {@code null} for an unnamed
	 *         execution
	 */
	default String name() {
		return null;
	}

	/**
	 * Code that runs before the model starts.
	 * <p>
	 * Does <b>not</b> contain model code.
	 * <p>
	 * Default implementation is empty.
	 */
	default void before() {
	}

	/**
	 * Enables the users to specify the basic settings of the model execution.
	 * The fields of the received parameter contains the default settings of a
	 * model execution and can be changed as desired. After this method returns,
	 * the settings cannot be changed anymore, that is, any further modification
	 * to the object received as the parameter of this method will have no
	 * effect on the model execution.
	 * <p>
	 * <i>Note:</i> runs after {@link #before} but before the model execution is
	 * started.
	 * <p>
	 * Does <b>not</b> contain model code.
	 * <p>
	 * Default implementation is empty.
	 */
	default void configure(Settings s) {
	}

	/**
	 * The initialization of the model that creates all required model class
	 * instances, link and start them.
	 * <p>
	 * <b>Contains model code</b> so only actions that are allowed inside a
	 * txtUML model can be used inside this method.
	 */
	void initialization();

	/**
	 * Code that runs in parallel with the model (but after its initialization
	 * has been completed).
	 * <p>
	 * Does <b>not</b> contain model code. Use methods of the
	 * {@link hu.elte.txtuml.api.model.API} class to communicate with the model.
	 * <p>
	 * Default implementation is empty.
	 */
	default void during() {
	}

	/**
	 * Code that runs after the model has been terminated.
	 * <p>
	 * Does <b>not</b> contain model code.
	 * <p>
	 * Default implementation is empty.
	 */
	default void after() {
	}

	/**
	 * Runs this execution using the information provided by the other methods
	 * of this class; not intended to be overridden.
	 * 
	 * <h2>Details</h2>
	 * 
	 * This method:
	 * <ol>
	 * <li>Calls the {@link #before} method.</li>
	 * <li>Calls the {@link #settings} method to configure the model
	 * execution.</li>
	 * <li>Starts the model execution with the {@link #initialization} method as
	 * its initialization on a different thread, and awaits the initialization
	 * to complete.</li>
	 * <li>Calls the {@link #during} method.</li>
	 * <li>Initiates and awaits the termination of the model execution.</li>
	 * <li>Calls the {@link #after} method.</li>
	 * </ol>
	 * <p>
	 * <i>Implementation note:</i> Calls {@link #start} and then the
	 * {@link Handle#shutdownAndAwait() shutdownAndAwait} method of its result.
	 */
	@Override
	default void run() {
		start().shutdownAndAwait();
	}

	/**
	 * Starts (but not terminates) this execution using the information provided
	 * by the other methods of this class; not intended to be overridden.
	 * <p>
	 * After starting the execution and calling {@link #during()}, this method
	 * returns and lets the model execution to run indefinitely. The returned
	 * {@link Handle} object can be used, however, to initiate and await the
	 * termination of the execution (and call {@link #after()}) and it also
	 * provides access to the (base) model executor that manages this execution.
	 * The latter is useful if the executor is required by some external
	 * libraries.
	 * 
	 * <h2>Details</h2>
	 * 
	 * This method:
	 * <ol>
	 * <li>Calls the {@link #before} method.</li>
	 * <li>Calls the {@link #configure} method to configure the model
	 * execution.</li>
	 * <li>Starts the model execution with the {@link #initialization} method as
	 * its initialization on a different thread, and awaits the initialization
	 * to complete.</li>
	 * <li>Calls the {@link #during} method.</li>
	 * </ol>
	 * 
	 * The {@link Handle#shutdownAndAwait() shutdownAndAwait()} method of the
	 * returned object:
	 * 
	 * <ol>
	 * <li>Initiates and awaits the termination of the model execution.</li>
	 * <li>Calls the {@link #after} method.</li>
	 * </ol>
	 * <p>
	 * <i>Note:</i> This method uses a default {@link ModelExecutor}.
	 * 
	 * @return a special {@link Handle} that enables the caller to gain access
	 *         to the (base) model executor managing this execution, and to
	 *         terminate the execution (which also calls the {@link #after}
	 *         method)
	 */
	default Handle start() {
		ModelExecutor executor;

		// create
		String name = name();
		if (name == null) {
			executor = ModelExecutor.create();
		} else {
			executor = ModelExecutor.create(name);
		}

		// prepare
		before();
		executor.set(this::configure);

		// start
		executor.start(this::initialization);
		during();

		// return handle to terminate
		return new Handle() {
			@Override
			public void shutdownAndAwait() {
				executor.shutdown().awaitTermination();
				after();
			}

			@Override
			public BaseModelExecutor get() {
				return executor;
			}
		};
	}

	/**
	 * A model execution handle. Call its {@link #shutdownAndAwait} method to
	 * initiate and await the termination of the execution its instance was
	 * created for. The (base) model executor in charge of the execution can
	 * also be accessed for external libraries to use.
	 */
	interface Handle extends Supplier<BaseModelExecutor> {

		/**
		 * Initiates and awaits the termination of the model execution this
		 * handle was created for; also calls the appropriate {@link #after}
		 * method.
		 */
		void shutdownAndAwait();

		/**
		 * Returns the (base) model executor that manages this model execution.
		 * Useful if the model executor is required by external libraries.
		 * 
		 * @return the (base) model executor that manages this model execution
		 */
		@Override
		BaseModelExecutor get();

	}

	/**
	 * The public fields of this class describe the basic settings of a model
	 * execution.
	 */
	public class Settings implements Cloneable {

		/**
		 * Determines which level of model execution logs should be shown.
		 * <p>
		 * Log level is {@link LogLevel#WARNING} by default.
		 */
		public LogLevel logLevel = LogLevel.WARNING;

		/**
		 * Determines which level of dynamic checks should be performed during
		 * the model execution.
		 * <p>
		 * Check level is {@link CheckLevel#OPTIONAL} by default.
		 */
		public CheckLevel checkLevel = CheckLevel.OPTIONAL;

		/**
		 * The model execution time helps testing txtUML models in the following
		 * way: when any time-related event inside the model is set to take
		 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
		 * <i>mul</i> milliseconds during model execution, where <i>mul</i> is
		 * the current execution time multiplier. This way, txtUML models might
		 * be tested at the desired speed.
		 * <p>
		 * Execution time multiplier is 1 by default.
		 */
		public double timeMultiplier = 1.0;

		@Override
		public Settings clone() {
			try {
				return (Settings) super.clone();
			} catch (CloneNotSupportedException e) {
				// Cannot happen.
				return null;
			}
		}
	}

	/**
	 * Receives an implementer class of {@link Execution} and runs it.
	 * <p>
	 * After instantiating the given class, its {@link run()} method is called.
	 * 
	 * @param cls
	 *            the execution type to run
	 * @throws IllegalArgumentException
	 *             if the given type is not an execution or cannot be
	 *             instantiated with a zero parameter constructor
	 */
	public static void run(Class<?> cls) throws IllegalArgumentException {
		if (!Execution.class.isAssignableFrom(cls)) {
			throw new IllegalArgumentException("The given type (" + cls.getName() + ") is not a subtype of Execution.");
		}

		@SuppressWarnings("unchecked")
		Class<Execution> type = (Class<Execution>) cls;

		if (cls.isInterface()) {
			throw new IllegalArgumentException("The given type (" + cls.getName() + ") is an interface.");
		}

		Constructor<Execution> constructor;
		try {
			constructor = type.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					"The given type (" + cls.getName() + ") does not have a 0 parameter constructor.");
		}

		Execution exec;
		try {
			exec = constructor.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("The given type (" + cls.getName() + ") is abstract.");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(
					"The zero parameter constructor of the given type (" + cls.getName() + ") cannot be accessed.");
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(
					"The zero parameter constructor of the given type (" + cls.getName() + ") threw an exception.",
					e.getCause());
		} catch (IllegalArgumentException e) {
			// Cannot happen. (We are calling a zero parameter constructor.)
			return;
		}

		exec.run();
	}

	/**
	 * Receives the fully qualified name of a class that implements
	 * {@link Execution} as first command line argument and runs it.
	 * <p>
	 * After instantiating the given class, its {@link run()} method is called.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			Logger.sys.error(
					"Execution main: One command line argument is required which is the execution class to be run.");
			return;
		}
		if (args.length > 1) {
			Logger.sys.warn("Execution main: Only the first command line argument is used.");
		}

		Class<?> cls;
		try {
			cls = Class.forName(args[0]);
		} catch (ClassNotFoundException e) {
			Logger.sys.error("Execution main: The given type (" + args[0] + ") cannot be loaded.");
			return;
		}

		try {
			run(cls);
		} catch (IllegalArgumentException e) {
			Throwable cause = e.getCause();
			if (cause == null) {
				Logger.sys.error("Execution main: " + e.getMessage());
			} else {
				Logger.sys.error("Execution main: " + e.getMessage(), cause);
			}
		}
	}

}
