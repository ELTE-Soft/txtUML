package hu.elte.txtuml.layout.visualizer.model;

import static org.junit.Assert.*;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.model.Direction;

import org.junit.Before;
import org.junit.Test;

public class DirectionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void OppositeTest() {
		assertEquals(Direction.south, Direction.opposite(Direction.north));
		assertEquals(Direction.north, Direction.opposite(Direction.south));
		assertEquals(Direction.east, Direction.opposite(Direction.west));
		assertEquals(Direction.west, Direction.opposite(Direction.east));
	}
	
	@Test
	public void FromIntTest() {
		try {
			assertEquals(Direction.north, Direction.fromInteger(0));
			assertEquals(Direction.east, Direction.fromInteger(1));
			assertEquals(Direction.south, Direction.fromInteger(2));
			assertEquals(Direction.west, Direction.fromInteger(3));
		} catch (ConversionException e) {
			fail("Test failed: " + e.getMessage());
		}
	}

}
