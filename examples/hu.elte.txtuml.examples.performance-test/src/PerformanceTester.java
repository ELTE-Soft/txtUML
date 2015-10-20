import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ObjectInputStream.GetField;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;

public class PerformanceTester {

	public static class NullOutputStream extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}

	public static void main(String[] args) {
		// ModelExecutor.Settings.setExecutorLog(true);
		
		long start = System.currentTimeMillis();
		
		ModelExecutor.Settings.setDynamicChecks(false);

		ModelExecutor.Settings.setExecutorErrorStream(new PrintStream(
				new NullOutputStream(), false));

		PerformanceTestModel_.Test m = Action
				.create(PerformanceTestModel_.Test.class);
		m.test();

		ModelExecutor.shutdown();
		
		System.out.println(System.currentTimeMillis() - start);
		
	}

}