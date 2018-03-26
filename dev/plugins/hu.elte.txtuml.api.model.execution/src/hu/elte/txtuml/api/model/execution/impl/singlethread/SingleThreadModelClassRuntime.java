package hu.elte.txtuml.api.model.execution.impl.singlethread;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Container;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.ModelClass.Status;
import hu.elte.txtuml.api.model.execution.CheckLevel;
import hu.elte.txtuml.api.model.execution.impl.assoc.AssociationEndRuntime;
import hu.elte.txtuml.api.model.execution.impl.assoc.AssociationsMap;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultipleContainerException;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultiplicityException;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.SignalWrapper;
import hu.elte.txtuml.utils.InstanceCreator;

/**
 * A {@link hu.elte.txtuml.api.model.impl.ModelClassRuntime} implementation for
 * model executors that use only one model executor thread. This may not be used
 * in a multi-thread executor as it lacks the necessary synchronizations.
 */
public class SingleThreadModelClassRuntime extends AbstractModelClassRuntime {

	private static final AtomicLong counter = new AtomicLong();

	private final AssociationsMap associations = AssociationsMap.create();
	private final ClassToInstanceMap<Port<?, ?>> ports = MutableClassToInstanceMap.create();

	public SingleThreadModelClassRuntime(ModelClass wrapped, AbstractExecutorThread thread) {
		super(wrapped, thread, "obj" + counter.getAndIncrement());
	}

	@Override
	public boolean isDeleted() {
		return getStatus() == Status.DELETED;
	}

	@Override
	public void start() {
		if (getStatus() != Status.READY) {
			if (isDeleted()) {
				getModelRuntime().error(x -> x.startingDeletedObject(getWrapped()));
			}
			return;
		}

		if (getModelRuntime().getCheckLevel().isAtLeast(CheckLevel.OPTIONAL)) {
			initializeAllDefinedAssociationEnds();
		}

		setStatus(Status.ACTIVE);

		process(SignalWrapper.of(null));
	}

	@Override
	public void delete() {
		if (!isDeletable()) {
			getModelRuntime().error(x -> x.objectCannotBeDeleted(getWrapped()));
			return;
		}

		setStatus(Status.DELETED);
	}

	/**
	 * Checks if this model object is ready to be deleted. If it is already
	 * deleted, this method automatically returns {@code true}. Otherwise, it
	 * checks whether all associations to this object have already been
	 * unlinked.
	 * 
	 * @return {@code true} if this model object is ready to be deleted,
	 *         {@code false} otherwise
	 */
	private boolean isDeletable() {
		if (isDeleted()) {
			return true;
		}

		for (AssociationEndRuntime<?, ?> assocEnd : this.associations.values()) {
			if (!assocEnd.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public <P extends Port<?, ?>> P getPortInstance(Class<P> portType) {
		P inst = ports.getInstance(portType);

		if (inst == null) {
			inst = createNewPortInstance(portType);
		}

		return inst;
	}

	/**
	 * Called when an instance of the given port type is queried but it is not
	 * present yet.
	 */
	protected <P extends Port<?, ?>> P createNewPortInstance(Class<P> portType) {
		P inst = InstanceCreator.create(portType, getWrapped());
		if (portType.isAnnotationPresent(BehaviorPort.class)) {
			getModelRuntime().connect(inst, this);
		}
		ports.putInstance(portType, inst);
		return inst;
	}

	@Override
	public <T extends ModelClass, C extends GeneralCollection<? super T>> AssociationEndRuntime<T, C> getAssoc(
			Class<? extends AssociationEnd<C>> otherEnd) {
		AssociationEndRuntime<T, C> ret = associations.getEnd(otherEnd);
		if (ret == null) {
			ret = AssociationEndRuntime.create(otherEnd);
			associations.putEnd(otherEnd, ret);
		}
		return ret;
	}

	@Override
	public <T extends ModelClass, C extends GeneralCollection<? super T>> boolean hasAssoc(
			Class<? extends AssociationEnd<C>> otherEnd, T object) {

		AssociationEndRuntime<T, ?> actualOtherEnd = associations.getEnd(otherEnd);
		return actualOtherEnd == null ? false : actualOtherEnd.getCollection().contains(object);
	}

	@Override
	public <T extends ModelClass, C extends GeneralCollection<? super T>> void addToAssoc(
			Class<? extends AssociationEnd<C>> otherEnd, T object)
			throws MultiplicityException, MultipleContainerException {
		containerCheck(otherEnd);
		AssociationEndRuntime<T, ?> assocEnd = getAssoc(otherEnd);
		assocEnd.add(object);
	}

	@Override
	public <T extends ModelClass, C extends GeneralCollection<? super T>> void removeFromAssoc(
			Class<? extends AssociationEnd<C>> otherEnd, T object) {

		AssociationEndRuntime<T, C> assocEnd = getAssoc(otherEnd);
		assocEnd.remove(object);

		if (getModelRuntime().getCheckLevel().isAtLeast(CheckLevel.OPTIONAL) && !assocEnd.checkLowerBound()) {
			getThread().addDelayedAction(() -> {
				if (!assocEnd.checkLowerBound() && !isDeleted()) {
					getModelRuntime().error(x -> x.lowerBoundOfMultiplicityOffended(getWrapped(), otherEnd));
				}
			});
		}

	}

	private <T extends ModelClass, C extends GeneralCollection<? super T>, AE extends AssociationEnd<C>> void containerCheck(
			Class<AE> otherEnd) throws MultipleContainerException {
		if (Container.class.isAssignableFrom(otherEnd)) {
			for (Entry<Class<? extends AssociationEnd<?>>, AssociationEndRuntime<?, ?>> entry : associations
					.entrySet()) {
				if (Container.class.isAssignableFrom(entry.getKey()) && !entry.getValue().isEmpty()) {
					throw new MultipleContainerException();
				}
			}
		}
	}

	/**
	 * Looks up all the defined associations of the model class this object is
	 * an instance of and initializes them, if they have not been initialized
	 * yet, by assigning empty {@link Collection Collections} to them in the
	 * {@link #associations} map. If any of them has a lower bound which is
	 * currently offended then registers that association end to be checked in
	 * the next <i>execution step</i>.
	 * <p>
	 * Shows an error about a bad model if any exception is thrown during the
	 * above described process as this method and all the methods this calls,
	 * assume that the model is well-defined.
	 * <p>
	 * See the documentation of {@link Model} for information about execution
	 * steps.
	 */
	protected void initializeAllDefinedAssociationEnds() {
		// TODO Implement initialization of defined association ends.

	}

}
