package hu.elte.txtuml.layout.export;

import org.junit.Test;

import hu.elte.txtuml.api.layout.Alignment;
import hu.elte.txtuml.api.layout.AlignmentType;
import hu.elte.txtuml.api.layout.BottomMost;
import hu.elte.txtuml.api.layout.Contains;
import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Priority;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.TopMost;
import hu.elte.txtuml.layout.export.Diagram1.L;
import hu.elte.txtuml.layout.export.Diagram1.LG1;
import hu.elte.txtuml.layout.export.Diagram1.MyClass;
import hu.elte.txtuml.layout.export.Diagram1.NG;
import hu.elte.txtuml.layout.export.Diagram1.NG3;
import hu.elte.txtuml.layout.export.Diagram1.NG52;
import hu.elte.txtuml.layout.export.Diagram1.NGA;
import hu.elte.txtuml.layout.export.Diagram1.NGB;
import hu.elte.txtuml.layout.export.Diagram1.NGC;
import hu.elte.txtuml.layout.export.model1.A;
import hu.elte.txtuml.layout.export.model1.B;
import hu.elte.txtuml.layout.export.model1.C;
import hu.elte.txtuml.layout.export.model1.D;
import hu.elte.txtuml.layout.export.model1.E;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;


class Diagram1 extends Diagram {

	@Contains({ A.class, B.class })
	class NG1 extends NodeGroup {
	}

	@Contains({ NG1.class, C.class })
	// NodeGroup NG1 as parameter
	@Alignment(AlignmentType.TopToBottom)
	class NG2 extends NodeGroup {
	}

	@Contains({ A.class, NG1.class, D.class })
	class NG51 extends NodeGroup {
	}

	@Contains({ C.class, NG52.class, F.class })
	class NG52 extends NodeGroup {
	}

	@Contains({ NGB.class })
	class NGA extends NodeGroup {
	}

	@Contains({ NGC.class })
	class NGB extends NodeGroup {
	}

	@Contains({ NGA.class })
	class NGC extends NodeGroup {
	}

	class NG3 extends NodeGroup {
	}

	@Alignment(AlignmentType.TopToBottom)
	class LG1 extends LinkGroup {
	}

	class MyClass {
	}

	class F extends Phantom {
	}

	class G extends Phantom {
	}

	@Deprecated
	@Contains({})
	// {C.class, D.class} is an anonymous NodeGroup
	class NG extends NodeGroup {
	}

	@Diamond(top = A.class, right = B.class, bottom = C.class, left = D.class)
	@Diamond(top = D.class, bottom = E.class, right = A.class, left = D.class)
	@North(val = { A.class, B.class, NG1.class }, from = B.class)
	@Show({})
	@Row({ NG52.class })
	@North(val = { NG52.class }, from = A.class, end = LinkEnd.End)
	@TopMost({ NG52.class })
	@BottomMost({ B.class, C.class })
	@Priority(val = { LG1.class }, prior = 100)
	@Row({ A.class, B.class, NG1.class })
	@North(val = NGA.class, from = NG52.class)
	@Deprecated
	class L extends Layout {
	}

}

public class Test1 extends BaseTest {

	@Test
	public void run() {
		problems.cardinalStatementExportationFailed(StatementType.north,
				new Class<?>[] { NGA.class }, new Class<?>[] { NG52.class },
				LinkEnd.Default);
		problems.cardinalStatementExportationFailed(StatementType.north,
				new Class<?>[] { NG52.class }, new Class<?>[] { A.class },
				LinkEnd.End);
		problems.lineStatementExportationFailed("row",
				new Class<?>[] { NG52.class });
		problems.mostStatementExportationFailed(TopMost.class,
				new Class<?>[] { NG52.class });
		problems.selfContainment(NGA.class);
		problems.selfContainment(NGB.class);
		problems.selfContainment(NGC.class);
		problems.selfContainment(NG52.class);

		// warnings

		problems.groupWithoutContainsAnnotation(LG1.class);
		problems.groupWithoutContainsAnnotation(NG3.class);
		problems.emptyGroup(NG.class);
		problems.unknownInnerClassOfDiagram(MyClass.class);
		problems.sugarStatementWithEmptyArguments("show");
		problems.unknownAnnotationOnClass(
				LG1.class.getAnnotation(Alignment.class), LG1.class);
		problems.unknownAnnotationOnClass(
				NG.class.getAnnotation(Deprecated.class), NG.class);
		problems.unknownAnnotationOnClass(
				L.class.getAnnotation(Deprecated.class), L.class);
		
		testForFailure(Diagram1.class);
	}
}
