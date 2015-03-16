package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.impl.StatementExporterImpl;
import hu.elte.txtuml.layout.lang.elements.LayoutGroup;
import hu.elte.txtuml.layout.lang.statements.Above;
import hu.elte.txtuml.layout.lang.statements.Alignment;
import hu.elte.txtuml.layout.lang.statements.Below;
import hu.elte.txtuml.layout.lang.statements.BottomMost;
import hu.elte.txtuml.layout.lang.statements.Contains;
import hu.elte.txtuml.layout.lang.statements.East;
import hu.elte.txtuml.layout.lang.statements.Left;
import hu.elte.txtuml.layout.lang.statements.LeftMost;
import hu.elte.txtuml.layout.lang.statements.North;
import hu.elte.txtuml.layout.lang.statements.Priority;
import hu.elte.txtuml.layout.lang.statements.Right;
import hu.elte.txtuml.layout.lang.statements.RightMost;
import hu.elte.txtuml.layout.lang.statements.South;
import hu.elte.txtuml.layout.lang.statements.TopMost;
import hu.elte.txtuml.layout.lang.statements.West;
import hu.elte.txtuml.layout.lang.statements.containers.AboveContainer;
import hu.elte.txtuml.layout.lang.statements.containers.BelowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.EastContainer;
import hu.elte.txtuml.layout.lang.statements.containers.LeftContainer;
import hu.elte.txtuml.layout.lang.statements.containers.NorthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.PriorityContainer;
import hu.elte.txtuml.layout.lang.statements.containers.RightContainer;
import hu.elte.txtuml.layout.lang.statements.containers.SouthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.WestContainer;

public interface StatementExporter {

	static StatementExporter create(StatementList statements) {
		return new StatementExporterImpl(statements);
	}

	// exporters
	
	void exportAlignment(Class<? extends LayoutGroup> group, Alignment annot);

	void exportContains(Class<? extends LayoutGroup> group, Contains annot);

	void exportAbove(Above annot);

	void exportBelow(Below annot);

	void exportRight(Right annot);

	void exportLeft(Left annot);

	void exportNorth(North annot);

	void exportSouth(South annot);

	// FIXME add Show
	
	void exportEast(East annot);

	void exportWest(West annot);

	void exportTopMost(TopMost annot);

	void exportBottomMost(BottomMost annot);

	void exportLeftMost(LeftMost annot);

	void exportRightMost(RightMost annot);

	void exportPriority(Priority annot);
	
	// container exporters
	
	void exportAboveContainer(AboveContainer annot);

	void exportBelowContainer(BelowContainer annot);

	void exportRightContainer(RightContainer annot);

	void exportLeftContainer(LeftContainer annot);

	void exportNorthContainer(NorthContainer annot);

	void exportSouthContainer(SouthContainer annot);

	// FIXME add ShowContainer

	void exportEastContainer(EastContainer annot);

	void exportWestContainer(WestContainer annot);

	void exportPriorityContainer(PriorityContainer annot);

}
