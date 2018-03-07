package hu.elte.txtuml.api.model.execution.impl.singlethread;

import java.util.List;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultipleContainerException;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultiplicityException;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassWrapper;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractPortWrapper;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.ModelExecutorThread;

/**
 * Abstract base class for {@link Runtime} implementations which belong to model
 * executors that use only one model executor thread.
 */
public abstract class SingleThreadRuntime<C extends AbstractModelClassWrapper, P extends AbstractPortWrapper>
		extends AbstractRuntime<C, P> {

	/**
	 * Must be called on the thread which manages the given
	 * {@link AbstractModelExecutor} instance.
	 */
	protected SingleThreadRuntime(AbstractModelExecutor<?> executor) {
		super(executor);
	}

	protected SingleThreadRuntime(AbstractModelExecutor<?> executor, List<TraceListener> traceListeners,
			List<ErrorListener> errorListeners, List<WarningListener> warningListeners, boolean dynamicChecks,
			double executionTimeMultiplier) {
		super(executor, traceListeners, errorListeners, warningListeners, dynamicChecks, executionTimeMultiplier);
	}

	@Override
	public <C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort) {
		P left = getInfo(leftPort);
		P right = getInfo(rightPort);

		left.setOuterConnection(right);
		right.setOuterConnection(left);
	}

	@Override
	public <P1 extends Port<I1, I2>, C0 extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C0> childEnd, P2 childPort) {
		P parent = getInfo(parentPort);
		P child = getInfo(childPort);
		
		if (parent.getInnerConnection() == null) {
			parent.setInnerConnection(child);			
		} else {
			parent.setOuterConnection(child);
		}
		child.setOuterConnection(parent);
	}

	@Override
	public void connect(Port<?, ?> portInstance, AbstractModelClassWrapper object) {
		getInfo(portInstance).setInnerConnection(object);
	}

	@Override
	public <L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void link(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj) {
		C left = getInfo(leftObj);
		C right = getInfo(rightObj);

		if (isLinkingDeleted(left) || isLinkingDeleted(right)) {
			return;
		}

		tryAddToAssoc(left, rightEnd, right, rightObj, () -> {
		});
		tryAddToAssoc(right, leftEnd, left, leftObj, () -> left.removeFromAssoc(rightEnd, rightObj));
	}

	protected <R extends ModelClass, CR extends GeneralCollection<R>> void tryAddToAssoc(C left, Class<? extends AssociationEnd<CR>> rightEnd, C right,
			R rightObj, Runnable rollBack) {
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
		C left = getInfo(leftObj);
		C right = getInfo(rightObj);

		if (isUnlinkingDeleted(left) || isUnlinkingDeleted(right)) {
			return;
		}

		if (dynamicChecks()) {
			if (!left.hasAssoc(rightEnd, rightObj)) {
				warning(x -> x.unlinkingNonExistingAssociation(leftEnd, leftObj, rightEnd, rightObj));
				return;
			}
		}

		left.removeFromAssoc(rightEnd, rightObj);
		right.removeFromAssoc(leftEnd, leftObj);
	}

	public abstract ModelExecutorThread getThread();

	@Override
	public abstract void start();
	
	@Override
	protected abstract C createModelClassWrapper(ModelClass object);

	@Override
	protected abstract P createPortWrapper(Port<?, ?> portInstance, ModelClass owner);

}
