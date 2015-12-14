package hu.elte.txtuml.diagnostics.session;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.api.diagnostics.protocol.InstanceEvent;
import hu.elte.txtuml.api.diagnostics.protocol.Message;
import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.utils.platform.PluginLogWrapper;

/**
 * @author gerazo
 */
public class InstanceRegisterTest {
	
	static class MyLogListener implements ILogListener {
		int counter = 0;
		@Override
		public void logging(IStatus status, String plugin) {
			counter++;
		}
	}
	
	MyLogListener mll;
	
	@Before
	public void setUp() {
		mll = new MyLogListener();
		PluginLogWrapper.getInstance().addLogListener(mll);
	}

	@After
	public void tearDown() {
		PluginLogWrapper.getInstance().removeLogListener(mll);
		mll = null;
	}

	@Test
	public void liveUniqueInstanceIsRetrievedWithoutProblem() {
		InstanceRegister ir = new InstanceRegister();
		assertThat(mll.counter, is(0));
		assertThat(ir.getInstance("obj11", 1), nullValue());
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		assertThat(mll.counter, is(0));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_CREATION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(0));
		assertThat(ir.getInstance("obj11", 1).classInstanceID, is("obj11"));
		assertThat(ir.getInstance("obj11", 1).serviceInstanceID, is(1));
		assertThat(ir.getInstance("obj11", 1).getModelClassName(), is("Kutya"));
		assertThat(mll.counter, is(0));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_DESTRUCTION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(0));
		ir.processMessage(new Message(MessageType.CHECKOUT, 1));
		assertThat(mll.counter, is(0));
		ir.dispose();
		assertThat(mll.counter, is(0));
	}

	@Test
	public void aliveServiceIsLoggedOnDispose() {
		InstanceRegister ir = new InstanceRegister();
		assertThat(mll.counter, is(0));
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		assertThat(mll.counter, is(0));
		ir.dispose();
		assertThat(mll.counter, is(1));
	}

	@Test
	public void aliveClassInstanceIsLoggedOnDispose() {
		InstanceRegister ir = new InstanceRegister();
		assertThat(mll.counter, is(0));
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_CREATION, 1, "Kutya", "obj11"));
		ir.processMessage(new Message(MessageType.CHECKOUT, 1));
		assertThat(mll.counter, is(0));
		ir.dispose();
		assertThat(mll.counter, is(1));
	}

	@Test
	public void messageFromUnregisteredServiceIsLogged() {
		InstanceRegister ir = new InstanceRegister();
		assertThat(mll.counter, is(0));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_CREATION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(1));
		ir.dispose();
	}

	@Test
	public void redundantCheckInIsLogged() {
		InstanceRegister ir = new InstanceRegister();
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		assertThat(mll.counter, is(0));
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		assertThat(mll.counter, is(1));
		ir.dispose();
	}

	@Test
	public void redundantCheckoutIsLogged() {
		InstanceRegister ir = new InstanceRegister();
		assertThat(mll.counter, is(0));
		ir.processMessage(new Message(MessageType.CHECKOUT, 1));
		assertThat(mll.counter, is(1));
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		assertThat(mll.counter, is(1));
		ir.processMessage(new Message(MessageType.CHECKOUT, 1));
		assertThat(mll.counter, is(1));
		ir.processMessage(new Message(MessageType.CHECKOUT, 1));
		assertThat(mll.counter, is(2));
		ir.dispose();
	}

	@Test
	public void redundantObjectCreationIsLogged() {
		InstanceRegister ir = new InstanceRegister();
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_CREATION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(0));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_CREATION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(1));
		ir.dispose();
	}

	@Test
	public void redundantObjectDestructionIsLogged() {
		InstanceRegister ir = new InstanceRegister();
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		assertThat(mll.counter, is(0));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_DESTRUCTION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(1));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_CREATION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(1));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_DESTRUCTION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(1));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_DESTRUCTION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(2));
		ir.dispose();
	}

	@Test
	public void eventFromUncreatedObjectIsLoggedOnce() {
		InstanceRegister ir = new InstanceRegister();
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		assertThat(mll.counter, is(0));
		ir.processMessage(new ModelEvent(MessageType.USING_TRANSITION, 1, "Kutya", "obj11", "CsontotKap"));
		assertThat(mll.counter, is(1));
		ir.processMessage(new ModelEvent(MessageType.USING_TRANSITION, 1, "Kutya", "obj11", "CsontotKap"));
		assertThat(mll.counter, is(1));
		ir.dispose();
	}

	@Test
	public void changedTypeOfObjectIsLogged() {
		InstanceRegister ir = new InstanceRegister();
		ir.processMessage(new Message(MessageType.CHECKIN, 1));
		ir.processMessage(new InstanceEvent(MessageType.INSTANCE_CREATION, 1, "Kutya", "obj11"));
		assertThat(mll.counter, is(0));
		ir.processMessage(new ModelEvent(MessageType.USING_TRANSITION, 1, "Kutya", "obj11", ""));
		assertThat(mll.counter, is(0));
		ir.processMessage(new ModelEvent(MessageType.USING_TRANSITION, 1, "Macska", "obj11", ""));
		assertThat(mll.counter, is(1));
		ir.dispose();
	}

}
