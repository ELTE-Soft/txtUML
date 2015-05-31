package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.impl.StatementExporterImpl;
import hu.elte.txtuml.layout.lang.elements.LayoutGroup;
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

/**
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface StatementExporter {

	static StatementExporter create(StatementList statements, ElementExporter elementExporter) {
		return new StatementExporterImpl(statements, elementExporter);
	}

	// statement exporters
	
	void exportAlignment(Class<? extends LayoutGroup> group, Alignment annot);

	void exportContains(Class<? extends LayoutGroup> group, Contains annot);

	void exportAbove(Above annot);

	void exportBelow(Below annot);

	void exportRight(Right annot);

	void exportLeft(Left annot);

	void exportNorth(North annot);

	void exportSouth(South annot);
	
	void exportEast(East annot);

	void exportWest(West annot);

	void exportTopMost(TopMost annot);

	void exportBottomMost(BottomMost annot);

	void exportLeftMost(LeftMost annot);

	void exportRightMost(RightMost annot);

	void exportPriority(Priority annot);

	void exportShow(Show annot);
	
	void exportColumn(Column annot);
	
	void exportRow(Row annot);
	
	void exportDiamond(Diamond annot);
	
	// statement container exporters
	
	void exportAboveContainer(AboveContainer annot);

	void exportBelowContainer(BelowContainer annot);

	void exportRightContainer(RightContainer annot);

	void exportLeftContainer(LeftContainer annot);

	void exportNorthContainer(NorthContainer annot);

	void exportSouthContainer(SouthContainer annot);

	void exportEastContainer(EastContainer annot);

	void exportWestContainer(WestContainer annot);

	void exportPriorityContainer(PriorityContainer annot);
	
	void exportShowContainer(ShowContainer annot);
	
	void exportColumnContainer(ColumnContainer annot);
	
	void exportRowContainer(RowContainer annot);
	
	void exportDiamondContainer(DiamondContainer annot);

}
