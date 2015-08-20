package hu.elte.txtuml.export.uml2.transform;

import static org.junit.Assert.*;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.api.model.*;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;

public class AssociationImporterTest {

	org.eclipse.uml2.uml.Model model;
	
	@Before
	public void setUp() throws Exception {
		model = UMLFactory.eINSTANCE.createModel();
        model.setName("TestModel");
        model.createOwnedClass("Class1", false);
        model.createOwnedClass("Class2", false);
        model.createOwnedClass("Class3", false);
	}

	@Test
	public void testImportAssociation() {
		//testing import of TestModel.Assoc1
		org.eclipse.uml2.uml.Association assoc = null;
		boolean exception=false;
		try {
			assoc = new AssociationImporter(TestModel.Assoc1.class, model).importAssociation();
		} catch (ImportException e) {
			exception = true;
		}
		assertTrue(exception);
		
		//testing import of TestModel.Assoc2
		exception=false;
		try {
			assoc = new AssociationImporter(TestModel.Assoc2.class, model).importAssociation();
		} catch (ImportException e) {
			exception = true;
		}
		assertTrue(exception);
		
		//testing import of TestModel.Assoc3
		exception=false;
		try {
			assoc = new AssociationImporter(TestModel.Assoc3.class, model).importAssociation();
		} catch (ImportException e) {
			exception = true;
		}
		
		assertFalse(exception);
		assertTrue( assoc != null);
		assertEquals(assoc.getName(),"Assoc3");
		assertEquals(assoc.getMemberEnds().size(), 2);
		
		for(Property end : assoc.getMemberEnds() ) //order is non-deterministic
		{
			assertTrue(end.getName().equals("End1") || end.getName().equals("End2"));
			if(end.getName().equals("End1"))
			{
				assertEquals(end.getType().getName(),"Class1");
				assertEquals(end.getLower(),1);
				assertEquals(end.getUpper(),1);
			}
			else
			{
				assertEquals(end.getName(),"End2");
				assertEquals(end.getType().getName(),"Class2");
				assertEquals(end.getLower(),0);
				assertEquals(end.getUpper(),org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED);
			}
		}
		
		//testing import of TestModel.Assoc4
		exception=false;
		try {
			assoc = new AssociationImporter(TestModel.Assoc4.class, model).importAssociation();
		} catch (ImportException e) {
			exception = true;
		}
		assertTrue(exception);
		
		//testing import of TestModel.Assoc5
		exception=false;
		try {
			assoc = new AssociationImporter(TestModel.Assoc5.class, model).importAssociation();
		} catch (ImportException e) {
			exception = true;
		}
		
		assertFalse(exception);
		assertTrue( assoc != null);
		assertEquals(assoc.getName(),"Assoc5");
		assertEquals(assoc.getMemberEnds().size(), 2);
		
		for(Property end : assoc.getMemberEnds() ) //order is non-deterministic
		{
			assertTrue(end.getName().equals("End1") || end.getName().equals("End2"));
			if(end.getName().equals("End1"))
			{
				assertEquals(end.getType().getName(),"Class2");
				assertEquals(end.getLower(),0);
				assertEquals(end.getUpper(),1);
			}
			else
			{
				assertEquals(end.getName(),"End2");
				assertEquals(end.getType().getName(),"Class3");
				assertEquals(end.getLower(),1);
				assertEquals(end.getUpper(),org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED);
			}
		}
		
		//testing import of TestModel.Assoc6
		exception=false;
		try {
			assoc = new AssociationImporter(TestModel.Assoc6.class, model).importAssociation();
		} catch (ImportException e) {
			exception = true;
		}
		
		assertFalse(exception);
		assertTrue( assoc != null);
		assertEquals(assoc.getName(),"Assoc6");
		assertEquals(assoc.getMemberEnds().size(), 2);
		
		for(Property end : assoc.getMemberEnds() ) //order is non-deterministic
		{
			assertTrue(end.getName().equals("End1") || end.getName().equals("End2"));
			if(end.getName().equals("End1"))
			{
				assertEquals(end.getType().getName(),"Class1");
				assertEquals(end.getLower(),0);
				assertEquals(end.getUpper(),1);
			}
			else
			{
				assertEquals(end.getName(),"End2");
				assertEquals(end.getType().getName(),"Class1");
				assertEquals(end.getLower(),1);
				assertEquals(end.getUpper(),org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED);
			}
		}
	}

	class TestModel extends Model
	{
		class Class1 extends ModelClass{}
		class Class2 extends ModelClass{}
		class Class3 extends ModelClass{}
		
		class Assoc1 extends Association
		{
		}
		
		class Assoc2 extends Association
		{
			class End1 extends One<Class1>{}
		}
		
		class Assoc3 extends Association
		{
			class End1 extends One<Class1>{}
			class End2 extends Many<Class2>{}
		}
		
		class Assoc4 extends Association
		{
			class End1 extends One<Class1>{}
			class End2 extends One<Class2>{}
			class End3 extends One<Class3>{}
		}
		
		class Assoc5 extends Association
		{
			class End1 extends MaybeOne<Class2>{}
			class End2 extends Some<Class3>{}
		}
		
		class Assoc6 extends Association
		{
			class End1 extends MaybeOne<Class1>{}
			class End2 extends Some<Class1>{}
		}
	
	}
	
}

