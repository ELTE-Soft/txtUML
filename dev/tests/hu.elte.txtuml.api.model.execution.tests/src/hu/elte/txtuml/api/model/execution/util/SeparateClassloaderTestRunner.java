package hu.elte.txtuml.api.model.execution.util;

import java.net.URLClassLoader;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class SeparateClassloaderTestRunner extends BlockJUnit4ClassRunner {

	public SeparateClassloaderTestRunner(Class<?> cls)
			throws InitializationError {

		super(getFromTestClassloader(cls));
	}

	private static Class<?> getFromTestClassloader(Class<?> cls)
			throws InitializationError {
		try {
			ClassLoader testClassLoader = new TestClassLoader();
			return Class.forName(cls.getName(), true, testClassLoader);
		} catch (ClassNotFoundException e) {
			throw new InitializationError(e);
		}
	}

	public static class TestClassLoader extends URLClassLoader {
		public TestClassLoader() {
			super(((URLClassLoader) getSystemClassLoader()).getURLs());
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			if (name.startsWith("hu.elte.txtuml.api.")) {
				return super.findClass(name);
			}
			return super.loadClass(name);
		}
	}
}