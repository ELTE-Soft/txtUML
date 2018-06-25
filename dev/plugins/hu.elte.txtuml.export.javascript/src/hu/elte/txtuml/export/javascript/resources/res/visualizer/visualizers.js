visualizer.visualizers = {}
// This is a base class for diagram visualizers. A visualizer task is to create
// holders from input, then visualize result when visualize is called
visualizer.visualizers.Visualizer = function (diagram, padding) {
	if (this.constructor === visualizer.visualizers.Visualizer) {
		throw new Error("Can't instantiate abstract class!");
	}
	this._diagram = diagram;
	this._padding = padding;
	this._nodes = [];
	this._links = [];

	// generate holders from input
	this._populateNodesAndLinks();
}

visualizer.visualizers.Visualizer.prototype.getShapeIdByElementName = function (name) {
	var nameMatches = function (holder) { return holder.getElementName() == name };
	var matchingHolder = _.find(this._nodes, nameMatches) || _.find(this._links, nameMatches);

	if (matchingHolder) {
		return matchingHolder.getShapeId();
	} else {
		return undefined;
	}
}

// returns ther LinkView to use during visualization
visualizer.visualizers.Visualizer.prototype._getLinkView = function () {
	return joint.dia.LinkView;
}

// Abstract method to populate holders (a visualizer must implement this)
visualizer.visualizers.Visualizer.prototype._populateNodesAndLinks = function () {
	throw new Error('Visualizers must implement _populateNodesAndLinks()');
}

// The visualization function. The diagram will be displayed in the element
// passed in holder
visualizer.visualizers.Visualizer.prototype.visualize = function (holder) {
	this._graph = new joint.dia.Graph();

	// JointJS paper
	var paper = new joint.dia.Paper({
			'el': holder,
			'gridSize': 1,
			'model': this._graph,
			'linkView': this._getLinkView(),
			'perpendicularLinks': true,
			'origin': {
				'x': this._padding,
				'y': this._padding
			},
			// initial size must be bigger then the diagram it will contain (will be scaled down)
			'width': 999999999999999999,
			'height': 999999999999999999,
			// make interaction toggleable
			interactive: function(cellView){
				//panEnabled is global
				return !panEnabled;
			}

		});

	// do previsualization tasks
	this._previsualize();

	_.each(this._nodes, function (node) {
		// add node to graph
		this._graph.addCell(node.getNode());
		// add modifier CSS classes
		node.addModifierClasses(paper);
	}, this);

	_.each(this._links, function (relation) {
		// add link to graph
		this._graph.addCell(relation.getLink());

	}, this);

	// do postvisualization tasks
	this._postvisualize();

	// scale paper to fit contents
	paper.fitToContent({
		'padding': this._padding,
		'allowNewOrigin': 'any'
	});
}

// Called before visualization (graph is ready at this point)
visualizer.visualizers.Visualizer.prototype._previsualize = function () {}

// Called after visualization (graph is ready at this point)
visualizer.visualizers.Visualizer.prototype._postvisualize = function () {}

// A visualizer for class diagrams
visualizer.visualizers.CDVisualizer = function (diagram, padding) {
	visualizer.visualizers.Visualizer.call(this, diagram, padding)
}

// prototype chaining
visualizer.visualizers.CDVisualizer.prototype = Object
	.create(visualizer.visualizers.Visualizer.prototype);
visualizer.visualizers.CDVisualizer.prototype.constructor = visualizer.visualizers.CDVisualizer;

// returns ther LinkView to use during visualization (in this case:
// visualizer.shapes.AttributeAssociationView)
visualizer.visualizers.CDVisualizer.prototype._getLinkView = function () {
	return visualizer.shapes.AttributeAssociationView;
}

// Populates holders with classes and associations
visualizer.visualizers.CDVisualizer.prototype._populateNodesAndLinks = function () {
	_.each(this._diagram.classes, function (node) {
		this._nodes.push(new visualizer.nodeholders.ClassNode(node));
	}, this);

	_.each(this._diagram.attributeLinks, function (link) {
		this._links.push(new visualizer.linkholders.ClassAttributeLink(link));
	}, this);

	_.each(this._diagram.nonAttributeLinks, function (link) {
		this._links
		.push(new visualizer.linkholders.ClassNonAttributeLink(link));
	}, this);
}

// A visualizer for statemachines
visualizer.visualizers.SMVisualizer = function (diagram, padding) {
	visualizer.visualizers.Visualizer.call(this, diagram, padding)
}

// prototype chaining
visualizer.visualizers.SMVisualizer.prototype = Object
	.create(visualizer.visualizers.Visualizer.prototype);
visualizer.visualizers.SMVisualizer.prototype.constructor = visualizer.visualizers.SMVisualizer;

// populates holders with states, pseudo states and transitions
visualizer.visualizers.SMVisualizer.prototype._populateNodesAndLinks = function () {

	// populate nodeholders with states
	_.each(this._diagram.states, function (state) {
		this._nodes.push(new visualizer.nodeholders.StateNode(state));
	}, this);

	// populate nodeholders with pseudo states
	_.each(this._diagram.pseudoStates, function (state) {
		this._nodes.push(new visualizer.nodeholders.PseudoStateNode(state));

	}, this);

	// populate linkholders with transitions
	_.each(this._diagram.transitions, function (link) {
		this._links.push(new visualizer.linkholders.TransitionLink(link));
	}, this);

}

visualizer.visualizers.SMVisualizer.prototype._postvisualize = function () {
	// scale SM region to fit embeded elements
	this._container.fitEmbeds({
		'padding': {
			top: 40,
			right: 10,
			bottom: 10,
			left: 10
		}
	});
}

// in case of statemachines a container need to be added before visualization
visualizer.visualizers.SMVisualizer.prototype._previsualize = function () {
	// container size will be set after visualization
	this._container = new visualizer.shapes.State({
			'position': {
				'x': 0,
				'y': 0
			},
			'name': this._diagram.machineName
		});

	// embed the nodes into the container
	_.each(this._nodes, function (node) {
		this._container.embed(node.getNode());
	}, this);

	// embed the links into the container
	_.each(this._links, function (relation) {
		this._container.embed(relation.getLink());
	}, this);

	// add container to the graph
	this._graph.addCell(this._container);
}
