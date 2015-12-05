package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.BottomMost;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.LeftMost;
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
