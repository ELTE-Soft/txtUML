package hu.elte.txtuml.diagnostics.session;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsNull.*;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.junit.Test;

import hu.elte.txtuml.diagnostics.PluginLogWrapper;

public class UniqueInstanceTest {
	
	private static final int serviceInstanceID = 123;
	private static final String classInstanceID = "obj_1234567";
	private static final String className = "std.Kutya";

	class LogCounter implements ILogListener {
		int counter = 0;

		@Override
		public void logging(IStatus status, String plugin) {
			counter++;
		}
	}
	
	@Test
	public void instanceCreated() {
		UniqueInstance inst = new UniqueInstance(classInstanceID, serviceInstanceID);
		assertThat(inst.classInstanceID, is(classInstanceID));
		assertThat(inst.serviceInstanceID, is(serviceInstanceID));
	}

	@Test
	public void modelClassNameQueried() {
		UniqueInstance inst = new UniqueInstance(classInstanceID, serviceInstanceID);
		assertThat(inst.getModelClassName(), nullValue());
		inst.setModelClassName(className);
		assertThat(inst.getModelClassName(), is(className));
	}

	@Test
	public void replacingModelClassNameWritesToLog() {
		UniqueInstance inst = new UniqueInstance(classInstanceID, serviceInstanceID);
		LogCounter logCount = new LogCounter();
		PluginLogWrapper.getInstance().addLogListener(logCount);
		assertThat(logCount.counter, is(0));
		inst.setModelClassName(className);
		assertThat(logCount.counter, is(0));
		inst.setModelClassName(className);
		assertThat(logCount.counter, is(0));
		inst.setModelClassName("butasag");
		assertThat(logCount.counter, is(1));
		PluginLogWrapper.getInstance().removeLogListener(logCount);
	}

	@Test
	public void hashCodeWorks() {
		UniqueInstance inst1 = new UniqueInstance(classInstanceID, serviceInstanceID);
		UniqueInstance inst2 = new UniqueInstance(classInstanceID, serviceInstanceID);
		UniqueInstance inst3 = new UniqueInstance(classInstanceID + ".kutya", serviceInstanceID);
		assertThat(inst1.hashCode(), is(inst1.hashCode()));
		assertThat(inst1.hashCode(), is(inst2.hashCode()));
		assertThat(inst1.hashCode(), not(inst3.hashCode()));
	}

	@Test
	public void equalsObjectWorks() {
		UniqueInstance inst1 = new UniqueInstance(classInstanceID, serviceInstanceID);
		UniqueInstance inst2 = new UniqueInstance(classInstanceID, serviceInstanceID);
		UniqueInstance inst3 = new UniqueInstance(classInstanceID + ".kutya", serviceInstanceID);
		assertThat(inst1, is(inst2));
		assertThat(inst1, not(inst3));
	}

	@Test
	public void testToString() {
		UniqueInstance inst1 = new UniqueInstance(classInstanceID, serviceInstanceID);
		UniqueInstance inst2 = new UniqueInstance(classInstanceID, serviceInstanceID);
		UniqueInstance inst3 = new UniqueInstance(classInstanceID + ".kutya", serviceInstanceID);
		assertThat(inst1.toString(), is(inst2.toString()));
		assertThat(inst1.toString(), not(inst3.toString()));
	}

}
