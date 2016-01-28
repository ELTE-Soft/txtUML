package hu.elte.txtuml.diagnostics;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Test;

public class PluginLogWrapperTest {
	
	static class MyLogListener implements ILogListener {
		int counter = 0;
		@Override
		public void logging(IStatus status, String plugin) {
			counter++;
		}	
	}
	
	@Test
	public void startsAndStops() {
		if (Activator.getDefault() != null) {
			assertThat(PluginLogWrapper.isPluginRunning(), is(true));
			PluginLogWrapper.getInstance();
			assertThat(PluginLogWrapper.isPluginRunning(), is(true));
		}
		else {
			PluginLogWrapper.pluginStopped();
			assertThat(PluginLogWrapper.isPluginRunning(), is(false));
			PluginLogWrapper.getInstance();
			assertThat(PluginLogWrapper.isPluginRunning(), is(true));
			PluginLogWrapper.pluginStopped();
			assertThat(PluginLogWrapper.isPluginRunning(), is(false));
		}
	}

	@Test
	public void logIsDispatched() {
		boolean wasRunning = PluginLogWrapper.isPluginRunning();
		PluginLogWrapper plw = PluginLogWrapper.getInstance();
		Status st = new Status(IStatus.WARNING, "pluginID", "Message");
		MyLogListener mll = new MyLogListener();
		plw.log(st);
		assertThat(mll.counter, is(0));
		plw.addLogListener(mll);
		plw.log(st);
		assertThat(mll.counter, is(1));
		plw.removeLogListener(mll);
		plw.log(st);
		assertThat(mll.counter, is(1));
		if (!wasRunning) {
			PluginLogWrapper.pluginStopped();
			assertThat(PluginLogWrapper.isPluginRunning(), is(false));
		}
	}

}
