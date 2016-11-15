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
	console.log('Setting bounds for: ' + this._id);
	this._node.set('position', bounds.position);
	//this._node.attr('rect/width', bounds.size.width); //TODO: find better fix for IE
	this._node.set('size',bounds.size);
	console.log('Set bounds for: ' + this._id);
	//this._node.updateRectangles();
	

	//this._node.attr('rect',bounds.size);
}


visualizer.nodeholders.ClassNode = function (node){
	var clazz = this;
	visualizer.nodeholders.Node.call(clazz, node);
	var attributes = [];
	var operations = [];
	clazz._attrClasses = [];
	clazz._opClasses = [];
	$.each(node.attributes, function(key, attribute){
		attributes.push(clazz._memberToString(attribute, false));
		clazz._attrClasses.push([attribute.visibility].concat(attribute.modifiers).join(' '))
	})
	$.each(node.operations, function(key, operation){
		operations.push(clazz._memberToString(operation, true));
		clazz._opClasses.push([operation.visibility].concat(operation.modifiers).join(' '))
	})
	var classData = {
		'name' : node.name,
		'id' : node.id,
		'attributes' : attributes,
		'methods' : operations
	}
	switch (node.type){
		case 'class': clazz._node = new visualizer.shapes.Class(classData); break;
		//case 'interface': clazz._node = new visualizer.shapes.Interface(classData); break;
		case 'abstract': clazz._node = new visualizer.shapes.Abstract(classData); break;
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

visualizer.nodeholders.ClassNode.prototype.addMemberClasses = function(paper){
	var clazz = this;
	var box = $(paper.findViewByModel(clazz._node).el);
	box.find('.uml-class-attrs-text').find('.v-line').each(function(key, tspan){
		V(tspan).addClass(clazz._attrClasses[key]);
	});
	
	box.find('.uml-class-methods-text').find('.v-line').each(function(key, tspan){
		V(tspan).addClass(clazz._opClasses[key]);
	})
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
		'id' : node.id,
		'size':{
			'width':30,
			'height':30			
		}
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
	console.log('ok');
	
}

/*visualizer.nodeholders.NonScalablePseudoStateNode.prototype.getGridPosition = function(){
	var x = this._gridPosition.x + this._gridSize.width / 4;  //TODO: teszt
	var y = this._gridPosition.y - this._gridSize.height / 4;
	console.log(x,y);
	return {
		'x' : x,
		'y' : y
	}

}*/

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
	switch (node.kind){
		case 'initial': this._node = new visualizer.shapes.StartState(nodeData);
		case 'choice': this._node = new visualizer.shapes.Choice(nodeData);
	}
	
}


visualizer.nodeholders.ScalablePseudoStateNode.prototype = Object.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.ScalablePseudoStateNode.prototype.constructor = visualizer.nodeholders.ScalablePseudoStateNode;
