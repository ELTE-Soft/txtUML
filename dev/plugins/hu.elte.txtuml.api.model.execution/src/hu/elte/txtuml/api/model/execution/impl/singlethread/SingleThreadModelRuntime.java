package hu.elte.txtuml.api.model.execution.impl.singlethread;

import java.util.List;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.execution.CheckLevel;
import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultipleContainerException;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultiplicityException;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractPortRuntime;

/**
 * Abstract base class for {@link Runtime} implementations which belong to model
 * executors that use only one model executor thread.
 */
public abstract class SingleThreadModelRuntime<C extends AbstractModelClassRuntime, P extends AbstractPortRuntime>
		extends AbstractModelRuntime<C, P> {

	/**
	 * Must be called on the thread which manages the given
	 * {@link AbstractModelExecutor} instance.
	 */
	protected SingleThreadModelRuntime(AbstractModelExecutor<?> modelExecutor) {
		super(modelExecutor);
	}

	protected SingleThreadModelRuntime(AbstractModelExecutor<?> modelExecutor, List<TraceListener> traceListeners,
			List<ErrorListener> errorListeners, List<WarningListener> warningListeners, Execution.Settings settings) {
		super(modelExecutor, traceListeners, errorListeners, warningListeners, settings);
	}

	@Override
	public <C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort) {
		P left = getRuntimeOf(leftPort);
		P right = getRuntimeOf(rightPort);

		left.setOuterConnection(right);
		right.setOuterConnection(left);
	}

	@Override
	public <P1 extends Port<I1, I2>, C0 extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C0> childEnd, P2 childPort) {
		P parent = getRuntimeOf(parentPort);
		P child = getRuntimeOf(childPort);

		if (parent.getInnerConnection() == null) {
			parent.setInnerConnection(child);
		} else {
			parent.setOuterConnection(child);
		}
		child.setOuterConnection(parent);
	}

	@Override
	public void connect(Port<?, ?> portInstance, AbstractModelClassRuntime object) {
		getRuntimeOf(portInstance).setInnerConnection(object);
	}

	@Override
	public <L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void link(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj) {
		C left = getRuntimeOf(leftObj);
		C right = getRuntimeOf(rightObj);

		if (isLinkingDeleted(left) || isLinkingDeleted(right)) {
			return;
		}

		tryAddToAssoc(left, rightEnd, right, rightObj, () -> {
		});
		tryAddToAssoc(right, leftEnd, left, leftObj, () -> left.removeFromAssoc(rightEnd, rightObj));
	}

	protected <R extends ModelClass, CR extends GeneralCollection<R>> void tryAddToAssoc(C left,
			Class<? extends AssociationEnd<CR>> rightEnd, C right, R rightObj, Runnable rollBack) {
		try {
			left.addToAssoc(rightEnd, rightObj);
			return;
		} catch (MultiplicityException e) {
			error(x -> x.upperBoundOfMultiplicityOffended(left.getWrapped(), rightEnd));
		} catch (MultipleContainerException e) {
			error(x -> x.multipleContainerForAnObject(left.getWrapped(), rightEnd));
		}
		rollBack.run();
	}

	@Override
	public <L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void unlink(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj) {
		C left = getRuntimeOf(leftObj);
		C right = getRuntimeOf(rightObj);

		if (isUnlinkingDeleted(left) || isUnlinkingDeleted(right)) {
			return;
		}

		if (getCheckLevel().isAtLeast(CheckLevel.OPTIONAL)) {
			if (!left.hasAssoc(rightEnd, rightObj)) {
				warning(x -> x.unlinkingNonExistingAssociation(leftEnd, leftObj, rightEnd, rightObj));
				return;
			}
		}

		left.removeFromAssoc(rightEnd, rightObj);
		right.removeFromAssoc(leftEnd, leftObj);
	}

	@Override
	public abstract void start();

	@Override
	public abstract C createModelClassRuntime(ModelClass object);

	@Override
	public abstract P createPortRuntime(Port<?, ?> portInstance, ModelClass owner);

}
