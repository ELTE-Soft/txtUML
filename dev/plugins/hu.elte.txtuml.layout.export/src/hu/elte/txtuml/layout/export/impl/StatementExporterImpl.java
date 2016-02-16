package hu.elte.txtuml.layout.export.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.BottomMost;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.LeftMost;
import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Priority;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.api.layout.RightMost;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.South;
import hu.elte.txtuml.api.layout.TopMost;
import hu.elte.txtuml.api.layout.West;
import hu.elte.txtuml.api.layout.containers.AboveContainer;
import hu.elte.txtuml.api.layout.containers.BelowContainer;
import hu.elte.txtuml.api.layout.containers.ColumnContainer;
import hu.elte.txtuml.api.layout.containers.DiamondContainer;
import hu.elte.txtuml.api.layout.containers.EastContainer;
import hu.elte.txtuml.api.layout.containers.LeftContainer;
import hu.elte.txtuml.api.layout.containers.NorthContainer;
import hu.elte.txtuml.api.layout.containers.PriorityContainer;
import hu.elte.txtuml.api.layout.containers.RightContainer;
import hu.elte.txtuml.api.layout.containers.RowContainer;
import hu.elte.txtuml.api.layout.containers.ShowContainer;
import hu.elte.txtuml.api.layout.containers.SouthContainer;
import hu.elte.txtuml.api.layout.containers.WestContainer;
import hu.elte.txtuml.layout.export.elementinfo.ConcreteElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.interfaces.StatementExporter;
import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.layout.export.problems.ProblemReporter;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

/**
 * Default implementation for {@link StatementExporter}.
 */
public class StatementExporterImpl implements StatementExporter {

	private final StatementList statements;
	private final ElementExporter elementExporter;
	private final ProblemReporter problemReporter;
	private final Map<Class<? extends Annotation>, NodeGroupInfo> mostMap;

	public StatementExporterImpl(ElementExporter elementExporter,
			ProblemReporter problemReporter) {
		this.statements = StatementList.create();
		this.elementExporter = elementExporter;
		this.problemReporter = problemReporter;
		this.mostMap = new HashMap<Class<? extends Annotation>, NodeGroupInfo>();
	}

	@Override
	public StatementList getStatements() {
		return statements;
	}

	// public statement exporters

	@Override
	public void exportAlignment(NodeGroupInfo info) {
		StatementType type;
		switch (info.getAlignment()) {
		case LeftToRight:
			type = StatementType.west;
			break;

		case RightToLeft:
			type = StatementType.east;
			break;

		case TopToBottom:
			type = StatementType.north;
			break;

		case BottomToTop:
			type = StatementType.south;
			break;

		default:
			type = StatementType.unknown;
		}

		exportAlignmentWithGivenStatement(info, type);
	}

	@Override
	public void exportAbove(Above annot) {
		exportAdjacencyStatement(StatementType.above, annot.val(), annot.from());
	}

	@Override
	public void exportBelow(Below annot) {
		exportAdjacencyStatement(StatementType.below, annot.val(), annot.from());
	}

	@Override
	public void exportRight(Right annot) {
		exportAdjacencyStatement(StatementType.right, annot.val(), annot.from());
	}

	@Override
	public void exportLeft(Left annot) {
		exportAdjacencyStatement(StatementType.left, annot.val(), annot.from());
	}

	@Override
	public void exportNorth(North annot) {
		exportCardinalStatement(StatementType.north, annot.val(), annot.from(),
				annot.end());
	}

	@Override
	public void exportSouth(South annot) {
		exportCardinalStatement(StatementType.south, annot.val(), annot.from(),
				annot.end());
	}

	@Override
	public void exportEast(East annot) {
		exportCardinalStatement(StatementType.east, annot.val(), annot.from(),
				annot.end());
	}

	@Override
	public void exportWest(West annot) {
		exportCardinalStatement(StatementType.west, annot.val(), annot.from(),
				annot.end());
	}

	@Override
	public void exportTopMost(TopMost annot) {
		processMostStatement(TopMost.class, annot.value());
	}

