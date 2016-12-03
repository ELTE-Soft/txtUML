visualizer.nodeholders = {}
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

visualizer.nodeholders.Node.prototype.getID = function(){
	return this._id;
}

visualizer.nodeholders.Node.prototype.getNode = function(){
	return this._node;
}

visualizer.nodeholders.Node.prototype.getGridPosition = function(){
	return this._gridPosition;
}

visualizer.nodeholders.Node.prototype.getGridSize = function(){
	return this._gridSize;
}

visualizer.nodeholders.Node.prototype.getPixelSize = function(){
	return this._node.get('size');
}

visualizer.nodeholders.Node.prototype.setBounds = function(bounds){
	this._node.set('position', bounds.position);
	this._node.set('size', bounds.size);
}


visualizer.nodeholders.ClassNode = function (node){
	visualizer.nodeholders.Node.call(this, node);
	var attributes = [];
	var operations = [];
	_.each(node.attributes, function(attribute){
		attributes.push(this._memberToString(attribute, false));
	},this);
	
	_.each(node.operations, function(operation){
		operations.push(this._memberToString(operation, true));
	},this)
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



visualizer.nodeholders.ClassNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.ClassNode.prototype.constructor = visualizer.nodeholders.ClassNode;


visualizer.nodeholders.ClassNode.prototype._MAPS = {
	VISIBILITY_MAP : {
		'public' : '+',
		'package' : '~',
		'protected' : '#',
		'private' : '-'
	}
}

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

visualizer.nodeholders.StateNode = function(node){
	visualizer.nodeholders.Node.call(this, node);
	var nodeData = {
		'name' : node.name,
		'id' : node.id
	}
	this._node = new visualizer.shapes.State(nodeData);
	
}

visualizer.nodeholders.StateNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.StateNode.prototype.constructor = visualizer.nodeholders.StateNode;

visualizer.nodeholders.NonScalablePseudoStateNode = function(node){
	visualizer.nodeholders.Node.call(this, node);
	this._gridSize = {
		'width' : 1,
		'height' : 1
	}
	var nodeData = {
		'attrs' : {
			'text':{
				'text':node.name
			}
		},
		'id' : node.id
	}
	switch (node.kind){
		case 'initial': this._node = new visualizer.shapes.StartState(nodeData);
	}
	
}


visualizer.nodeholders.NonScalablePseudoStateNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.NonScalablePseudoStateNode.prototype.constructor = visualizer.nodeholders.NonScalablePseudoStateNode;

visualizer.nodeholders.NonScalablePseudoStateNode.prototype.setBounds = function(bounds){
	var size = this._node.get('size');
	var target = bounds.position;
	target.x += bounds.size.width / 2 - size.width / 2;
	target.y += bounds.size.height / 2 - size.height / 2;
	this._node.set('position', target);
}

visualizer.nodeholders.NonScalablePseudoStateNode.prototype.correctGridBounds = function(anchor, routeFirst){
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


visualizer.nodeholders.ScalablePseudoStateNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.ScalablePseudoStateNode.prototype.constructor = visualizer.nodeholders.ScalablePseudoStateNode;
