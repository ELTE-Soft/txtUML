visualizer.visualizers = {}
visualizer.visualizers.Visualizer = function (diagram, padding){
	if (this.constructor === visualizer.visualizers.Visualizer) {
      throw new Error("Can't instantiate abstract class!");
    }
	this._diagram = diagram;
	this._padding = padding;
	this._nodes = [];
	this._links = [];

	this._populateNodesAndLinks();
	
	this._grid = new visualizer.Grid(this._nodes,this._links, padding);

	
}
visualizer.visualizers.Visualizer.prototype._populateNodesAndLinks = function(){
	throw new Error('Visualizers must implement _populateNodesAndLinks()');
}

visualizer.visualizers.Visualizer.prototype.visualize = function(holder){
	var totalSize = this._grid.getTotalPixelSize();
	this._graph = new joint.dia.Graph();
	
	var paper = new joint.dia.Paper({
		'el': holder,
		'width': totalSize.width,
		'height': totalSize.height,
		'gridSize': 1,
		'model': this._graph,
		'linkView': this._getLinkView(),
		'perpendicularLinks': true
	});
	
	
	this._previsualize();
	_.each(this._nodes, function (clazz) {
		var bounds = this._grid.getPixelBounds(clazz.getGridPosition(), clazz.getGridSize());
		clazz.setBounds(bounds);
		this._graph.addCell(clazz.getNode());
	},this);

	_.each(this._links, function (relation) {
		
		var route = this._grid.translateRoute(relation.getRoute());
		relation.setPixelRoute(route);
		this._graph.addCell(relation.getLink());

	},this);
	

}

visualizer.visualizers.Visualizer.prototype._previsualize = function(){
	
}

visualizer.visualizers.CDVisualizer = function(diagram, padding){
	visualizer.visualizers.Visualizer.call(this, diagram, padding)
}

visualizer.visualizers.CDVisualizer.prototype = Object.create(visualizer.visualizers.Visualizer.prototype);
visualizer.visualizers.CDVisualizer.prototype.constructor = visualizer.visualizers.CDVisualizer;

visualizer.visualizers.CDVisualizer.prototype._getLinkView = function(){
	return visualizer.shapes.AttributeAssociationView;
}

visualizer.visualizers.CDVisualizer.prototype._populateNodesAndLinks = function(){
	_.each(this._diagram.classes, function (clazz) {
		this._nodes.push(new visualizer.nodeholders.ClassNode(clazz));
	},this);

	_.each(this._diagram.attributeLinks, function (link) {
		this._links.push(new visualizer.linkholders.ClassAttributeLink(link));
	},this);
	
	_.each(this._diagram.nonAttributeLinks, function (link) {
		this._links.push(new visualizer.linkholders.ClassNonAttributeLink(link));
	},this);
}

visualizer.visualizers.SMVisualizer = function(diagram, padding){
	visualizer.visualizers.Visualizer.call(this, diagram, padding)
}

visualizer.visualizers.SMVisualizer.prototype = Object.create(visualizer.visualizers.Visualizer.prototype);
visualizer.visualizers.SMVisualizer.prototype.constructor = visualizer.visualizers.SMVisualizer;

visualizer.visualizers.SMVisualizer.prototype._getLinkView = function(){
	return joint.dia.LinkView;
}

visualizer.visualizers.SMVisualizer.prototype._populateNodesAndLinks = function(){
	
	_.each(this._diagram.states, function (state) {
		this._nodes.push(new visualizer.nodeholders.StateNode(state));
	},this);

	var inits = [];
	var current = null;
	_.each(this._diagram.pseudoStates, function (state) {
		if (visualizer.Utils.MAPS.NON_SCALABLE_PSEUDOSTATE_KINDS.indexOf(state.kind) > -1){
			current = new visualizer.nodeholders.NonScalablePseudoStateNode(state);
			this._nodes.push(current);
		}else{
			current = new visualizer.nodeholders.NonScalablePseudoStateNode(state);
			this._nodes.push(current);
		}
		
		if (state.kind === 'initial'){
			inits[current.getID()] = current; 
		}
		
	},this);
	
		
	_.each(this._diagram.signals, function (link) {
		this._links.push(new visualizer.linkholders.TransitionLink(link));
		if (_.has(inits,link.fromID)){
            inits[link.fromID].correctGridBounds(link.anchors[0],link.route[0]);
		}
	},this);
	
	
	
}

visualizer.visualizers.SMVisualizer.prototype._previsualize = function(){
	var totalSize = this._grid.getTotalPixelSize();
	this._container = new visualizer.shapes.State({
		'position':{
			'x':10,
			'y':10
		},
		'name' : this._diagram.name
	});
	var size = {
		'width': totalSize.width - 20,
		'height': totalSize.height - 20
	}
	this._container.set('size',size);
	this._container.attr('rect/width',size.width);
	this._container.attr('rect/height',size.height);
	
	_.each(this._nodes, function (clazz) {
		this._container.embed(clazz.getNode());
	},this);

	_.each(this._links, function (relation) {
		this._container.embed(relation.getLink());
	},this);
	
	this._graph.addCell(this._container);
}