	@Override
	public void exportBottomMost(BottomMost annot) {
		processMostStatement(BottomMost.class, annot.value());
	}

	@Override
	public void exportLeftMost(LeftMost annot) {
		processMostStatement(LeftMost.class, annot.value());
	}

	@Override
	public void exportRightMost(RightMost annot) {
		processMostStatement(RightMost.class, annot.value());
	}

	@Override
	public void exportPriority(Priority annot) {
		if (annot.val().length == 0) {
			problemReporter.priorityStatementWithEmptyArguments(annot.val(),
					annot.prior());
			return;
		}

		for (Class<?> link : annot.val()) {
			try {
				ElementInfo info = elementExporter.exportElement(link);

				if (info instanceof LinkInfo) {
					statements.addNew(StatementType.priority, info.toString(),
							Integer.toString(annot.prior()));
				} else if (info instanceof LinkGroupInfo) {
					for (LinkInfo innerLink : ((LinkGroupInfo) info)
							.getAllLinks().values()) {
						statements.addNew(StatementType.priority,
								innerLink.toString(),
								Integer.toString(annot.prior()));
					}
				}
			} catch (ElementExportationException e) {
				problemReporter.priorityStatementWithInvalidElement(link,
						annot.val(), annot.prior());
			}
		}
	}

	@Override
	public void exportShow(Show annot) {
		if (annot.value().length == 0) {
			problemReporter.sugarStatementWithEmptyArguments("show");
			return;
		}

		for (Class<?> element : annot.value()) {
			try {
				elementExporter.exportConcreteElement(element);
			} catch (ElementExportationException e) {
				problemReporter.showStatementWithInvalidElement(element,
						annot.value());
			}
		}
	}

	@Override
	public void exportColumn(Column annot) {
		exportLineStatement(StatementType.above, annot.value());
	}

	@Override
	public void exportRow(Row annot) {
		exportLineStatement(StatementType.left, annot.value());
	}

	@Override
	public void exportDiamond(Diamond annot) {
		try {
			NodeInfo top = elementExporter.exportNode(annot.top());
			NodeInfo right = elementExporter.exportNode(annot.right());
			NodeInfo bottom = elementExporter.exportNode(annot.bottom());
			NodeInfo left = elementExporter.exportNode(annot.left());

			NodeInfo phantom = elementExporter.createPhantom();

			statements.addNew(StatementType.above, top.toString(),
					phantom.toString());
			statements.addNew(StatementType.right, right.toString(),
					phantom.toString());
			statements.addNew(StatementType.below, bottom.toString(),
					phantom.toString());
			statements.addNew(StatementType.left, left.toString(),
					phantom.toString());
		} catch (ElementExportationException e) {
			problemReporter.diamondStatementExportationFailed(annot.top(),
					annot.right(), annot.bottom(), annot.left());
		}
	}
	
	@Override
	public void exportCorridorRatio(hu.elte.txtuml.api.layout.Spacing annot) {
		statements.addNew(StatementType.corridorsize, Double.toString(annot.value()));
	}

	// public statement container exporters

	@Override
	public void exportAboveContainer(AboveContainer annot) {
		for (Above a : annot.value()) {
			exportAbove(a);
		}
	}

	@Override
	public void exportBelowContainer(BelowContainer annot) {
		for (Below a : annot.value()) {
			exportBelow(a);
		}
	}

	@Override
	public void exportRightContainer(RightContainer annot) {
		for (Right a : annot.value()) {
			exportRight(a);
		}
	}

	@Override
	public void exportLeftContainer(LeftContainer annot) {
		for (Left a : annot.value()) {
			exportLeft(a);
		}
	}

	@Override
	public void exportNorthContainer(NorthContainer annot) {
		for (North a : annot.value()) {
			exportNorth(a);
		}
	}

	@Override
	public void exportSouthContainer(SouthContainer annot) {
		for (South a : annot.value()) {
			exportSouth(a);
		}
	}

	@Override
	public void exportEastContainer(EastContainer annot) {
		for (East a : annot.value()) {
			exportEast(a);
		}
	}

