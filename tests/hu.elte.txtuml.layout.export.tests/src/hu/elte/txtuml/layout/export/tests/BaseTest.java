package hu.elte.txtuml.layout.export.tests;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.problems.ProblemReporter;
import hu.elte.txtuml.layout.export.problems.Utils;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class BaseTest {

	private DiagramExportationReport expectedReport;
	protected ProblemReporter problems;
	protected DiagramType type;
	protected Set<RectangleObject> nodes;
	protected Set<Link> links;
	protected List<Statement> statements;

	@Before
	public void initialize() {
		expectedReport = new DiagramExportationReport();
		problems = new ProblemReporter(expectedReport);
		type = DiagramType.Unknown;
		nodes = new HashSet<>();
		links = new HashSet<>();
		statements = new ArrayList<>();
	}

	protected final void testForFailure(Class<? extends Diagram> diag) {

		DiagramExporter exporter = DiagramExporter.create(diag);
		DiagramExportationReport report = exporter.export();

		assertEqualsAsMultisets(expectedReport.getErrors(), report.getErrors());
		assertEqualsAsMultisets(expectedReport.getWarnings(),
				report.getWarnings());
	}

	protected final void testForSuccess(Class<? extends Diagram> diag) {

		DiagramExporter exporter = DiagramExporter.create(diag);
		DiagramExportationReport report = exporter.export();

		Assert.assertTrue(report.isSuccessful());
		Assert.assertEquals(0, report.getErrorCount());
		Assert.assertEquals(0, report.getWarningCount());
		Assert.assertEquals(type, report.getType());

		assertCollectionEquals(nodes, report.getNodes());
		assertCollectionEquals(links,
				report.getLinks().stream().map(Link::fromLineAssociation)
						.collect(Collectors.toSet()));
		assertEqualsAsMultisets(statements, report.getStatements());
	}

	protected final void node(Class<?> node) {
		nodes.add(new RectangleObject(Utils.classAsString(node)));
	}

	protected final void directedLink(Class<?> link, Class<?> source,
			Class<?> target) {
		directedLink(link, source, target, AssociationType.normal);
	}

	protected final void directedLink(Class<?> link, Class<?> source,
			Class<?> target, AssociationType type) {
		links.add(new Link(true, Utils.classAsString(link), Utils
				.classAsString(source), Utils.classAsString(target), type));
	}

	protected final void undirectedLink(Class<?> link, Class<?> source,
			Class<?> target) {
		undirectedLink(link, source, target, AssociationType.normal);
	}

	protected final void undirectedLink(Class<?> link, Class<?> source,
			Class<?> target, AssociationType type) {
		links.add(new Link(false, Utils.classAsString(link), Utils
				.classAsString(source), Utils.classAsString(target), type));
	}

	protected void generalization(Class<?> base, Class<?> derived) {
		links.add(new Link(true, Utils.classAsString(base) + "#"
				+ Utils.classAsString(derived), Utils.classAsString(base),
				Utils.classAsString(derived), AssociationType.generalization));
	}

	protected final void statement(StatementType type, Object... params) {
		String[] stringParams = new String[params.length];
		for (int i = 0; i < params.length; ++i) {
			if (params[i] instanceof Class<?>) {
				stringParams[i] = Utils.classAsString((Class<?>) params[i]);
			} else {
				stringParams[i] = params[i].toString();
			}
		}

		statements.add(new Statement(type, stringParams));
	}

	protected static <T> void assertEqualsAsMultisets(
			Collection<T> expectedCollection, Collection<T> actualCollection) {

		Multiset<T> expecteds = HashMultiset.create();
		Multiset<T> actuals = HashMultiset.create();

		expecteds.addAll(expectedCollection);
		actuals.addAll(actualCollection);

		assertCollectionEquals(expecteds, actuals);
	}

	protected static <T> void assertCollectionEquals(Collection<T> expecteds,
			Collection<T> actuals) {
		if (!expecteds.equals(actuals)) {
			System.err.println("EXPECTED");
			prettyPrint(expecteds, System.err);
			System.err.println("ACTUAL");
			prettyPrint(actuals, System.err);
			Assert.assertTrue(false);
		}
	}

	protected static void prettyPrint(Collection<?> coll, PrintStream on) {
		coll.stream().map(Object::toString).sorted().forEach(on::println);
	}

	protected static class Link {

		private final boolean directed;
		private final String id;
		private final String source;
		private final String target;
		private final AssociationType type;

		Link(boolean directed, String id, String source, String target,
				AssociationType type) {
			if (id == null || source == null || target == null || type == null) {
				throw new IllegalArgumentException();
			}

			this.directed = directed;
			this.id = id;
			this.source = source;
			this.target = target;
			this.type = type;
		}

		@Override
		public int hashCode() {
			return id.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Link)) {
				return false;
			}
			Link other = (Link) obj;

			if (id.equals(other.id) && type == other.type) {

				if (source.equals(other.source) && target.equals(other.target)) {
					return true;
				} else if (!directed || !other.directed) {
					if (source.equals(other.target)
							&& target.equals(other.source)) {
						return true;
					}
				}

			}
			return true;
		}

		@Override
		public String toString() {
			return id + " [ " + source + " --> " + target + " (" + type + ")]";
		}

		static Link fromLineAssociation(LineAssociation lineAssociation) {
			return new Link(true, lineAssociation.getId(),
					lineAssociation.getFrom(), lineAssociation.getTo(),
					lineAssociation.getType());
		}
	}

}
