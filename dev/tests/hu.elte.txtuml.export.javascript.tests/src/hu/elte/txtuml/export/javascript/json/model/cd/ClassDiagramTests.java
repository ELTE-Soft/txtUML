package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

public class ClassDiagramTests {

	Model model;
	Class classA;
	Pair<List<String>, List<PrimitiveType>> primitives;

	@Before
	public void prepare() {
		model = UMLFactory.eINSTANCE.createModel();

		List<String> names = Arrays.asList("String", "Boolean", "Integer", "Real", "UnlimitedNatural");
		List<PrimitiveType> types = Arrays.asList(model.createOwnedPrimitiveType("String"),
				model.createOwnedPrimitiveType("Boolean"), model.createOwnedPrimitiveType("Integer"),
				model.createOwnedPrimitiveType("Real"), model.createOwnedPrimitiveType("UnlimitedNatural"));

		primitives = Pair.of(names, types);

		classA = model.createOwnedClass("A", false);
	}

	@Test
	public void testArgument() {
		// given
		BasicEList<String> paramNames = new BasicEList<>(Arrays.asList("a", "b", "c", "d", "e", "f"));

		BasicEList<Type> paramTypes = new BasicEList<Type>();
		paramTypes.add(classA);
		paramTypes.addAll(primitives.getSecond());

		List<String> paramTypeNames = new ArrayList<String>();
		paramTypeNames.add("A");
		paramTypeNames.addAll(primitives.getFirst());

		Operation op = classA.createOwnedOperation("function", (EList<String>) paramNames, (EList<Type>) paramTypes);

		// when
		List<Argument> instances = new ArrayList<>();
		op.getOwnedParameters().forEach(parameter -> {
			instances.add(new Argument(parameter));
		});

		// then
		IntStream.range(0, instances.size()).forEach(i -> {
			Argument instanceArgument = instances.get(i);

			Assert.assertEquals(paramNames.get(i), instanceArgument.getName());
			Assert.assertEquals(paramTypeNames.get(i), instanceArgument.getType());
		});
	}

	@Test
	public void testAttribute() {
		// given
		List<String> attrNames = Arrays.asList("a", "b", "c", "d", "e", "f");

		ArrayList<Type> attrTypes = new ArrayList<>();
		attrTypes.add(classA);
		attrTypes.addAll(primitives.getSecond());

		List<String> attrTypeNames = new ArrayList<>();
		attrTypeNames.add("A");
		attrTypeNames.addAll(primitives.getFirst());

		List<VisibilityKind> expectedVisibilities = Arrays.asList(VisibilityKind.PUBLIC_LITERAL,
				VisibilityKind.PROTECTED_LITERAL, VisibilityKind.PRIVATE_LITERAL, VisibilityKind.PACKAGE_LITERAL,
				VisibilityKind.PUBLIC_LITERAL, VisibilityKind.PROTECTED_LITERAL);

		List<Boolean> expectedStaticnesses = Arrays.asList(true, false, true, false, true, false);

		// when
		List<Attribute> instances = new ArrayList<>();
		for (int i = 0; i < attrNames.size(); ++i) {
			Property p = classA.createOwnedAttribute(attrNames.get(i), attrTypes.get(i));
			p.setVisibility(expectedVisibilities.get(i));
			p.setIsStatic(expectedStaticnesses.get(i));
			instances.add(new Attribute(p));
		}

		// then
		IntStream.range(0, instances.size()).forEach(i -> {
			Attribute instanceAttribute = instances.get(i);

			Assert.assertEquals(attrNames.get(i), instanceAttribute.getName());
			Assert.assertEquals(attrTypeNames.get(i), instanceAttribute.getType());
			Assert.assertEquals(expectedVisibilities.get(i), instanceAttribute.getVisibility());
			Assert.assertEquals(expectedStaticnesses.get(i), instanceAttribute.isStatic());
		});
	}

