package hu.elte.txtuml.utils.tests;

import hu.elte.txtuml.utils.MultiMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiMapTest {

	MultiMap<Integer, String> multimap;
	
	@Before
	public void setUp() throws Exception {
		multimap = new MultiMap<Integer, String>();
	}

	@After
	public void tearDown() throws Exception {
		multimap = null;
	}

	@Test
	public void testClear() {
		multimap.putAll(3, Arrays.asList("one", "two", "three"));
		multimap.clear();
		Assert.assertTrue(multimap.isEmpty());
	}

	@Test
	public void testContainsKey() {
		Assert.assertFalse(multimap.containsKey(1));
		multimap.put(1, "one");
		Assert.assertTrue(multimap.containsKey(1));
		
		multimap.removeValue("one");
		Assert.assertTrue(multimap.containsKey(1));
		
		multimap.remove(1);
		Assert.assertFalse(multimap.containsKey(1));
	}

	@Test
	public void testContainsValue() {
		Assert.assertFalse(multimap.containsValue("one"));
		multimap.put(1, "one");
		Assert.assertTrue(multimap.containsValue("one"));
	}

	@Test
	public void testGet() {
		multimap.putAll(3, Arrays.asList("one", "two", "three"));
		HashSet<String> values = multimap.get(3);
		
		Assert.assertTrue(values.contains("one"));
		Assert.assertTrue(values.contains("two"));
		Assert.assertTrue(values.contains("three"));
		
		Assert.assertEquals(3, values.size());
	}

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(multimap.isEmpty());
		
		multimap.put(1, "one");		
		Assert.assertFalse(multimap.isEmpty());
		
		multimap.remove(1);
		Assert.assertTrue(multimap.isEmpty());
		
		multimap.put(2, "two");
		multimap.removeValue("two");
		Assert.assertFalse(multimap.isEmpty());
	}

	@Test
	public void testKeySet() {
		Assert.assertEquals(0, multimap.keySet().size());
		multimap.put(1, "one");
		Assert.assertEquals(1, multimap.keySet().size());
		Assert.assertTrue(multimap.keySet().contains(1));
	}

	@Test
	public void testPutOneElement() {
		multimap.put(1, "one");
		Assert.assertTrue(multimap.get(1).contains("one"));
	}
	
	@Test
	public void testPutNull() {
		multimap.put(1, null);
		Assert.assertTrue(multimap.get(1).contains(null));
	}
	
	@Test
	public void testPutElementTwice() {
		multimap.put(1, "putmetwice");
		multimap.put(1, "putmetwice");
		Assert.assertEquals(1, multimap.size());
		Assert.assertTrue(multimap.get(1).contains("putmetwice"));
	}
	
	@Test
	public void testPutElementsWithDifferentKeys() {
		multimap.put(1, "one");
		multimap.put(2, "two");
		Assert.assertTrue(multimap.get(1).contains("one"));
		Assert.assertTrue(multimap.get(2).contains("two"));
	}
	
	@Test
	public void testPutElementsWithSameKeys() {
		multimap.put(1, "one");
		multimap.put(1, "anotherOne");
		multimap.put(1, "thirdOne");
		Assert.assertTrue(multimap.get(1).contains("one"));
		Assert.assertTrue(multimap.get(1).contains("anotherOne"));
		Assert.assertTrue(multimap.get(1).contains("thirdOne"));
	}

	@Test
	public void testPutAll() {
		multimap.putAll(5, Arrays.asList("one", "two", "three"));
		Assert.assertEquals(3, multimap.get(5).size());
		Assert.assertTrue(multimap.get(5).contains("one"));
		Assert.assertTrue(multimap.get(5).contains("two"));
		Assert.assertTrue(multimap.get(5).contains("three"));
	}

	@Test
	public void testRemove() {
		multimap.put(1, "one");
		multimap.put(2, "two");
		multimap.remove(1);
		Assert.assertNull(multimap.get(1));
		Assert.assertTrue(multimap.get(2).contains("two"));
	}

	@Test
	public void testRemoveValue() {
		multimap.put(1, "one");
		multimap.put(2, "two");
		multimap.removeValue("one");
		Assert.assertNotNull(multimap.get(1));
		Assert.assertEquals(0, multimap.get(1).size());
		Assert.assertTrue(multimap.get(2).contains("two"));
	}

	@Test
	public void testSize() {
		Assert.assertEquals(0, multimap.size());
		
		multimap.put(1, "one");
		Assert.assertEquals(1, multimap.size());
		
		multimap.put(1, "anotherone");
		Assert.assertEquals(1, multimap.size());
		
		multimap.put(12, "twelve");
		Assert.assertEquals(2, multimap.size());
		
		multimap.remove(12);
		Assert.assertEquals(1, multimap.size());
	}

	@Test
	public void testValues() {
		multimap.putAll(1, Arrays.asList("11", "12", "13"));
		multimap.putAll(2, Arrays.asList("21", "22", "23"));
		multimap.putAll(3, Arrays.asList("31", "32", "33"));
		Collection<HashSet<String>> valuescollection = multimap.values();
		Iterator<HashSet<String>> it = valuescollection.iterator();
		
		HashSet<String> firstvalues = it.next();
		Assert.assertTrue(firstvalues.contains("11"));
		Assert.assertTrue(firstvalues.contains("12"));
		Assert.assertTrue(firstvalues.contains("13"));
		
		HashSet<String> secondvalues = it.next();
		Assert.assertTrue(secondvalues.contains("21"));
		Assert.assertTrue(secondvalues.contains("22"));
		Assert.assertTrue(secondvalues.contains("23"));
		
		HashSet<String> thirdvalues = it.next();
		Assert.assertTrue(thirdvalues.contains("31"));
		Assert.assertTrue(thirdvalues.contains("32"));
		Assert.assertTrue(thirdvalues.contains("33"));
		
		Assert.assertEquals(3, multimap.size());
		
	}

}
