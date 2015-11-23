package hu.elte.txtuml.layout.visualizer.tests.statements;

import static org.junit.Assert.*;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

import org.junit.Assert;
import org.junit.Test;

public class StatementTest {
	
	@Test
	public void equalsTest() {
		Statement north = new Statement(StatementType.north, "A", "B");
		Statement north2 = new Statement(StatementType.north, "A", "B");
		
		Assert.assertEquals(north, north2);
	}
	
	@Test
	public void oppositeTest() {

		Statement north = new Statement(StatementType.north, "A", "B");
		Statement east = new Statement(StatementType.east, "A", "B");
		Statement south = new Statement(StatementType.south, "B", "A");
		Statement west = new Statement(StatementType.west, "B", "A");
		
		try {
			Assert.assertEquals(north, Statement.opposite(south));
			Assert.assertEquals(south, Statement.opposite(north));
			Assert.assertEquals(east, Statement.opposite(west));
			Assert.assertEquals(west, Statement.opposite(east));
		} catch (InternalException | UnknownStatementException e) {
			fail("Test failed: " + e.getMessage());
		}
		
		Statement above = new Statement(StatementType.above, "A", "B");
		Statement right = new Statement(StatementType.right, "A", "B");
		Statement below = new Statement(StatementType.below, "B", "A");
		Statement left = new Statement(StatementType.left, "B", "A");
		
		try {
			Assert.assertEquals(above, Statement.opposite(below));
			Assert.assertEquals(below, Statement.opposite(above));
			Assert.assertEquals(right, Statement.opposite(left));
			Assert.assertEquals(left, Statement.opposite(right));
		} catch (InternalException | UnknownStatementException e) {
			fail("Test failed: " + e.getMessage());
		}
		
		Statement hor = new Statement(StatementType.horizontal, "A", "B");
		Statement ver = new Statement(StatementType.vertical, "A", "B");
		Statement prio = new Statement(StatementType.priority, "A", "10");
		Statement phan = new Statement(StatementType.phantom, "A");
		Statement corr = new Statement(StatementType.corridorsize, "1.0");
		Statement over = new Statement(StatementType.overlaparrange, "few");
		
		try {
			Statement.opposite(hor);
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		} catch(UnknownStatementException e){
			assertTrue(true);
		}
		
		try {
			Statement.opposite(ver);
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		} catch(UnknownStatementException e){
			assertTrue(true);
		}
		
		try {
			Statement.opposite(prio);
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		} catch(UnknownStatementException e){
			assertTrue(true);
		}
		
		try{
			Statement.opposite(phan);
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		} catch(UnknownStatementException e){
			assertTrue(true);
		}
		
		try {
			Statement.opposite(corr);
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		} catch (UnknownStatementException e) {
			assertTrue(true);
		}

		try {
			Statement.opposite(over);
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		} catch (UnknownStatementException e) {
			assertTrue(true);
		}
	}

}
