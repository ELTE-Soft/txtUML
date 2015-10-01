package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Consumation;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.ConsumeJob;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Consumer;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.ProduceJob;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Producer;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Production;
import hu.elte.txtuml.examples.producer_consumer.Producer_consumerModel.Storage;

import java.util.ArrayList;
import java.util.List;

class Producer_consumerModel extends Model {
	
	// Classes
	
	class Item extends ModelClass{}
	
	static class ProduceJob extends Signal{}
	
	class Producer extends ModelClass
	{
		
		
		class Init extends Initial{}
		
		@From(Init.class) @To(ProduceItem.class) @Trigger(ProduceJob.class)
		class Initialize extends Transition {}
		
		class ProduceItem extends State {
			@Override
			public void entry() {
				Storage store = Producer.this.assoc(Production.store.class).selectAny();
				
				Item it = Action.create(Item.class);
				
				while ( store.isFull() ) 
				{
					Action.log("Producer: storage is full, retrying later...");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
				
				Action.log("Producer: creating item.");
				Action.send(store, new PutItem(it));
			}
		}
		
		@From(ProduceItem.class) @To(ProduceItem.class) @Trigger(ProduceJob.class)
		class Job extends Transition{}
		
	}
	
	class PutItem extends Signal
	{
		Item item;
		
		public PutItem(Item it)
		{
			item = it;
		}
	}

	class GetItem extends Signal
	{
		Consumer needer;
		
		public GetItem(Consumer cons)
		{
			needer = cons;
		}
	}
	
	class Storage extends ModelClass
	{
		List<Item> items;
		int top;
		
		public Storage(int top)
		{
			this.top = top;
			items = new ArrayList<Item>();
		}
		
		public boolean isFull()
		{
			return top == items.size();
		}
		
		class Init extends Initial{}
		
		@From(Init.class) @To(Empty.class)
		class Initialize extends Transition{}
		
		class Empty extends State
		{
			@Override
			public void entry() 
			{
				Action.log("Storage: empty.");
			}
		}
		
		@From(Empty.class) @To(NotFull.class) @Trigger(PutItem.class)
		class Put extends Transition
		{
			@Override
			public void effect() 
			{
				Item it = getSignal(PutItem.class).item;
				items.add(it);
			}
		}
		
		class NotFull extends State
		{
			@Override
			public void entry() 
			{
				Action.log("Storage: has some items (" + items.size() + ").");
			}
		}
		
		@From(NotFull.class) @To(NotFull.class) @Trigger(PutItem.class)
		class Put2 extends Transition
		{
			@Override
			public boolean guard() 
			{
				return items.size() < top - 1;
			}
			
			@Override
			public void effect() 
			{
				Item it = getSignal(PutItem.class).item;
				items.add(it);
			}
		}
		
		@From(NotFull.class) @To(Full.class) @Trigger(PutItem.class)
		class Put3 extends Transition
		{
			@Override
			public boolean guard() 
			{
				return items.size() == top - 1;
			}
			
			@Override
			public void effect() 
			{
				Item it = getSignal(PutItem.class).item;
				items.add(it);
			}
		}
		
		@From(NotFull.class) @To(Empty.class) @Trigger(GetItem.class)
		class Get extends Transition
		{
			@Override
			public boolean guard() 
			{
				return items.size() == 1;
			}
			
			@Override
			public void effect() 
			{
				Item it = items.remove(0);
			
				Action.send(getSignal(GetItem.class).needer, new ConsumeItem(it));
			}
		}
		
		@From(NotFull.class) @To(NotFull.class) @Trigger(GetItem.class)
		class Get2 extends Transition
		{
			@Override
			public boolean guard() 
			{
				return items.size() > 1;
			}
			
			@Override
			public void effect() 
			{
				Item it = items.remove(0);
			
				Action.send(getSignal(GetItem.class).needer, new ConsumeItem(it));
			}
		}
		
		class Full extends State
		{
			@Override
			public void entry() 
			{
				Action.log("Storage: full.");
			}
		}
		
