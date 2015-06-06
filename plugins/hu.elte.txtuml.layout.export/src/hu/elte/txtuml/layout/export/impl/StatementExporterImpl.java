package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.StatementExporter;
import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.elements.LayoutGroup;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.elements.LayoutNonGroupElement;
import hu.elte.txtuml.layout.lang.statements.Above;
import hu.elte.txtuml.layout.lang.statements.Alignment;
import hu.elte.txtuml.layout.lang.statements.Below;
import hu.elte.txtuml.layout.lang.statements.BottomMost;
import hu.elte.txtuml.layout.lang.statements.Column;
import hu.elte.txtuml.layout.lang.statements.Contains;
import hu.elte.txtuml.layout.lang.statements.Diamond;
import hu.elte.txtuml.layout.lang.statements.East;
import hu.elte.txtuml.layout.lang.statements.Left;
import hu.elte.txtuml.layout.lang.statements.LeftMost;
import hu.elte.txtuml.layout.lang.statements.North;
import hu.elte.txtuml.layout.lang.statements.Priority;
import hu.elte.txtuml.layout.lang.statements.Right;
import hu.elte.txtuml.layout.lang.statements.RightMost;
import hu.elte.txtuml.layout.lang.statements.Row;
import hu.elte.txtuml.layout.lang.statements.Show;
import hu.elte.txtuml.layout.lang.statements.South;
import hu.elte.txtuml.layout.lang.statements.TopMost;
import hu.elte.txtuml.layout.lang.statements.West;
import hu.elte.txtuml.layout.lang.statements.containers.AboveContainer;
import hu.elte.txtuml.layout.lang.statements.containers.BelowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.ColumnContainer;
import hu.elte.txtuml.layout.lang.statements.containers.DiamondContainer;
import hu.elte.txtuml.layout.lang.statements.containers.EastContainer;
import hu.elte.txtuml.layout.lang.statements.containers.LeftContainer;
import hu.elte.txtuml.layout.lang.statements.containers.NorthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.PriorityContainer;
import hu.elte.txtuml.layout.lang.statements.containers.RightContainer;
import hu.elte.txtuml.layout.lang.statements.containers.RowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.ShowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.SouthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.WestContainer;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;

/**
 * Default implementation for {@link StatementExporter}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class StatementExporterImpl implements StatementExporter {

	private final StatementList statements;
	private final ElementExporter elementExporter;

	public StatementExporterImpl(StatementList statements, ElementExporter elementExporter) {
		this.statements = statements;
		this.elementExporter = elementExporter;
	}

	@SuppressWarnings("unused") // TODO remove when used
	private void newStatementWithLinkEndCheck(StatementType type, String param1, String param2, LinkEnd end) {
		if (end == LinkEnd.Default) {
			statements.addNew(type, param1, param2);
		} else {
			statements.addNew(type, param1, param2, end.toString());			
		}
	}
	
	private void exportAdjacencyStatement(StatementType type, Class<? extends LayoutNode> val, Class<? extends LayoutNode> from) {
		NodeInfo valInfo = elementExporter.exportNode(val).asNodeInfo();
		NodeInfo fromInfo = elementExporter.exportNode(from).asNodeInfo();
		if (valInfo == null || fromInfo == null) {
			// TODO show error
			return;
		}
		statements.addNew(type, valInfo.toString(), fromInfo.toString());
	}
	
	// statement exporters

	@Override
	public void exportAlignment(Class<? extends LayoutGroup> group,
			Alignment annot) {
		// TODO
		//statements.addNew(StatementType.layout, asString(group), annot.value()
			//	.toString());
	}

	@Override
	public void exportContains(Class<? extends LayoutGroup> group,
			Contains annot) {
		// TODO
		//for (Class<? extends LayoutElement> element : annot.value()) {
			//statements.addNew(StatementType.group, asString(element),
				//	asString(group));
		//}
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
		// TODO
		//newStatementWithLinkEndCheck(StatementType.north,
			//	asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportSouth(South annot) {
		// TODO
		//newStatementWithLinkEndCheck(StatementType.south,
			//	asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportEast(East annot) {
		// TODO
		//newStatementWithLinkEndCheck(StatementType.east,
			//	asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportWest(West annot) {
		// TODO
		//newStatementWithLinkEndCheck(StatementType.west,
			//	asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportTopMost(TopMost annot) {
		// TODO
		//statements.addNew(StatementType.topmost, asString(annot.value()));
	}

	@Override
	public void exportBottomMost(BottomMost annot) {
		// TODO
		//statements.addNew(StatementType.bottommost, asString(annot.value()));
	}

	@Override
	public void exportLeftMost(LeftMost annot) {
		// TODO
		//statements.addNew(StatementType.leftmost, asString(annot.value()));
	}

	@Override
	public void exportRightMost(RightMost annot) {
		// TODO
		//statements.addNew(StatementType.rightmost, asString(annot.value()));
	}

	@Override
	public void exportPriority(Priority annot) {
		// TODO
		//statements.addNew(StatementType.priority, asString(annot.val()),
			//	Integer.toString(annot.prior()));
	}

	@Override
	public void exportShow(Show annot) {
		for (Class<? extends LayoutNonGroupElement> element : annot.value()) {
			elementExporter.exportNonGroupElement(element);
		}
		// TODO show warning if empty
	}

	@Override
	public void exportColumn(Column annot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportRow(Row annot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportDiamond(Diamond annot) {
		// TODO Auto-generated method stub
		
	}
	
	// statement container exporters

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
	
}