	@Test
	public void testAssociationEnd() {
		// given
		Class classB = model.createOwnedClass("B", false);
		List<String> names = Arrays.asList("a", "b", "c", "d");
		List<Boolean> compositions = Arrays.asList(true, false, false, true);
		List<Boolean> navigabilities = Arrays.asList(true, true, false, false);
		List<String> expectedMultiplicites = Arrays.asList("*", "2..*", "1..3", "2");
		List<VisibilityKind> expectedVisibilities = Arrays.asList(VisibilityKind.PUBLIC_LITERAL,
				VisibilityKind.PRIVATE_LITERAL, VisibilityKind.PROTECTED_LITERAL, VisibilityKind.PACKAGE_LITERAL);

		Association a = classA.createAssociation(true, AggregationKind.NONE_LITERAL, "b", 2, -1, classB, true,
				AggregationKind.COMPOSITE_LITERAL, "a", 0, -1);
		Association b = classB.createAssociation(false, AggregationKind.COMPOSITE_LITERAL, "d", 2, 2, classA, false,
				AggregationKind.NONE_LITERAL, "c", 1, 3);

		a.getMemberEnd("a", classA).setVisibility(VisibilityKind.PUBLIC_LITERAL);
		a.getMemberEnd("b", classB).setVisibility(VisibilityKind.PRIVATE_LITERAL);
		b.getMemberEnd("c", classB).setVisibility(VisibilityKind.PROTECTED_LITERAL);
		b.getMemberEnd("d", classA).setVisibility(VisibilityKind.PACKAGE_LITERAL);

		List<Property> props = Arrays.asList(a.getMemberEnd("a", classA), a.getMemberEnd("b", classB),
				b.getMemberEnd("c", classB), b.getMemberEnd("d", classA));

		// when
		List<AssociationEnd> instances = new ArrayList<>();
		props.forEach(property -> {
			instances.add(new AssociationEnd(property));
		});

		// then
		IntStream.range(0, instances.size()).forEach(i -> {
			AssociationEnd instanceAssocEnd = instances.get(i);

			Assert.assertEquals(names.get(i), instanceAssocEnd.getName());
			Assert.assertEquals(compositions.get(i), instanceAssocEnd.isComposition());
			Assert.assertEquals(navigabilities.get(i), instanceAssocEnd.isNavigable());
			Assert.assertEquals(expectedMultiplicites.get(i), instanceAssocEnd.getMultiplicity());
			Assert.assertEquals(expectedVisibilities.get(i), instanceAssocEnd.getVisibility());
		});
	}

	@Test
	public void testClassAttributeLink() throws UnexpectedEndException {
		// given
		Class classB = model.createOwnedClass("B", false);
		LineAssociation lab = new LineAssociation("package.AB", "package.A", "package.B");
		LineAssociation laa = new LineAssociation("package.AA", "package.A", "package.A");

		Association ab = classA.createAssociation(true, AggregationKind.NONE_LITERAL, "b", 1, 1, classB, true,
				AggregationKind.NONE_LITERAL, "a", 2, 2);
		ab.setName("AB");

		Association aa = classA.createAssociation(true, AggregationKind.NONE_LITERAL, "a2", 3, 3, classA, true,
				AggregationKind.NONE_LITERAL, "a1", 4, 4);

		aa.setName("AA");

		// when
		ClassAttributeLink calab = new ClassAttributeLink(lab, ab, classA, classB);
		ClassAttributeLink calaa = new ClassAttributeLink(laa, aa, classA, classA);

		// then
		Assert.assertEquals("package.AB", calab.getId());
		Assert.assertEquals("package.AA", calaa.getId());

		Assert.assertEquals("AB", calab.getName());
		Assert.assertEquals("AA", calaa.getName());

		Assert.assertEquals(AssociationType.normal, calab.getType());
		Assert.assertEquals(AssociationType.normal, calaa.getType());

		Assert.assertEquals("package.A", calab.getFromID());
		Assert.assertEquals("package.A", calaa.getFromID());

		Assert.assertEquals("package.B", calab.getToID());
		Assert.assertEquals("package.A", calaa.getToID());

		Assert.assertEquals("a", calab.getFrom().getName());
		Assert.assertEquals("a1", calaa.getFrom().getName());

		Assert.assertEquals("b", calab.getTo().getName());
		Assert.assertEquals("a2", calaa.getTo().getName());
	}

	@Test
	public void testClassLink() {
		// given
		LineAssociation la = new LineAssociation("package.AB", "package.A", "package.B");
		la.setType(AssociationType.generalization);

		// when
		ClassLink cl = new ClassLink(la);

		// then
		Assert.assertEquals("package.AB", cl.getId());
		Assert.assertEquals("package.A", cl.getFromID());
		Assert.assertEquals("package.B", cl.getToID());
		Assert.assertEquals(AssociationType.generalization, cl.getType());
	}

