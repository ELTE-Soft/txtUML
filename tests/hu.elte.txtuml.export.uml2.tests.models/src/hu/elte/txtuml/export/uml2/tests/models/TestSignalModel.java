package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.Signal;

public class TestSignalModel extends Model {
		class TestSignal extends Signal {
			int a;
			boolean b;
			String c;
		}
}
