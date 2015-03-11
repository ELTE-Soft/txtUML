package hu.elte.txtuml.layout.visualizer.helpers;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashSet;

public class MyModel
{
	public Triple<HashSet<RectangleObject>, HashSet<LineAssociation>, ArrayList<Statement>> Value;
	
	public MyModel()
	{
		Value = null;
	}
	
	public MyModel(
			Triple<HashSet<RectangleObject>, HashSet<LineAssociation>, ArrayList<Statement>> v)
	{
		Value = v;
	}
	
	public MyModel(HashSet<RectangleObject> o, HashSet<LineAssociation> a,
			ArrayList<Statement> s)
	{
		Value = new Triple<HashSet<RectangleObject>, HashSet<LineAssociation>, ArrayList<Statement>>(
				o, a, s);
	}
}