	@Test
	public void testClassNode() {
		// given
		List<String> expectedAttrNames = Arrays.asList("attr1", "attr2");
		for (String attr : expectedAttrNames) {
			classA.createOwnedAttribute(attr, primitives.getSecond().get(0));
		}

		List<String> expectedOpNames = Arrays.asList("op1", "op2");
		BasicEList<String> emptyStrings = new BasicEList<String>();
		BasicEList<Type> emptyTypes = new BasicEList<Type>();
		for (String op : expectedOpNames) {
			classA.createOwnedOperation(op, emptyStrings, emptyTypes);
		}

		Class classB = model.createOwnedClass("B", true);
		RectangleObject rectA = new RectangleObject("package.A");
		rectA.setPosition(new Point(1, 2));
		rectA.setWidth(3);
		rectA.setHeight(4);

		RectangleObject rectB = new RectangleObject("package.B");
		rectB.setPosition(new Point(5, 6));
		rectB.setWidth(7);
		rectB.setHeight(8);

		// when
		ClassNode cnA = new ClassNode(classA, rectA.getName());
		ClassNode cnB = new ClassNode(classB, rectB.getName());

		List<String> opNames = new ArrayList<String>();
		for (MemberOperation op : cnA.getOperations()) {
			opNames.add(op.getName());
		}

		List<String> attrNames = new ArrayList<String>();
		for (Attribute attr : cnA.getAttributes()) {
			attrNames.add(attr.getName());
		}

		Rectangle layoutTest = new Rectangle(1, 2, 3, 4);
		cnA.setLayout(layoutTest);

		// then
		Assert.assertEquals("package.A", cnA.getId());
		Assert.assertEquals("package.B", cnB.getId());

		Assert.assertEquals("A", cnA.getName());
		Assert.assertEquals("B", cnB.getName());

		Assert.assertEquals(CDNodeType.CLASS, cnA.getType());
		Assert.assertEquals(CDNodeType.ABSTRACT_CLASS, cnB.getType());

		Assert.assertEquals(1, cnA.getPosition().getX());
		Assert.assertEquals(2, cnA.getPosition().getY());

		Assert.assertEquals(3, cnA.getWidth().intValue());
		Assert.assertEquals(4, cnA.getHeight().intValue());

		Assert.assertTrue(cnB.getAttributes().isEmpty());
		Assert.assertTrue(cnB.getOperations().isEmpty());

		Assert.assertArrayEquals(expectedAttrNames.toArray(), attrNames.toArray());
		Assert.assertArrayEquals(expectedOpNames.toArray(), opNames.toArray());
	}

	@Test
	public void testMemberOperation() {
		// given
		List<String> opNames = Arrays.asList("a", "b", "c", "d");
		List<VisibilityKind> expectedVisibilities = Arrays.asList(VisibilityKind.PUBLIC_LITERAL,
				VisibilityKind.PROTECTED_LITERAL, VisibilityKind.PRIVATE_LITERAL, VisibilityKind.PACKAGE_LITERAL);
		List<Boolean> expectedAbstractnesses = Arrays.asList(true, false, true, false);
		List<Boolean> expectedStaticnesses = Arrays.asList(true, true, false, false);
		List<Type> returnTypes = Arrays.asList(null, classA, primitives.getSecond().get(0), null);
		List<String> returnTypeNames = Arrays.asList(null, "A", primitives.getFirst().get(0), null);

		List<MemberOperation> instances = new ArrayList<>();
		List<List<String>> expectedArgNames = new ArrayList<>();
		List<List<String>> expectedArgTypeNames = new ArrayList<>();
		List<Operation> operations = new ArrayList<>();
		for (int i = 0; i < opNames.size(); ++i) {
			EList<Type> argTypes = new BasicEList<>(primitives.getSecond().subList(0, i));
			List<String> argTypeNames = primitives.getFirst().subList(0, i);
			List<String> argNames = opNames.subList(0, i);
			expectedArgNames.add(argNames);
			expectedArgTypeNames.add(argTypeNames);

			Operation op = classA.createOwnedOperation(opNames.get(i), new BasicEList<String>(argNames), argTypes);
			op.setVisibility(expectedVisibilities.get(i));
			op.setIsAbstract(expectedAbstractnesses.get(i));
			op.setIsStatic(expectedStaticnesses.get(i));

			if (returnTypes.get(i) != null) {
				Parameter ret = op.createOwnedParameter("return", returnTypes.get(i));
				ret.setDirection(ParameterDirectionKind.RETURN_LITERAL);
			}

			operations.add(op);
		}

		// when
		operations.forEach(op -> {
			instances.add(new MemberOperation(op));
		});

		// then
		IntStream.range(0, instances.size()).forEach(i -> {
			MemberOperation instanceMemberOperation = instances.get(i);

			List<String> actualArgNames = instanceMemberOperation.getArgs().stream().map(a -> a.getName())
					.collect(Collectors.toList());
			List<String> actualArgTypeNames = instanceMemberOperation.getArgs().stream().map(a -> a.getType())
					.collect(Collectors.toList());
			List<String> argNames = expectedArgNames.get(i);
			List<String> argTypeNames = expectedArgTypeNames.get(i);

			Assert.assertEquals(opNames.get(i), instanceMemberOperation.getName());
			Assert.assertEquals(expectedVisibilities.get(i), instanceMemberOperation.getVisibility());
			Assert.assertEquals(expectedAbstractnesses.get(i), instanceMemberOperation.isAbstract());
			Assert.assertEquals(expectedStaticnesses.get(i), instanceMemberOperation.isStatic());
			Assert.assertEquals(returnTypeNames.get(i), instanceMemberOperation.getReturnType());
			Assert.assertArrayEquals(actualArgNames.toArray(), argNames.toArray());
			Assert.assertArrayEquals(actualArgTypeNames.toArray(), argTypeNames.toArray());
		});
	}
}
