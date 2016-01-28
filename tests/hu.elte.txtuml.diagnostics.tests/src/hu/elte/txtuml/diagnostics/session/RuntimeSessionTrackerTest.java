package hu.elte.txtuml.diagnostics.session;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.junit.Test;

public class RuntimeSessionTrackerTest {

	class FakePlugin implements IDisposable {
		int shutdownWasCalled = 0;

		@Override
		public void dispose() {
			shutdownWasCalled++;
		}		
	}
	
	class FakeProcess implements IProcess {

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return null;
		}

		@Override
		public boolean canTerminate() {
			return false;
		}

		@Override
		public boolean isTerminated() {
			return false;
		}

		@Override
		public void terminate() throws DebugException {
		}

		@Override
		public String getLabel() {
			return null;
		}

		@Override
		public ILaunch getLaunch() {
			return null;
		}

		@Override
		public IStreamsProxy getStreamsProxy() {
			return null;
		}

		@Override
		public void setAttribute(String key, String value) {
		}

		@Override
		public String getAttribute(String key) {
			return null;
		}

		@Override
		public int getExitValue() throws DebugException {
			return 0;
		}
	}
	
	@Test
	public void singleSessionIsTracked() {
		FakePlugin fp = new FakePlugin();
		RuntimeSessionTracker rst = new RuntimeSessionTracker(null, fp);
		assertThat("The session should be alive", fp.shutdownWasCalled, is(0));
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.CREATE)});
		assertThat("The session should be alive", fp.shutdownWasCalled, is(0));
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.TERMINATE)});
		assertThat("The session should be over", fp.shutdownWasCalled, is(1));
	}

	@Test
	public void multipleSessionsAreTracked() {
		FakePlugin fp = new FakePlugin();
		RuntimeSessionTracker rst = new RuntimeSessionTracker(null, fp);
		assertThat("The session should be alive", fp.shutdownWasCalled, is(0));
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.CREATE)});
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.CREATE)});
		assertThat("The session should be alive", fp.shutdownWasCalled, is(0));
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.TERMINATE)});
		assertThat("The session should be alive", fp.shutdownWasCalled, is(0));
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.CREATE)});
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.TERMINATE)});
		assertThat("The session should be alive", fp.shutdownWasCalled, is(0));
		rst.handleDebugEvents(new DebugEvent[] {new DebugEvent(new FakeProcess(), DebugEvent.TERMINATE)});
		assertThat("The session should be over", fp.shutdownWasCalled, is(1));
	}

}