	@Override
	public void exportWestContainer(WestContainer annot) {
		for (West a : annot.value()) {
			exportWest(a);
		}
	}

	@Override
	public void exportPriorityContainer(PriorityContainer annot) {
		for (Priority a : annot.value()) {
			exportPriority(a);
		}
	}

	@Override
	public void exportShowContainer(ShowContainer annot) {
		for (Show a : annot.value()) {
			exportShow(a);
		}
	}

	@Override
	public void exportColumnContainer(ColumnContainer annot) {
		for (Column a : annot.value()) {
			exportColumn(a);
		}
	}

	@Override
	public void exportRowContainer(RowContainer annot) {
		for (Row a : annot.value()) {
			exportRow(a);
		}
	}

	@Override
	public void exportDiamondContainer(DiamondContainer annot) {
		for (Diamond a : annot.value()) {
			exportDiamond(a);
		}
	}

	@Override
	public void resolveMosts() {
		NodeGroupInfo topMostInfo = mostMap.get(TopMost.class);
		if (topMostInfo != null) {
			exportMostStatement(StatementType.north, topMostInfo);
		}

		NodeGroupInfo bottomMostInfo = mostMap.get(BottomMost.class);
		if (bottomMostInfo != null) {
			exportMostStatement(StatementType.south, bottomMostInfo);
		}

		NodeGroupInfo rightMostInfo = mostMap.get(RightMost.class);
		if (rightMostInfo != null) {
			exportMostStatement(StatementType.east, rightMostInfo);
		}

		NodeGroupInfo leftMostInfo = mostMap.get(LeftMost.class);
		if (leftMostInfo != null) {
			exportMostStatement(StatementType.west, leftMostInfo);
		}
	}

	@Override
	public void exportPhantoms() {
		for (NodeInfo phantom : elementExporter.getPhantoms()) {
			statements.addNew(StatementType.phantom, phantom.toString());
		}
	}

	// private exporter methods

	private void exportLineStatement(StatementType type, Class<?>[] nodes) {
		if (nodes.length == 0) {
			problemReporter
					.sugarStatementWithEmptyArguments(type == StatementType.above ? "column"
							: "row");
			return;
		}

		try {
			NodeGroupInfo info = elementExporter
					.exportAnonymousNodeGroup(nodes);
			exportAlignmentWithGivenStatement(info, type);
		} catch (ElementExportationException e) {
			problemReporter.lineStatementExportationFailed(
					type == StatementType.above ? "column" : "row", nodes);

		}
	}

	private void exportAdjacencyStatement(StatementType type, Class<?> val,
			Class<?> from) {
		try {
			NodeInfo valInfo = elementExporter.exportNode(val);
			NodeInfo fromInfo = elementExporter.exportNode(from);
			statements.addNew(type, valInfo.toString(), fromInfo.toString());
		} catch (ElementExportationException e) {
			problemReporter
					.adjacencyStatementExportationFailed(type, val, from);
		}

	}

	private void exportCardinalStatement(StatementType type, Class<?>[] val,
			Class<?>[] from, LinkEnd end) {// XXX
		if (val.length == 0 || from.length == 0) {
			problemReporter.cardinalStatementWithEmptyArguments(type, val,
					from, end);
			return;
		}

		// type(link, node, [linkend])
		if (val.length == 1 && from.length == 1) {
			try {
				LinkInfo valInfo = elementExporter.exportLink(val[0]);
				NodeInfo fromInfo = elementExporter.exportNode(from[0]);
				newStatementWithLinkEndCheck(type, valInfo.toString(),
						fromInfo.toString(), end);
				return;
			} catch (ElementExportationException e) {
				// Check other options.
			}
		}

		try {
			for (Class<?> valClass : val) {
				ElementInfo valInfo = elementExporter.exportElement(valClass);

				for (Class<?> fromClass : from) {
					ElementInfo fromInfo = elementExporter
							.exportElement(fromClass);
					exportCardinalStatement(type, valInfo, fromInfo);
				}
			}
		} catch (ElementExportationException e) {
			problemReporter.cardinalStatementExportationFailed(type, val, from,
					end);
		}
	}

