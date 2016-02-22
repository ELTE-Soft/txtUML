package hu.elte.txtuml.utils;

import java.util.function.Function;

/**
 * Methods of this interface should be used with extreme care.
 */
public interface Sneaky {

	/**
	 * Useful in cases when a checked exception is thrown in a sneaky way and it
	 * has to be caught in a surrounding catch block (which would cause compile
	 * error without this method as the compiler does not see the checked
	 * exception to be thrown).
	 */
	static <E extends Exception> void Throw() throws E {
	}

	static <E extends Exception> void uncheck(ExceptionThrowingRunnable<E> r) {
		try {
			r.run();
		} catch (Exception e) {
			sneakyThrow(e);
		}
	}

	static <T, R, E extends Exception> R uncheck(
			ExceptionThrowingFunction<T, R, E> f, T param) {
		try {
			return f.apply(param);
		} catch (Exception e) {
			return sneakyThrow(e);
		}
	}

	static <E extends Exception> Runnable unchecked(
			ExceptionThrowingRunnable<E> r) {
		return () -> uncheck(r);
	}

	static <T, R, E extends Exception> Function<T, R> unchecked(
			ExceptionThrowingFunction<T, R, E> f) {
		return t -> uncheck(f, t);
	}

	static <T> T sneakyThrow(Throwable e) {
		return SneakyThrow.<RuntimeException, T> sneakyThrow0(e);
	}

	@FunctionalInterface
	interface ExceptionThrowingRunnable<E extends Exception> {
		void run() throws E;
	}

	@FunctionalInterface
	interface ExceptionThrowingFunction<T, R, E extends Exception> {
		R apply(T t) throws E;
	}

}

final class SneakyThrow {
	@SuppressWarnings("unchecked")
	static <E extends Throwable, T> T sneakyThrow0(Throwable t) throws E {
		throw (E) t;
	}

	private SneakyThrow() {
	}
}
