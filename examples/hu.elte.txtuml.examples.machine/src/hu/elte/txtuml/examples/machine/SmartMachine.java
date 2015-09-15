package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.model.*;
import hu.elte.txtuml.api.stdlib.*;
import hu.elte.txtuml.examples.machine.SmartMachineModel.*;

class SmartMachineModel extends Model {
	// classes

	class Machine extends ModelClass {
		ModelInt tasksToDo = new ModelInt(3);

		class Init extends Initial {}

		class Off extends State {
			@Override
			public void entry() {
				Action.log("\tMachine enters state: 'off'");
			}

			@Override
			public void exit() {
				Action.log("\tMachine exits state: 'off'");
			}
		}

		class On extends CompositeState {
			@Override
			public void entry() {
				Action.log("\tMachine enters state: 'on'");
			}

			@Override
			public void exit() {
				Action.log("\tMachine exits state: 'on'");
			}

			class Init extends Initial {}

			class Active extends State {
				@Override
				public void entry() {
					Action.log("\tMachine enters state: 'active'");
					Action.log("\tMachine: tasks to do: " + Machine.this.tasksToDo);
				}

				@Override
				public void exit() {
					Action.log("\tMachine exits state: 'active'");
				}
			}

			@From(Init.class) @To(Active.class)
			class Initialize extends Transition {}

			@From(Active.class) @To(Active.class) @Trigger(DoTasks.class)
			class DoActivity extends Transition {
				@Override
				public void effect() {
					DoTasks doTasks = getSignal(DoTasks.class);
					Machine.this.tasksToDo = Machine.this.tasksToDo
							.subtract(doTasks.count);
					Action.log("\tMachine: becoming active...");
				}
				
				@Override
				public ModelBool guard() {
					return Machine.this.tasksToDo.isMore(ModelInt.ZERO);
				}
			}
		}

		@From(Init.class) @To(Off.class)
		class Initialize extends Transition {
			@Override
			public void effect() {
				Action.log("\tMachine: initializing...");
			}
		}

		@From(Off.class) @To(On.class) @Trigger(ButtonPress.class)
		class SwitchOn extends Transition {
			@Override
			public void effect() {
				Action.log("\tMachine: switching on...");
			}

		}

		@From(On.class) @To(Off.class) @Trigger(ButtonPress.class)
		class SwitchOff extends Transition {
			@Override
			public void effect() {
				Action.log("\tMachine: switching off...");
			}

			@Override
			public ModelBool guard() {
				return Machine.this.tasksToDo.isLessEqual(ModelInt.ZERO);
			}
		}
	}

	class User extends ModelClass {

		ModelString name;
		ModelInt id;
		ModelInt workToDo = new ModelInt(7);

		class Init extends Initial {}
		class NotWorking extends State {}

		class WhereToGo extends Choice {}

		@From(Init.class) @To(NotWorking.class)
		class Initialize extends Transition {
			@Override
			public void effect() {
				Action.log("\t" + name.toString() + ": initializing...");				
			}
		}

		@From(NotWorking.class) @To(WhereToGo.class) @Trigger(DoYourWork.class)
		class NW_WTG extends Transition {}

		@From(WhereToGo.class) @To(NotWorking.class) // from choice (trigger is not needed)
		class NoWork extends Transition {
			@Override
			public ModelBool guard() {
				return workToDo.isEqual(ModelInt.ZERO);
			}

			@Override
			public void effect() {
				log("\t" + name.toString() + ": I have no work to do!");
			}
		}

		@From(WhereToGo.class) @To(NotWorking.class) // from choice (trigger is not needed)
		class HasWork extends Transition {
			@Override
			public ModelBool guard() {
				return ModelBool.ELSE;
			}

			@Override
			public void effect() {
				log("\t" + name.toString() + ": I am doing my work now...");
				doWork();
			}
		}

		User findOtherUser(Machine m) {

			log("\t" + name.toString() + ": analyzing machine...");

			Collection<User> usersOfM = m.assoc(Usage.userOfMachine.class);
 			
 			log("\t\tI found its users!");
			log("");
			
			For(usersOfM, user -> user.sayHello());
			log("");

		 	If(() -> usersOfM.contains(this), () ->
	 			log("\t\tI am a user of this machine.")); 

		 	If(() -> usersOfM.selectAll(user -> user.id.isEqual(ModelInt.ZERO)).isEmpty(), () ->
	 			log("\t\tNo user of this machine has an id of 0."));

		 	Collection<User> otherUsers = usersOfM.remove(this);
		 	
		 	If(() -> otherUsers.count().isEqual(ModelInt.ONE), () ->
		 		log("\t\tThere is exactly one other person who is user of this machine."));
		 	
		  	return otherUsers.selectAny();
		}
		
		void sayHello() {
			log("\t\tHello, I am " + name.toString() + ".");
		}
		
		void newWork() {
			workToDo = workToDo.add(ModelInt.ONE);
			log("\t" + name.toString() + ": I got some new work to do.");
		}
		
		void workDone() {
			workToDo = workToDo.subtract(ModelInt.ONE);
			log("\t" + name.toString() + ": I am done with some of my work.");
		}
		
		void doWork() {
			Action.log("\t" + name.toString() + ": starting to work...");

			Machine myMachine = this.assoc(Usage.usedMachine.class).selectAny();
			
			User otherUser = findOtherUser(myMachine);

			log("\t" + name.toString() + ": giving some of my work to other user...");
			
		 	For(new ModelInt(1), new ModelInt(3), i -> {// repeating three times
		 			log("\t" + name.toString() + ": giving work to other user (" + i +")");
		 			otherUser.newWork();
		 			this.workDone();
		 		});
		 			 	
			send(myMachine, new ButtonPress());
			// Switching the machine on.
			
			log("\t" + name.toString() + ": finishing my work...");
			
			While(() -> workToDo.isMore(ModelInt.ZERO), () -> {
				send(myMachine, new DoTasks(ModelInt.ONE));
				this.workDone();
			});
			// Repeats until he or she has work to do.

			Timer.start(myMachine, new ButtonPress(), new ModelInt(2000));
			// Switching off the machine with some delay.
			
			Action.log("\t" + name.toString() + ": work finished...");

		}

	}

	// associations

	class Usage extends Association {
		class usedMachine extends One<Machine> {}
		class userOfMachine extends Many<User> {}
	}

	// signals

	class ButtonPress extends Signal {
		ModelString name = new ModelString("ButtonPress");
	}

	class DoTasks extends Signal {
		ModelInt count;
		
		DoTasks(ModelInt count) {
			this.count = count;
		}
	}

	static class DoYourWork extends Signal {}
	// Signal classes are allowed to be static for simpler use.
	
}

class SmartMachineTester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);
		
		Machine m = Action.create(Machine.class);
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		u1.name = new ModelString("user1");
		u2.name = new ModelString("user2");
		u1.id = new ModelInt(1);
		u2.id = new ModelInt(2);

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);

		Action.log("One of the users is starting to do his or her work.");
		
		User oneOfTheUsers = m.assoc(Usage.userOfMachine.class).selectAny();
		// In SimpleMachineModel this cannot be done as userOfMachine
		// association end is non-navigable in that model.		
		Action.send(oneOfTheUsers, new DoYourWork());
		
		Timer.start(oneOfTheUsers, new DoYourWork(), new ModelInt(5000));
		
		Timer.shutdown();
	}

}

public class SmartMachine {
	public static void main(String[] args) {
		new SmartMachineTester().test();
	}
}