package hu.elte.txtuml.api.model.execution.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Assert;

import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;

public class ModelExecutionAsserter {

	private static final Consumer<ErrorListener> EMPTY_ERROR_CONSUMER = x -> {
	};

	private static final Consumer<ErrorListener> EMPTY_WARNING_CONSUMER = x -> {
	};

	private final List<ListenerInvocation> events = new ArrayList<>();
	private final List<ListenerInvocation> errors = new ArrayList<>();
	private final List<ListenerInvocation> warnings = new ArrayList<>();

	public void configureFor(ModelExecutor executor) {
		events.clear();
		errors.clear();
		warnings.clear();
		executor.addTraceListener(createListener(TraceListener.class, events));
		executor.addErrorListener(createListener(ErrorListener.class, errors));
		executor.addWarningListener(createListener(WarningListener.class, warnings));
	}
	
	@SuppressWarnings("unchecked")
	private static <L> L createListener(Class<L> listenerClass, List<ListenerInvocation> list) {
		InvocationHandler handler = (proxy, method, params) -> {
			list.add(new ListenerInvocation(method, params));
			return null;
		};
		return (L) Proxy.newProxyInstance(listenerClass.getClassLoader(), new Class[] { listenerClass }, handler);
	}

	public void assertEvents(Consumer<TraceListener> generator) {
		privateAssert(TraceListener.class, generator, events);
	}

	public void assertErrors(Consumer<ErrorListener> generator) {
		privateAssert(ErrorListener.class, generator, errors);
	}

	public void assertWarnings(Consumer<WarningListener> generator) {
		privateAssert(WarningListener.class, generator, warnings);
	}

	public void assertNoErrors() {
		privateAssert(ErrorListener.class, EMPTY_ERROR_CONSUMER, errors);
	}

	public void assertNoWarnings() {
		privateAssert(ErrorListener.class, EMPTY_WARNING_CONSUMER, warnings);
	}

	private static <L> void privateAssert(Class<L> listenerClass, Consumer<L> generator,
			List<ListenerInvocation> actuals) {
		List<ListenerInvocation> expecteds = new ArrayList<>();
		generator.accept(createListener(listenerClass, expecteds));

		Assert.assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	public boolean hasEvents(Consumer<TraceListener> generator) {
		return privateCheck(TraceListener.class, generator, events);
	}

	public boolean hasErrors(Consumer<ErrorListener> generator) {
		return privateCheck(ErrorListener.class, generator, errors);
	}

	public boolean hasWarnings(Consumer<WarningListener> generator) {
		return privateCheck(WarningListener.class, generator, warnings);
	}

	private static <L> boolean privateCheck(Class<L> listenerClass, Consumer<L> generator,
			List<ListenerInvocation> actuals) {
		List<ListenerInvocation> expecteds = new ArrayList<>();
		generator.accept(createListener(listenerClass, expecteds));

		return Arrays.equals(expecteds.toArray(), actuals.toArray());
	}

	private static class ListenerInvocation {
		final Method method;
		final Object[] params;

		ListenerInvocation(Method method, Object[] params) {
			this.method = method;
			this.params = params;
		}

		@Override
		public int hashCode() {
			return 0; // never used
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || !(obj instanceof ListenerInvocation)) {
				return false;
			}
			ListenerInvocation other = (ListenerInvocation) obj;
			if (method != other.method) {
				return false;
			}
			if (params == null) {
				if (other.params != null) {
					return false;
				}
				return true;
			}
			if (params.length != other.params.length) {
				return false;
			}
			for (int i = 0; i < params.length; ++i) {
				if (params[i] != other.params[i]
						&& !String.valueOf(params[i]).equals(String.valueOf(other.params[i]))) {
					if (params[i] instanceof Object[] && other.params[i] instanceof Object[] && Arrays
							.toString((Object[]) params[i]).equals(Arrays.toString((Object[]) other.params[i]))) {
						continue;
					}
					return false;
				}
			}
			return true;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(method.getName() + "(");
			if (params != null && params.length > 0) {
				builder.append(params[0]);
				for (int i = 1; i < params.length; ++i) {
					builder.append(", " + params[i]);
				}
			}
			builder.append(")");
			return builder.toString();
		}

	}

}
