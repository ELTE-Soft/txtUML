package hu.elte.txtuml.diagnostics.session;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import java.lang.reflect.*;

import org.junit.Test;


public class InstanceRegisterTest {
	UniqueInstance ui1 = new UniqueInstance("obj1", 123);
	UniqueInstance ui2= new UniqueInstance("obj2", 123);
	
	@Test
	public void insertIntoAliveClasses_Test(){
		//Should return false when inserting the second instance from the same model class.
		InstanceRegister ir = new InstanceRegister();
		ir.insertIntoAliveClasses(ui1);
		boolean new_instance = ir.insertIntoAliveClasses(ui1);
		
		assertFalse(new_instance);
	}

	@Test
	public void insertIntoAliveClasses_Test2(){
		//Should return true when inserting new Unique instances (even from the same class).
		InstanceRegister ir = new InstanceRegister();
		boolean new_instance = ir.insertIntoAliveClasses(ui1);
		assertTrue(new_instance);
		
		new_instance = ir.insertIntoAliveClasses(ui2);
		assertTrue(new_instance);
	}
	
	@Test
	public void getInstanceClassIfAlive_Test(){
		//Should return null when the instance is not in the map.
		InstanceRegister ir = new InstanceRegister();
		UniqueInstance instance =  ir.getInstanceClassIfAlive("None", ui1);
		assertNull(instance);
	}
	

	@Test
	public void getInstanceClassIfAlive_Test2(){
		//Should return the instance when the instance is alive in the map.
		InstanceRegister ir = new InstanceRegister();
		String modelClassName = "org.elte.TxtUML";
		ui1.setModelClassName(modelClassName);
		ir.insertIntoAliveClasses(ui1);
		
		UniqueInstance instance = ir.getInstanceClassIfAlive(modelClassName, ui1);
		assertEquals(instance, ui1);
	}

	@Test
	public void removeFromAliveClasses_Test(){
		//Should return false when the instance was not in the map.
		InstanceRegister ir = new InstanceRegister();
		String modelClassName = "org.elte.TxtUML";
		ui1.setModelClassName(modelClassName);

		boolean wasThere = ir.removeFromAliveClasses(ui1);
		assertFalse(wasThere);		
	}
	

	@Test
	public void removeFromAliveClasses_Test2(){
		//Should return true when the instance was in the map.
		InstanceRegister ir = new InstanceRegister();
		String modelClassName = "org.elte.TxtUML";
		ui1.setModelClassName(modelClassName);

		ir.insertIntoAliveClasses(ui1);
		boolean wasThere = ir.removeFromAliveClasses(ui1);
		assertTrue(wasThere);
	}
}