		@From(Full.class) @To(NotFull.class) @Trigger(GetItem.class)
		class Get3 extends Transition
		{
			@Override
			public void effect() 
			{
				Item it = items.remove(0);
			
				Action.send(getSignal(GetItem.class).needer, new ConsumeItem(it));
			}
		}
		
	}
	
	class ConsumeItem extends Signal
	{
		Item item;
		
		public ConsumeItem(Item it)
		{
			this.item = it;
		}
	}
	
	static class ConsumeJob extends Signal{}
	
	class Consumer extends ModelClass
	{
		
		private void doSomething(Item item)
		{
			Action.log("Consumer: doing something with item...");
		}
		
		class Init extends Initial{}
		
		@From(Init.class) @To(Waiting.class)
		class Initialize extends Transition{}
		
		class Waiting extends State{}
		
		@From(Waiting.class) @To(Waiting.class) @Trigger(ConsumeJob.class)
		class Get extends Transition
		{
			@Override
			public void effect()
			{
				Storage store = Consumer.this.assoc(Consumation.store.class).selectAny();
				
				Action.send(store, new GetItem(Consumer.this));
			}
		}
		
		@From(Waiting.class) @To(Waiting.class) @Trigger(ConsumeItem.class)
		class Received extends Transition
		{
			@Override
			public void effect()
			{
				Item it = getSignal(ConsumeItem.class).item;
				
				Action.log("Consumer: received item, starting to consume.");
				
				doSomething(it);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Action.log("Consumer: finished consume.");
			}
		}
		
	}
	
	// associations
	
	class Production extends Association
	{
		class producer extends Many<Producer>{}
		class store extends One<Storage>{}
	}
	
	class Consumation extends Association
	{
		class consumer extends Many<Consumer>{}
		class store extends One<Storage>{}
	}
	
	// signals
	
	
	
	// Signal classes are allowed to be static for simpler use.
	
}

class Producer_consumerTester
{
	
	void test() throws InterruptedException
	{
		ModelExecutor.Settings.setExecutorLog(false);
		
		Action.log("-- Starting test --");
		
		Storage store = Action.create(Storage.class, 5);
		
		Producer p1 = Action.create(Producer.class);
		Producer p2 = Action.create(Producer.class);
		Producer p3 = Action.create(Producer.class);
		
		Consumer c1 = Action.create(Consumer.class);
		Consumer c2 = Action.create(Consumer.class);
		Consumer c3 = Action.create(Consumer.class);
		
		Action.link(Production.store.class, store,
				Production.producer.class, p1);
		Action.link(Production.store.class, store,
				Production.producer.class, p2);
		Action.link(Production.store.class, store,
				Production.producer.class, p3);
		
		Action.link(Consumation.store.class, store,
				Consumation.consumer.class, c1);
		Action.link(Consumation.store.class, store,
				Consumation.consumer.class, c2);
		Action.link(Consumation.store.class, store,
				Consumation.consumer.class, c3);
		
		Action.log("Store starting.");
		Action.start(store);
		
		Action.log("Starting producers and consumers.");
		Action.start(p1);
		Action.start(p2);
		Action.start(p3);
		Action.start(c1);
		Action.start(c2);
		Action.start(c3);
		
		Action.send(p1, new ProduceJob());
		Thread.sleep(500);
		Action.send(c1, new ConsumeJob());
		Action.send(c2, new ConsumeJob());
		Thread.sleep(500);
		Action.send(p2, new ProduceJob());
		Action.send(p3, new ProduceJob());
		Action.send(p3, new ProduceJob());
		Thread.sleep(500);
		Action.send(c2, new ConsumeJob());
		Action.send(c3, new ConsumeJob());
		Action.send(c3, new ConsumeJob());
		
		Thread.sleep(5000);
		
		Action.log("-- Ending test --");
		
		ModelExecutor.shutdown();
	}
	
}

public class Producer_consumer
{
	public static void main(String[] args) throws InterruptedException
	{
		new Producer_consumerTester().test();
	}
}
