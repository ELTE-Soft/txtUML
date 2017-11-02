/**
 * Provides txtUML models the ability to communicate with the outside world in an organized
 * and thread-safe way. Intended for use cases when the model has to send
 * messages to active outside entities which operate on their own separate threads.
 * <p>
 * These active outside entities are represented by
 * {@link hu.elte.txtuml.api.stdlib.world.WorldObject WorldObject}s in the model which
 * belong to the single {@link hu.elte.txtuml.api.stdlib.world.World World}
 * instance and have unique {@code String} identifiers.
 * Signals sent to these world objects are then handled by specific listeners.
 * <p>
 * Note that as {@code World} and {@code WorldObject} are model classes, their instances are always
 * created in and belong to a specific model execution. That is, the {@code World} is singleton
 * and world object identifiers are unique only in the context of a specific model execution.
 * Multiple parallel model executions are not related in any way.
 * 
 * <h1>How to use</h1>
 * 
 * <h2>Shared between the model and the outside world</h2>
 * 
 * There are only two things that need to be shared between the model and the outside world.
 * <ul>
 * <li>The strings that identify the outside entities and also identify the world objects that represent them.<br/>
 * For this, static fields or methods are recommended to ensure thread safety.</li>
 * <li>The signal types to be sent.<br/>
 * These signal types have to be part of the model and <b>must extend
 * {@link hu.elte.txtuml.api.stdlib.world.SignalToWorld SignalToWorld}</b>. Apart from simple Java
 * visibility, nothing else is required for them to be used in the outside world as well.</li>
 * </li>
 * </ul>
 * 
 * <h2>Send signals from the model</h2>
 * 
 * Simple as it can be. Just:
 * <ol>
 * <li>Create an instance of the required signal (that extends {@code SignalToWorld}).</li>
 * <li>Get the world object for the appropriate identifier. For this, simply call {@link
 * hu.elte.txtuml.api.stdlib.world.World#get World.get}.</li>
 * <li>Call {@link hu.elte.txtuml.api.model.Action#send(Signal, ModelClass) Action.send} as always.</li>
 * </ol>
 * 
 * <h2>Handle signals outside</h2>
 * 
 * <ol>
 * <li>Subclass one of the subclasses of {@link
 * hu.elte.txtuml.api.stdlib.world.WorldObjectListener WorldObjectListener}.<br/>
 * This is the step that has to be done with some care as different situations require different
 * listeners to ensure thread safety. Take a look at the predefined versions or use
 * {@link hu.elte.txtuml.api.stdlib.world.AbstractWorldObjectListener AbstractWorldObjectListener} if
 * no other helps.</li>
 * <li>In the listener, create signal handler methods that will take the desired signal as parameter.
 * They also have to be {@code public} and have the
 * {@link hu.elte.txtuml.api.stdlib.world.WorldObjectListener.SignalHandler SignalHandler} annotation.</li>
 * <li>Call the {@link hu.elte.txtuml.api.stdlib.world.WorldObjectListener#register
 * WorldObjectListener.register} method to register for signals of the world object that has the specified
 * identifier and belongs to the given model executor.</li>
 * <li>Wait for signals to arrive.</li>
 * </ol>
 * <h2>Send response to the model</h2>
 * Obtain a reference to the target model class instance and use
 * {@link hu.elte.txtuml.api.model.API#send(Signal, ModelClass) API.send} as always.
 * 
 * <h1>Example</h1>
 * 
 * <h2>Model</h2>
 * 
 * <pre>
 * <code>
 * class SigIn extends Signal {}
 * class SigOut extends SignalToWorld {}
 * 
 * class SampleClass extends ModelClass {
 * 	class init extends Initial {}
 * 	class waiting extends State {}
 * 
 * 	{@literal @From(init.class) @To(waiting.class)}
 * 	class initialize extends Transition {}
 * 
 * 	{@literal @From(waiting.class) @To(waiting.class)}
 * 	{@literal @Trigger(SigIn.class)}
 * 	class acceptSigIn extends Transition {
 * 		{@literal @Override}
 * 		public void effect() {
 * 			Action.log("SigIn arrived");
 * 			Action.send(new SigOut(), World.get(responderId()));
 * 		}
 * 	}
 * 
 * 	public static String responderId() {
 * 		return "responder";
 * 	}
 * 
 * }
 * </pre>
 * </code>
 * 
 * <h2>Listener</h2>
 * 
 * <pre>
 * <code>
 * class Listener extends QueueWorldObjectListener {
 * 	SampleClass modelObj;
 * 
 * 	Listener(SampleClass modelObj) {
 * 		this.modelObj = modelObj;
 * 	}
 * 
 * 	{@literal @SignalHandler}
 * 	public void accept(SigOut s) {
 * 		System.out.println("SigOut arrived");
 * 		try {
 * 			Thread.sleep(2000);
 * 		} catch(InterruptedException e) {}
 * 
 * 		API.send(new SigIn(), modelObj);
 * 	}
 * }
 * </pre>
 * </code>
 * 
 * <h2>Main</h2>
 * 
 * <pre>
 * <code>
 * public class Main {
 * 
 * 	static SampleClass modelObj;
 * 
 * 	public static void main(String[] args) {
 * 		ModelExecutor executor = ModelExecutor.create().launch( () -> {
 * 			modelObj = Action.create(SampleClass.class);
 * 			Action.start(modelObj);
 * 			Action.send(new SigIn(), modelObj);
 * 		});
 * 
 * 		Listener listener = new Listener(modelObj);
 * 		listener.register(executor, SampleClass.responderId());
 *  	while (true) {
 *  		listener.handleNext();
 *  	}
 * 	}
 * 
 * }
 * </pre>
 * </code>
 * 
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@Model("txtUML World")
package hu.elte.txtuml.api.stdlib.world;

import hu.elte.txtuml.api.model.Model;
