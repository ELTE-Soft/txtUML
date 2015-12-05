package hu.elte.txtuml.api.model.util;

import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;
import hu.elte.txtuml.api.model.report.RuntimeErrorsListener;
import hu.elte.txtuml.api.model.report.RuntimeWarningsListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

public final class ModelExecutionAsserter {

	private final List<ListenerInvocation> events = new ArrayList<>();
	private final List<ListenerInvocation> errors = new ArrayList<>();
	private final List<ListenerInvocation> warnings = new ArrayList<>();

	public ModelExecutionAsserter() {
		ModelExecutor.Report.addModelExecutionEventsListener(createListener(
				ModelExecutionEventsListener.class, events));
		ModelExecutor.Report.addRuntimeErrorsListener(createListener(
				RuntimeErrorsListener.class, errors));
		ModelExecutor.Report.addRuntimeWarningsListener(createListener(
				RuntimeWarningsListener.class, warnings));

	}

	@SuppressWarnings("unchecked")
	private static <L> L createListener(Class<L> listenerClass,
			List<ListenerInvocation> list) {
		InvocationHandler handler = (proxy, method, params) -> {
			list.add(new ListenerInvocation(method, params));
			return null;
		};
		return (L) Proxy.newProxyInstance(listenerClass.getClassLoader(),
				new Class[] { listenerClass }, handler);
	}

	public void assertEvents(
			ExpectedsGenerator<ModelExecutionEventsListener> generator) {
		privateAssert(ModelExecutionEventsListener.class, generator, events);
	}

	public void assertErrors(ExpectedsGenerator<RuntimeErrorsListener> generator) {
		privateAssert(RuntimeErrorsListener.class, generator, errors);
	}

	public void assertWarnings(
			ExpectedsGenerator<RuntimeWarningsListener> generator) {
		privateAssert(RuntimeWarningsListener.class, generator, warnings);
	}

	private static <L> void privateAssert(Class<L> listenerClass,
			ExpectedsGenerator<L> generator, List<ListenerInvocation> actuals) {
		List<ListenerInvocation> expecteds = new ArrayList<>();
		generator.generate(createListener(listenerClass, expecteds));

		Assert.assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	public boolean checkEvents(
			ExpectedsGenerator<ModelExecutionEventsListener> generator) {
		return privateCheck(ModelExecutionEventsListener.class, generator,
				events);
	}

	public boolean checkErrors(
			ExpectedsGenerator<RuntimeErrorsListener> generator) {
		return privateCheck(RuntimeErrorsListener.class, generator, errors);
	}

	public boolean checkWarnings(
			ExpectedsGenerator<RuntimeWarningsListener> generator) {
		return privateCheck(RuntimeWarningsListener.class, generator, warnings);
	}

	private static <L> boolean privateCheck(Class<L> listenerClass,
			ExpectedsGenerator<L> generator, List<ListenerInvocation> actuals) {
		List<ListenerInvocation> expecteds = new ArrayList<>();
		generator.generate(createListener(listenerClass, expecteds));

		return Arrays.equals(expecteds.toArray(), actuals.toArray());
	}

	@FunctionalInterface
	public interface ExpectedsGenerator<L> {
		void generate(L listener);
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
						&& !String.valueOf(params[i]).equals(
								String.valueOf(other.params[i]))) {
					if (params[i] instanceof Object[]
							&& other.params[i] instanceof Object[]
							&& Arrays
									.toString((Object[]) params[i])
									.equals(Arrays
											.toString((Object[]) other.params[i]))) {
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