	private void exportCardinalStatement(StatementType type,
			ElementInfo valInfo, ElementInfo fromInfo)
			throws ElementExportationException {

		// type(node, node) || type(link, node)
		// not sure if the latter is necessary
		if (valInfo instanceof ConcreteElementInfo
				&& fromInfo instanceof NodeInfo) {
			statements.addNew(type, valInfo.toString(), fromInfo.toString());
			return;
		}

		// type(linkGroup, node)
		if (valInfo instanceof LinkGroupInfo && fromInfo instanceof NodeInfo) {
			for (LinkInfo innerValLinkInfo : ((LinkGroupInfo) valInfo)
					.getAllLinks().values()) {
				statements.addNew(type, innerValLinkInfo.toString(),
						fromInfo.toString());
			}
			return;
		}

		// type(node, nodegroup)
		if (valInfo instanceof NodeInfo && fromInfo instanceof NodeGroupInfo) {
			for (NodeInfo innerFromNodeInfo : ((NodeGroupInfo) fromInfo)
					.getAllNodes().values()) {
				statements.addNew(type, valInfo.toString(),
						innerFromNodeInfo.toString());
			}
			return;
		}

		// type(nodegroup, node)
		if (valInfo instanceof NodeGroupInfo && fromInfo instanceof NodeInfo) {
			for (NodeInfo innerValNodeInfo : ((NodeGroupInfo) valInfo)
					.getAllNodes().values()) {
				statements.addNew(type, innerValNodeInfo.toString(),
						fromInfo.toString());
			}
			return;
		}

		// type(nodegroup, nodegroup)
		if (valInfo instanceof NodeGroupInfo
				&& fromInfo instanceof NodeGroupInfo) {
			for (NodeInfo innerValNodeInfo : ((NodeGroupInfo) valInfo)
					.getAllNodes().values()) {
				for (NodeInfo innerFromNodeInfo : ((NodeGroupInfo) fromInfo)
						.getAllNodes().values()) {
					statements.addNew(type, innerValNodeInfo.toString(),
							innerFromNodeInfo.toString());
				}
			}
			return;
		}

		throw new ElementExportationException();
	}

	private void newStatementWithLinkEndCheck(StatementType type,
			String param1, String param2, LinkEnd end) {
		if (end == LinkEnd.Default) {
			statements.addNew(type, param1, param2);
		} else {
			statements.addNew(type, param1, param2, end.toString());
		}
	}

	private void processMostStatement(Class<? extends Annotation> type,
			Class<?>[] val) {
		if (mostMap.containsKey(type)) {
			problemReporter.multipleMostStatement(type, val);
			return;
		}

		if (val.length == 0) {
			problemReporter.mostStatementWithEmptyArguments(type, val);
			return;
		}

		try {
			NodeGroupInfo info = elementExporter.exportAnonymousNodeGroup(val);
			mostMap.put(type, info);
		} catch (ElementExportationException e) {
			problemReporter.mostStatementExportationFailed(type, val);
		}
	}

	private void exportMostStatement(StatementType type, NodeGroupInfo info) {
		NodeMap allNodes = elementExporter.getNodes();
		NodeMap mostedNodes = info.getAllNodes();

		for (Class<?> nodeClass : allNodes.keySet()) {
			if (!mostedNodes.containsKey(nodeClass)) {
				for (NodeInfo valNodeInfo : mostedNodes.values()) {
					statements.addNew(type, valNodeInfo.toString(), allNodes
							.get(nodeClass).toString());
				}
			}
		}
	}

	private void exportAlignmentWithGivenStatement(NodeGroupInfo info,
			StatementType type) {
		NodeInfo prevNode = null;
		for (NodeInfo currNode : info.getAllNodes().values()) {
			if (prevNode != null) {
				statements.addNew(type, prevNode.toString(),
						currNode.toString());
			}

			prevNode = currNode;
		}
	}

}
