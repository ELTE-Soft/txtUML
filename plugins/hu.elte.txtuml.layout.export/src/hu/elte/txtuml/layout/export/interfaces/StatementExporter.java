package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.api.layout.statements.Above;
import hu.elte.txtuml.api.layout.statements.Below;
import hu.elte.txtuml.api.layout.statements.BottomMost;
import hu.elte.txtuml.api.layout.statements.Column;
import hu.elte.txtuml.api.layout.statements.Diamond;
import hu.elte.txtuml.api.layout.statements.East;
import hu.elte.txtuml.api.layout.statements.Left;
import hu.elte.txtuml.api.layout.statements.LeftMost;
import hu.elte.txtuml.api.layout.statements.North;
import hu.elte.txtuml.api.layout.statements.Priority;
import hu.elte.txtuml.api.layout.statements.Right;
import hu.elte.txtuml.api.layout.statements.RightMost;
import hu.elte.txtuml.api.layout.statements.Row;
import hu.elte.txtuml.api.layout.statements.Show;
import hu.elte.txtuml.api.layout.statements.South;
import hu.elte.txtuml.api.layout.statements.TopMost;
import hu.elte.txtuml.api.layout.statements.West;
import hu.elte.txtuml.api.layout.statements.containers.AboveContainer;
import hu.elte.txtuml.api.layout.statements.containers.BelowContainer;
import hu.elte.txtuml.api.layout.statements.containers.ColumnContainer;
import hu.elte.txtuml.api.layout.statements.containers.DiamondContainer;
import hu.elte.txtuml.api.layout.statements.containers.EastContainer;
import hu.elte.txtuml.api.layout.statements.containers.LeftContainer;
import hu.elte.txtuml.api.layout.statements.containers.NorthContainer;
import hu.elte.txtuml.api.layout.statements.containers.PriorityContainer;
import hu.elte.txtuml.api.layout.statements.containers.RightContainer;
import hu.elte.txtuml.api.layout.statements.containers.RowContainer;
import hu.elte.txtuml.api.layout.statements.containers.ShowContainer;
import hu.elte.txtuml.api.layout.statements.containers.SouthContainer;
import hu.elte.txtuml.api.layout.statements.containers.WestContainer;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.impl.StatementExporterImpl;
import hu.elte.txtuml.layout.export.problems.ProblemReporter;

/**
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface StatementExporter {

    static StatementExporter create(ElementExporter elementExporter, ProblemReporter problemReporter) {
        return new StatementExporterImpl(elementExporter, problemReporter);
    }

    StatementList getStatements();
    
	// statement exporters
	
	void exportAlignment(NodeGroupInfo info);

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
	
	// exportation finalizers
	
	void resolveMosts();
	
	void exportPhantoms();

}
