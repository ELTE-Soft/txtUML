visualizer.visualizers = {}
//This is a base class for diagram visualizers. A visualizer task is to create holders from input,
//create holders, create a grid, set sizes and positions then visualize result when visualize is called
visualizer.visualizers.Visualizer = function (diagram, padding){
	if (this.constructor === visualizer.visualizers.Visualizer) {
      throw new Error("Can't instantiate abstract class!");
    }
	this._diagram = diagram;
	this._padding = padding;
	this._nodes = [];
	this._links = [];

	//generate holders from input
	this._populateNodesAndLinks();
	
	//create grid from holders padding and spacing
	//this._grid = new visualizer.Grid(this._nodes,this._links, padding, diagram.spacing);

	
}
//returns ther LinkView to use during visualization 
visualizer.visualizers.Visualizer.prototype._getLinkView = function(){
	return joint.dia.LinkView;
}

//Abstract method to populate holders (a visualizer must implement this)
visualizer.visualizers.Visualizer.prototype._populateNodesAndLinks = function(){
	throw new Error('Visualizers must implement _populateNodesAndLinks()');
}

//The visualization function. The diagram will be displayed in the element passed in holder
visualizer.visualizers.Visualizer.prototype.visualize = function(holder){
	//var totalSize = this._grid.getTotalPixelSize();
	this._graph = new joint.dia.Graph();
	
	//JointJS paper
	var paper = new joint.dia.Paper({
		'el': holder,
		'gridSize': 1,
		'model': this._graph,
		'linkView': this._getLinkView(), 
		'perpendicularLinks': true,
		'origin':{
			'x':this._padding,
			'y':this._padding
		},
		'width': 999999999999999999,
		'height': 999999999999999999

	});
	
	//do previsualization tasks
	this._previsualize();
	
	_.each(this._nodes, function (node) {
		//get pixel bounds from grid and set it for nodeholders
		//var bounds = this._grid.getPixelBounds(node.getGridPosition(), node.getGridSize());
		//node.setBounds(bounds);
		
		//add node to graph
		this._graph.addCell(node.getNode());
	},this);

	_.each(this._links, function (relation) {
		//get pixel vertices from grid and set it for linkholders
		//var route = this._grid.translateRoute(relation.getRoute());
		//relation.setPixelRoute(route);
		
		//add link to graph
		this._graph.addCell(relation.getLink()); 

	},this);
	

	paper.fitToContent({'padding' : this._padding, 'allowNewOrigin': 'any'});
	//paper.setOrigin(this._padding,this._padding);	

}

//Called before visualization (graph is ready at this point)
visualizer.visualizers.Visualizer.prototype._previsualize = function(){
	
}

//A visualizer for class diagrams
visualizer.visualizers.CDVisualizer = function(diagram, padding){
	visualizer.visualizers.Visualizer.call(this, diagram, padding)
}

//prototype chaining
visualizer.visualizers.CDVisualizer.prototype = Object.create(visualizer.visualizers.Visualizer.prototype);
visualizer.visualizers.CDVisualizer.prototype.constructor = visualizer.visualizers.CDVisualizer;

//returns ther LinkView to use during visualization (in this case: visualizer.shapes.AttributeAssociationView)
visualizer.visualizers.CDVisualizer.prototype._getLinkView = function(){
	return visualizer.shapes.AttributeAssociationView;
}

//Populates holders with classes and associations
visualizer.visualizers.CDVisualizer.prototype._populateNodesAndLinks = function(){
	_.each(this._diagram.classess, function (node) { 
		this._nodes.push(new visualizer.nodeholders.ClassNode(node));
	},this);

	_.each(this._diagram.attributeLinks, function (link) {
		this._links.push(new visualizer.linkholders.ClassAttributeLink(link));
	},this);
	
	_.each(this._diagram.nonAttributeLinks, function (link) {
		this._links.push(new visualizer.linkholders.ClassNonAttributeLink(link));
	},this);
}

//A visualizer for statemachines
visualizer.visualizers.SMVisualizer = function(diagram, padding){
	visualizer.visualizers.Visualizer.call(this, diagram, padding)
}

//prototype chaining
visualizer.visualizers.SMVisualizer.prototype = Object.create(visualizer.visualizers.Visualizer.prototype);
visualizer.visualizers.SMVisualizer.prototype.constructor = visualizer.visualizers.SMVisualizer;

//populates holders with states, pseudo states and transitions
visualizer.visualizers.SMVisualizer.prototype._populateNodesAndLinks = function(){
	
	//populate nodeholders with states
	_.each(this._diagram.states, function (state) {
		this._nodes.push(new visualizer.nodeholders.StateNode(state));
	},this);

	var inits = []; //collection for initial states (currently max 1)
	var current = null; // currently generated holder
	
	//populate nodeholders with pseudo states
	_.each(this._diagram.pseudoStates, function (state) {
		if (visualizer.Utils.MAPS.NON_SCALABLE_PSEUDOSTATE_KINDS.indexOf(state.kind) > -1){
			current = new visualizer.nodeholders.NonScalablePseudoStateNode(state);
			this._nodes.push(current);
		}else{
			current = new visualizer.nodeholders.ScalablePseudoStateNode(state);
			this._nodes.push(current);
		}
		
		//store all initial pseudo states (currently maximum 1)
		if (state.kind === 'initial'){
			inits[current.getID()] = current; 
		}
		
	},this);
	
	//populate linkholders with transitions
	_.each(this._diagram.transitions, function (link) {
		this._links.push(new visualizer.linkholders.TransitionLink(link));
		
		//correct initial states' location based on the link's route exiting it
		if (_.has(inits,link.fromID)){
            inits[link.fromID].correctGridBounds(link.anchors[0],link.route[0]);
		}
	},this);
	
}

//in case of statemachines a container need to be added before visualization
visualizer.visualizers.SMVisualizer.prototype._previsualize = function(){
	//we create a container which is smaller than the grid's total size
	//but bigger than the grid's total size without padding so it can contain
	//all the states and links
	var totalSize = this._grid.getTotalPixelSize();
	this._container = new visualizer.shapes.State({
		'position':{
			'x':this._padding /2,
			'y':this._padding /2
		},
		'name' : this._diagram.machineName
	});
	var size = {
		'width': totalSize.width - this._padding /2,
		'height': totalSize.height - this._padding /2
	}
	this._container.set('size',size);
	
	//embed the nodes into the container
	_.each(this._nodes, function (node) {
		this._container.embed(node.getNode());
	},this);

	//embed the links into the container
	_.each(this._links, function (relation) {
		this._container.embed(relation.getLink());
	},this);
	
	//add container to the graph
	this._graph.addCell(this._container);
}