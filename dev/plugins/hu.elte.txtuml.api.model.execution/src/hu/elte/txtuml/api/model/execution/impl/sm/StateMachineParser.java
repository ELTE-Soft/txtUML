package hu.elte.txtuml.api.model.execution.impl.sm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.CompositeState;
import hu.elte.txtuml.api.model.StateMachine.Initial;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.utils.InstanceCreator;

public final class StateMachineParser {

	private final ModelClass owner;
	private final Map<Class<?>, VertexWrapperBuilder> vertices = new HashMap<>();
	private final List<TransitionWrapperBuilder> transitions = new ArrayList<>();

	private VertexWrapperBuilder initial = null;

	public static VertexWrapper parse(ModelClass owner) {
		return new StateMachineParser(owner).parse();
	}

	private StateMachineParser(ModelClass owner) {
		this.owner = owner;
	}

	private VertexWrapper parse() {
		for (Class<?> cls = owner.getClass(); cls != ModelClass.class; cls = cls.getSuperclass()) {
			parsePartOfSM(owner, cls.getDeclaredClasses(), null);

			transitions.forEach(t -> {
				VertexWrapperBuilder source = vertices.get(t.typeOfSource);
				VertexWrapperBuilder target = vertices.get(t.typeOfTarget);

				if (source == null || target == null) {
					return;
				}

				source.outgoings.add(t);
				t.target = target;
			});

			if (initial != null) {
				return initial.get();
			}

			vertices.clear();
			transitions.clear();
		}
		return null;
	}

	/**
	 * @param parent
	 *            the parent of the sm part to parse
	 * @param container
	 *            {@code null} if parent is a model class instance and the
	 *            corresponding wrapper builder if it is a composite state
	 */
	private void parsePartOfSM(Object parent, VertexWrapperBuilder container) {
		parsePartOfSM(parent, parent.getClass().getDeclaredClasses(), container);
	}

	/**
	 * @param parent
	 *            the parent of the sm part to parse
	 * @param innerClasses
	 *            the inner classes to parse
	 * @param container
	 *            {@code null} if parent is a model class instance and the
	 *            corresponding wrapper builder if it is a composite state
	 */
	private void parsePartOfSM(Object parent, Class<?>[] innerClasses, VertexWrapperBuilder container) {

		for (Class<?> inner : innerClasses) {

			if (Transition.class.isAssignableFrom(inner)) {
				transitions.add(new TransitionWrapperBuilder(inner, parent));
			} else if (Vertex.class.isAssignableFrom(inner)) {
				vertices.put(inner, new VertexWrapperBuilder(inner, parent, container));
			}

		}

	}

	// builder classes

	private class VertexWrapperBuilder extends WrapperBuilder<VertexWrapper, Vertex> {

		final VertexWrapperBuilder container;
		final List<TransitionWrapperBuilder> outgoings = new ArrayList<>();

		VertexWrapperBuilder initialOfSubSM = null;

		VertexWrapperBuilder(Class<?> type, Object parent, VertexWrapperBuilder container) {
			super((Vertex) InstanceCreator.create(type, parent));

			this.container = container;

			if (Initial.class.isAssignableFrom(type)) {
				if (container == null) {
					StateMachineParser.this.initial = this;
				} else {
					container.initialOfSubSM = this;
				}
			} else if (CompositeState.class.isAssignableFrom(type)) {
				parsePartOfSM(getWrapped(), this);
			}

		}

		@Override
		protected void build() {
			VertexWrapper builtContainer = container == null ? null : container.get();

			TransitionWrapper[] arrayOfOutGoings = new TransitionWrapper[outgoings.size()];

			if (initialOfSubSM != null) {
				result = VertexWrapper.createComposite(getWrapped(), builtContainer, arrayOfOutGoings);
				result.setSubSM(initialOfSubSM.get());
			} else {
				result = VertexWrapper.create(getWrapped(), builtContainer, arrayOfOutGoings);
			}

			for (int i = 0; i < outgoings.size(); ++i) {
				arrayOfOutGoings[i] = outgoings.get(i).get();
			}
		}
	}

	private static class TransitionWrapperBuilder extends WrapperBuilder<TransitionWrapper, Transition> {

		final Class<?> typeOfSource;
		final Class<?> typeOfTarget;

		VertexWrapperBuilder target = null;

		TransitionWrapperBuilder(Class<?> type, Object parent) {
			super((Transition) InstanceCreator.create(type, parent));

			From from = type.getAnnotation(From.class);
			this.typeOfSource = from == null ? null : from.value();

			To to = type.getAnnotation(To.class);
			this.typeOfTarget = to == null ? null : to.value();
		}

		protected void build() {
			result = TransitionWrapper.create(getWrapped(), target.get());
		}
	}

}
