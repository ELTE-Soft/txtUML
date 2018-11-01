package hu.elte.txtuml.layout.visualizer.algorithms.links.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation.RouteConfig;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementLevel;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

/**
 * This class generates {@link Statement}s automatically for the
 * {@link LineAssociation}s.
 */
public class DefaultAssocStatements {
	/**
	 * Generated {@link Statement}s.
	 */
	private List<Statement> _result;
	/**
	 * Previously used Group Id.
	 */
	private Integer _gId;

	/**
	 * Constructor and run command for the generation of automatic
	 * {@link Statement}s on {@link LineAssociation}s.
	 * 
	 * @param gid
	 *            previously used group id.
	 * @param stat
	 *            list of {@link Statement}s.
	 * @param assocs
	 *            list of {@link LineAssociation}s.
	 * @throws InternalException
	 *             Throws if the algorithm encounters a value that should not
	 *             have reached that part. Contact programmer for more details.
	 */
	public DefaultAssocStatements(Integer gid, List<Statement> stat, Set<LineAssociation> assocs)
			throws InternalException {
		_result = stat;
		_gId = gid;

		//predefinePriorities(assocs);
	}

	/**
	 * Returns the previously used Group Id.
	 * 
	 * @return the previously used Group Id.
	 */
	public Integer getGroupId() {
		return _gId;
	}

	/**
	 * Returns the generated {@link Statement}s.
	 * 
	 * @return the generated {@link Statement}s.
	 */
	public List<Statement> value() {
		return _result;
	}

	@SuppressWarnings("all")
	private void predefinePriorities(List<LineAssociation> assocs) throws InternalException {
		// select min priority
		Set<Statement> priorities = _result.stream().filter(s -> s.getType().equals(StatementType.priority))
				.collect(Collectors.toSet());
		Optional<Integer> minPriority = _result.stream().filter(s -> s.getType().equals(StatementType.priority))
				.map(s -> Integer.valueOf(s.getParameter(1))).min((i, j) -> {
					return Integer.compare(i, j);
				});

		Integer freeLinkCount = (assocs.size() - priorities.size());
		if (minPriority.isPresent()) {
			Integer min = minPriority.get();
			if (freeLinkCount >= min) {
				// not good, ++ every prior
				Integer alterAmount = freeLinkCount;
				for (Statement s : _result) {
					if (s.getType().equals(StatementType.priority)) {
						Integer par_value = Integer.valueOf(s.getParameter(1)) + (alterAmount * 2);
						s.setParameter(1, par_value.toString());
					}
				}
			}
		}

		Integer actPrior = 1 + freeLinkCount;
		ArrayList<LineAssociation> orderedAssocs = (ArrayList<LineAssociation>) assocs.stream()
				.collect(Collectors.toList());
		orderedAssocs.sort((a1, a2) -> {
			return Double.compare(distanceOfEnds(a1), distanceOfEnds(a2));
		});
		for (LineAssociation a : orderedAssocs) {
			if (!priorities.stream().anyMatch(s -> s.getParameter(0).equals(a.getId()))) {
				++_gId;
				Integer parameterValue = a.isReflexive() ? (actPrior - freeLinkCount) : actPrior;
				_result.add(new Statement(StatementType.priority, StatementLevel.Low, _gId, a.getId(),
						parameterValue.toString()));
				++actPrior;
			}
		}
	}

	private Double distanceOfEnds(LineAssociation a) {
		Point one = a.getRoute(RouteConfig.START);
		Point two = a.getRoute(RouteConfig.END);

		return (Point.Substract(two, one)).length();
	}
}
