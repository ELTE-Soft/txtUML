package hu.elte.txtuml.utils;

import java.util.function.Function;

/**
 * Methods of this class should be used with extreme care; see the documentation
 * of {@link #sneakyThrow} for more information about the purpose of this type.
 */
public class Sneaky {

	/**
	 * Does nothing, only forces the compiler to think that the given Exception
	 * can be thrown at this point in the code.
	 * <p>
	 * Useful in cases when a checked exception is thrown in a sneaky way and it
	 * has to be caught in a surrounding catch block (which would cause compile
	 * error without this method as the compiler does not see the checked
	 * exception to be thrown).
	 */
	public static <E extends Exception> void Throw() throws E {
	}

	/**
	 * Runs the given action; if an exception is raised, this throws it in a
	 * {@link #sneakyThrow(Throwable) sneaky} way.
	 */
	public static <E extends Exception> void uncheck(ExceptionThrowingRunnable<E> r) {
		try {
			r.run();
		} catch (Exception e) {
			sneakyThrow(e);
		}
	}

	/**
	 * Runs the given action with the given parameter and returns the result; if
	 * an exception is raised, this throws it in a
	 * {@link #sneakyThrow(Throwable) sneaky} way.
	 */
	public static <T, R, E extends Exception> R uncheck(ExceptionThrowingFunction<T, R, E> f, T param) {
		try {
			return f.apply(param);
		} catch (Exception e) {
			return sneakyThrow(e);
		}
	}

	/**
	 * Returns a Runnable that, when executed, throws any raised exception in a
	 * {@link #sneakyThrow(Throwable) sneaky} way.
	 */
	public static <E extends Exception> Runnable unchecked(ExceptionThrowingRunnable<E> r) {
		return () -> uncheck(r);
	}

	/**
	 * Returns a Function that, when executed, throws any raised exception in a
	 * {@link #sneakyThrow(Throwable) sneaky} way.
	 */
	public static <T, R, E extends Exception> Function<T, R> unchecked(ExceptionThrowingFunction<T, R, E> f) {
		return t -> uncheck(f, t);
	}

	/**
	 * Throw the given throwable in a sneaky way, that is, by forcing the
	 * compiler to treat checked exceptions as they were unchecked. The compiler
	 * will be unaware of the fact that this exception is thrown here but it
	 * will still be thrown at runtime.
	 * 
	 * @throws T
	 *             always, but without the compiler realizing it
	 */
	public static <T> T sneakyThrow(Throwable e) {
		return Sneaky.<RuntimeException, T>sneakyThrow0(e);
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable, T> T sneakyThrow0(Throwable t) throws E {
		throw (E) t;
	}

	@FunctionalInterface
	public interface ExceptionThrowingRunnable<E extends Exception> {
		void run() throws E;
	}

	@FunctionalInterface
	public interface ExceptionThrowingFunction<T, R, E extends Exception> {
		R apply(T t) throws E;
	}

}
