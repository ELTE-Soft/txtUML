package hu.elte.txtuml.layout.visualizer.algorithms.boxes.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementLevel;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;
import hu.elte.txtuml.utils.Pair;

/**
 * Class for resolving overlapping boxes with matrix layout
 */
public class MatrixOverlapRemover {

	private List<RectangleObject> _objects;
	private List<Statement> _statements;
	private Integer _gId;

	/**
	 * Creates a {@link MatrixOverlapRemover} object.
	 * @param objs {@link RectangleObject}s to work with.
	 * @param stats {@link Statement}s to work with.
	 * @param gid Latest used {@link Statement} group id.
	 */
	public MatrixOverlapRemover(List<RectangleObject> objs, List<Statement> stats, Integer gid) {
		_objects = objs;
		_statements = stats;
		_gId = gid;
	}

	/**
	 * Returns the necessary {@link Statement}s to form a matrix layout.
	 * @return the necessary {@link Statement}s to form a matrix layout.
	 * @throws ConversionException
	 * @throws InternalException
	 */
	public Pair<List<Statement>, Integer> makeStatements() throws ConversionException, InternalException {
		// Fixes the layout of the diagram by giving statements preserving the
		// current state
		Pair<List<Statement>, Integer> pair = OverlapHelper.fixCurrentState(_objects, _statements, _gId);
		_statements.addAll(pair.getFirst());
		_gId = pair.getSecond();

		for (Entry<Point, HashSet<String>> entry : OverlapHelper.overlaps(_objects).entrySet()) {
			if (entry.getValue().size() > 1) {
				Integer matrixSize = (int) Math.ceil(Math.sqrt(entry.getValue().size()));
				String[][] matrix = new String[matrixSize][matrixSize];

				Integer nextI = 0;
				Integer nextJ = 0;

				for (String name : entry.getValue()) {
					matrix[nextI][nextJ] = name;
					++nextJ;

					if (nextJ >= matrixSize) {
						++nextI;
						nextJ = 0;
					}
				}

				makeStatementsForMatrix(matrix, matrixSize);
			}
		}
		
		return Pair.of(_statements, _gId);
	}

	private void makeStatementsForMatrix(String[][] matrix, Integer size) throws InternalException {
		++_gId;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if (matrix[i][j] == null)
					continue;

				if (i > 0) {
					_statements.add(new Statement(StatementType.south, StatementLevel.Medium, _gId, matrix[i][j],
							matrix[i - 1][j]));
				}
				if (j > 0) {
					_statements.add(new Statement(StatementType.east, StatementLevel.Medium, _gId, matrix[i][j],
							matrix[i][j - 1]));
				}
			}
		}
	}
}
