package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.*;
import hu.elte.txtuml.stdlib.*;
import hu.elte.txtuml.examples.machine.SmartMachineModel.*;

class SmartMachineModel extends Model {
	
	class Machine extends ModelClass {
		ModelInt tasksTodo = new ModelInt(3);

		class Init extends Initial {}
		
		class Off extends State {
			@Override public void entry() {
	        	Action.log("Machine: entering state: 'off'");
            }
            @Override public void exit() {
	        	Action.log("Machine: leaving state: 'off'");
            }
		}
				
		class On extends CompositeState {
			@Override public void entry() {
	        	Action.log("Machine: entering state: 'on'");
            }
			@Override public void exit() {
	        	Action.log("Machine: leaving state: 'on'");
            }
			
			class Init extends Initial {}

			class Active extends State {
				@Override public void entry() {
		            Action.log("Machine: entering state: 'active'");
					Action.log("tasks todo: " + Machine.this.tasksTodo);
				}
	            @Override public void exit() {
		        	Action.log("Machine: leaving state: 'active'");
	            }
			}

			@From(Init.class) @To(Active.class)
			class Initialize extends Transition {}
			
			@From(Active.class) @To(Active.class) @Trigger(DoTasks.class)
			class DoActivity extends Transition {
				@Override public void effect() {
					DoTasks dTE = getSignal(DoTasks.class);
					Machine.this.tasksTodo = Machine.this.tasksTodo.subtract(dTE.count);	
					Action.log("\tMachine: Becoming active...");
				}
			}
		}

		@From(Init.class) @To(Off.class)
		class Initialize extends Transition {
			@Override public void effect() {
				Action.log("\tMachine: Initializing...");
			}
		} 
		
		@From(Off.class) @To(On.class) @Trigger(ButtonPress.class)
        class SwitchOn extends Transition {
			@Override public void effect() {
				Action.log("\tMachine: Switch on...");
			}
			
		}
		
		@From(On.class) @To(Off.class) @Trigger(ButtonPress.class)
		class SwitchOff extends Transition {
			@Override public void effect() {
				Action.log("\tMachine: Switch off...");
			}
			@Override public ModelBool guard() {
				return Machine.this.tasksTodo.isLessEqual(ModelInt.ZERO);
			}
		}
	}
	
	class User extends ModelClass {
		ModelString name;
		ModelInt id;
		ModelInt workToDo = new ModelInt(3);
		
		void doWork() {
			Action.log("User: starting to work...");
			Object o = this.assoc(Usage.usedMachine.class);
			System.out.println(o.getClass().getSimpleName());
			Machine myMachine = this.assoc(Usage.usedMachine.class).selectAny();
			
			Action.send(myMachine, new ButtonPress()); // switches the machine on
			Action.send(myMachine, new ButtonPress()); // tries to switch it off, but fails because of the guard
			Action.send(myMachine, new DoTasks(ModelInt.ONE)); // the machine becomes active and decreases its tasks-to-do count by 1 
			workToDo = workToDo.subtract(ModelInt.ONE);
			
			Action.send(myMachine, new ButtonPress()); // tries to switch it off, but fails again
			
			While(() -> workToDo.isMore(ModelInt.ZERO), () -> { // repeats until he or she has work to do
				Action.send(myMachine, new DoTasks(new ModelInt(1)));
						workToDo = workToDo.subtract(ModelInt.ONE);
			});
			
			For(new ModelInt(0), new ModelInt(5),
					i -> If( () -> i.isLess(new ModelInt(3)),
							() -> Action.send(myMachine, new ButtonPress()),
							() -> Action.send(myMachine, new DoTasks(ModelInt.ONE))
					) );
			
			Timer.Handle t1 = Timer.start(myMachine, new ButtonPress(), new ModelInt(2000)); // switching off the machine with delay
			t1.add(new ModelInt(3000));

			Action.send(myMachine, new ButtonPress());
						
			Action.log("User: work finished...");
		}
		
		void sayHello() {
			Action.log("Hello, I am " + name.toString());
		}
		
		class Init extends Initial {}
		class NotWorking extends State {}
		class WhereToGo extends Choice {}
		
		@From(Init.class) @To(NotWorking.class)
		class Initialize extends Transition {}
		
		@From(NotWorking.class) @To(WhereToGo.class) @Trigger(DoYourWork.class)
		class NW_WTG extends Transition {}
		
		@From(WhereToGo.class) @To(NotWorking.class) // from choice
		class HasWork extends Transition {
			@Override public ModelBool guard() {
				return ModelBool.ELSE;
			}
			@Override public void effect() {
				log("User: I am doing my work now...");
				doWork();
			}
		}		

		@From(WhereToGo.class) @To(NotWorking.class) // from choice
		class NoWork extends Transition {
			@Override public ModelBool guard() {
				return workToDo.isEqual(ModelInt.ZERO); 
			}
			@Override public void effect() {
				log("I have no work to do!");
			}
		}		
	}
	
	// Association(s)
	
	class Usage extends Association {
		class usedMachine extends One<Machine> {}
		class userOfMachine extends Many<User> {}
	}

	
	// Signals
	
	class ButtonPress extends Signal {
		ModelString name = new ModelString("ButtonPress");
	}

	class DoTasks extends Signal {
		DoTasks(ModelInt count) {
			this.count = count; 
		}
		ModelInt count;
	}

	class DoYourWork extends Signal {}

}

class SmartMachineTester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);
	
		Machine m = Action.create(Machine.class); 
		
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

        u1.name = new ModelString("user1");
        u2.name = new ModelString("user2");
        u1.id = new ModelInt(100);
        u2.id = new ModelInt(200);
		
        Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
        Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);
 
		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);
        
/*		Collection<User> userEndOfAssoc = m.assoc(Usage.userOfMachine.class);
        
		For(userEndOfAssoc, user -> user.sayHello());
        
		If(() -> userEndOfAssoc.remove(u1).add(u1).count().isEqual(new ModelInt(2)),
        		() -> Action.log("There are exactly two users."));
        
		
		If(() -> userEndOfAssoc.contains(u1),
				() -> log("user1 is on the other side of the association."));
		If(() -> userEndOfAssoc.selectAll(user -> user.id.isEqual(ModelInt.ZERO)).isEmpty(),
				() -> log("Nobody has an id of 0."));
		Collection<User> users = new Collection.Empty<>();
		users.addAll(userEndOfAssoc).selectAny().sayHello();
		
		
		For(new ModelInt(0), new ModelInt(5),
				i -> If( () -> i.isLess(new ModelInt(3)),
						() -> log("Number lesser than three."),
						() -> log("Number is not lesser than three.")
				) );*/
					
		Action.log("One of the users is starting to do his or her work.");
		User oneOfTheUsers = m.assoc(Usage.userOfMachine.class).selectAny();
//		Action.send(oneOfTheUsers, new DoYourWork());
		
//		Timer.start(oneOfTheUsers, new DoYourWork(), new ModelInt(10000));
	}

}

public class SmartMachine {
	public static void main(String[] args) {
		new SmartMachineTester().test();
	}
}