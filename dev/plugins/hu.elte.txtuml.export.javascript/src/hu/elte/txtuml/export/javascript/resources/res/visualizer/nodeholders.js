visualizer.nodeholders = {}
//This is an abstract base class for wrapper classes around JointJS Node models, and abstract layout informations
visualizer.nodeholders.Node = function (node){
	if (this.constructor === visualizer.nodeholders.Node) {
      throw new Error("Can't instantiate abstract class!");
    }
	this._id = node.id;
	this._gridPosition = node.position;
	this._gridSize = {
		'width': node.width,
		'height': node.height
	};	
}

//returns the node layout descriptor ID
visualizer.nodeholders.Node.prototype.getID = function(){
	return this._id;
}

//returns the JointJS model
visualizer.nodeholders.Node.prototype.getNode = function(){
	return this._node;
}

//returns the node's abstract position
visualizer.nodeholders.Node.prototype.getGridPosition = function(){
	return this._gridPosition;
}

//returns the node's abstract size
visualizer.nodeholders.Node.prototype.getGridSize = function(){
	return this._gridSize;
}

//returns the node's size
visualizer.nodeholders.Node.prototype.getPixelSize = function(){
	return this._node.get('size');
}

//sets the node pixel position and size
visualizer.nodeholders.Node.prototype.setBounds = function(bounds){
	this._node.set('position', bounds.position);
	this._node.set('size', bounds.size);
}

//A nodeholder for classes
visualizer.nodeholders.ClassNode = function (node){
	visualizer.nodeholders.Node.call(this, node);
	var attributes = [];
	var operations = [];
	
	//populate attributes
	_.each(node.attributes, function(attribute){
		attributes.push(this._memberToString(attribute, false));
	},this);
	
	//populate operations
	_.each(node.operations, function(operation){
		operations.push(this._memberToString(operation, true));
	},this)
	
	//JointJS model data
	var classData = {
		'name' : node.name,
		'id' : node.id,
		'attributes' : attributes,
		'methods' : operations
	}
	
	switch (node.type){
		case 'class': this._node = new visualizer.shapes.Class(classData); break;
		case 'abstract': this._node = new visualizer.shapes.Abstract(classData); break;
		default: throw new Error('Unexpected class type: ' + node.type); break;
	}
}

//prototype chaining
visualizer.nodeholders.ClassNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.ClassNode.prototype.constructor = visualizer.nodeholders.ClassNode;


//returns the textual representation of the class member passed 
//(if isOperation is true then member will be handled as an operation)
visualizer.nodeholders.ClassNode.prototype._memberToString = function(member, isOperation){
    var memberString = visualizer.Utils.MAPS.VISIBILITY_MAP[member.visibility] + ' ' +member.name;
	if (isOperation){
		var argsString = member.args.map(function (arg){
			return arg.name + ' : ' + arg.type;
		}).join(', ');
		memberString +=  '(' + argsString + ')';
		if (_.has(member, 'returnType')){
			memberString += ' : ' + member.returnType;
		}
	}
	else{
		memberString += ' : ' + member.type;
	}
	return memberString;
}

//A nodeholder for states
visualizer.nodeholders.StateNode = function(node){
	visualizer.nodeholders.Node.call(this, node);
	var nodeData = {
		'name' : node.name,
		'id' : node.id
	}
	this._node = new visualizer.shapes.State(nodeData);
	
}

//prototype chaining
visualizer.nodeholders.StateNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.StateNode.prototype.constructor = visualizer.nodeholders.StateNode;

//A nodeholder for pseudo states which can not be scaled (currently only the initial)
visualizer.nodeholders.NonScalablePseudoStateNode = function(node){
	visualizer.nodeholders.Node.call(this, node);
	
	//abstract size is always 1
	this._gridSize = {
		'width' : 1,
		'height' : 1
	}
	
	// JointJS model data
	var nodeData = {
		'attrs' : {
			'text':{
				'text':node.name
			}
		},
		'id' : node.id
	}
	
	switch (node.kind){
		case 'initial': this._node = new visualizer.shapes.StartState(nodeData); break;
		default: throw new Error('Unexpected nonscalable pseudostate type: ' + node.kind); break;
	}
	
}

//prototype chaining
visualizer.nodeholders.NonScalablePseudoStateNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.NonScalablePseudoStateNode.prototype.constructor = visualizer.nodeholders.NonScalablePseudoStateNode;

//sets the pixel size and position for the non-scaling pseudo state based on bounds argument
visualizer.nodeholders.NonScalablePseudoStateNode.prototype.setBounds = function(bounds){
	var size = this._node.get('size');
	
	//translate to the center of the cell
	var target = bounds.position;
	target.x += bounds.size.width / 2 - size.width / 2;
	target.y += bounds.size.height / 2 - size.height / 2;
	
	this._node.set('position', target);
}

//corrects the position of the pseudo state so the route connecting to it will be orthogonal
visualizer.nodeholders.NonScalablePseudoStateNode.prototype.correctGridBounds = function(anchor, routeFirst){
	//determine shift based on the abstract connection point and the first turning point
	var position = {};
	if (anchor.x > routeFirst.x){
		position.x = anchor.x + 1;
		position.y = anchor.y;
	}else if (anchor.x < routeFirst.x){
		position.x = anchor.x - 1;
		position.y = anchor.y;
	}else if (anchor.y > routeFirst.y){
		position.x = anchor.x;
		position.y = anchor.y + 1;
	}else{
		position.x = anchor.x;
		position.y = anchor.y - 1;
	}
	this._gridPosition = anchor;
	
}

// A nodeholder for pseudo states which can be scaled
visualizer.nodeholders.ScalablePseudoStateNode = function(node){
	visualizer.nodeholders.Node.call(this, node);
	var nodeData = {
		'attrs' : {
			'text':{
				'text':node.name
			}
		},
		'id' : node.id
	}
	this._node = new visualizer.shapes.Choice(nodeData);
	
}

//prototype chaining
visualizer.nodeholders.ScalablePseudoStateNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.ScalablePseudoStateNode.prototype.constructor = visualizer.nodeholders.ScalablePseudoStateNode;
