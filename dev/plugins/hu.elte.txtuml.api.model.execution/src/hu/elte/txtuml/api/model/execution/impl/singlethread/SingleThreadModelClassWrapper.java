package hu.elte.txtuml.api.model.execution.impl.singlethread;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.ModelClass.Status;
import hu.elte.txtuml.api.model.assocends.ContainmentKind;
import hu.elte.txtuml.api.model.execution.impl.assoc.AssociationEndWrapper;
import hu.elte.txtuml.api.model.execution.impl.assoc.AssociationsMap;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultipleContainerException;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultiplicityException;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassWrapper;
import hu.elte.txtuml.api.model.execution.impl.base.ModelExecutorThread;
import hu.elte.txtuml.utils.InstanceCreator;

/**
 * A {@link hu.elte.txtuml.api.model.runtime.ModelClassWrapper} implementation for model executors that use only
 * one model executor thread. This may not be used in a multi-thread executor as
 * it lacks the necessary synchronizations.
 */
public class SingleThreadModelClassWrapper extends AbstractModelClassWrapper {

	private static final AtomicLong counter = new AtomicLong();

	private final AssociationsMap associations = AssociationsMap.create();
	private final ClassToInstanceMap<Port<?, ?>> ports = MutableClassToInstanceMap.create();

	public SingleThreadModelClassWrapper(ModelClass wrapped, ModelExecutorThread thread) {
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
				getRuntime().error(x -> x.startingDeletedObject(getWrapped()));
			}
			return;
		}

		if (getRuntime().dynamicChecks()) {
			initializeAllDefinedAssociationEnds();
		}

		setStatus(Status.ACTIVE);

		process(null, null, false);
	}

	@Override
	public void delete() {
		if (!isDeletable()) {
			getRuntime().error(x -> x.objectCannotBeDeleted(getWrapped()));
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

		for (AssociationEndWrapper<?, ?> assocEnd : this.associations.values()) {
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
			getRuntime().connect(inst, this);
		}
		ports.putInstance(portType, inst);
		return inst;
	}

	@Override
	public <T extends ModelClass, C extends Collection<T>> AssociationEndWrapper<T, C> getAssoc(
			Class<? extends AssociationEnd<T, C>> otherEnd) {
		AssociationEndWrapper<T, C> ret = associations.getEnd(otherEnd);
		if (ret == null) {
			ret = AssociationEndWrapper.create(otherEnd);
			associations.putEnd(otherEnd, ret);
		}
		return ret;
	}

	@Override
	public <T extends ModelClass, C extends Collection<T>> boolean hasAssoc(
			Class<? extends AssociationEnd<T, C>> otherEnd, T object) {

		AssociationEndWrapper<T, ?> actualOtherEnd = associations.getEnd(otherEnd);
		return actualOtherEnd == null ? false : actualOtherEnd.getCollection().contains(object);
	}

	@Override
	public <T extends ModelClass, C extends Collection<T>> void addToAssoc(
			Class<? extends AssociationEnd<T, C>> otherEnd, T object)
			throws MultiplicityException, MultipleContainerException {
		containerCheck(otherEnd);
		AssociationEndWrapper<T, ?> assocEnd = getAssoc(otherEnd);
		assocEnd.add(object);
	}

	@Override
	public <T extends ModelClass, C extends Collection<T>> void removeFromAssoc(
			Class<? extends AssociationEnd<T, C>> otherEnd, T object) {

		AssociationEndWrapper<T, C> assocEnd = getAssoc(otherEnd);
		assocEnd.remove(object);

		if (getRuntime().dynamicChecks() && !assocEnd.checkLowerBound()) {
			getThread().addDelayedAction(() -> {
				if (!assocEnd.checkLowerBound()) {
					getRuntime().error(x -> x.lowerBoundOfMultiplicityOffended(getWrapped(), otherEnd));
				}
			});
		}

	}

	private <T extends ModelClass, C extends Collection<T>, AE extends AssociationEnd<T, C>> void containerCheck(
			Class<AE> otherEnd) throws MultipleContainerException {
		if (ContainmentKind.ContainerEnd.class.isAssignableFrom(otherEnd)) {
			for (Entry<Class<? extends AssociationEnd<?, ?>>, AssociationEndWrapper<?, ?>> entry : associations
					.entrySet()) {
				if (ContainmentKind.ContainerEnd.class.isAssignableFrom(entry.getKey())
						&& !entry.getValue().isEmpty()) {
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
