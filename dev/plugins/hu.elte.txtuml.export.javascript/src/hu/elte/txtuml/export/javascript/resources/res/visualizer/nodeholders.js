visualizer.nodeholders = {}
// This is an abstract base class for wrapper classes around JointJS Node
// models, and abstract layout informations
visualizer.nodeholders.Node = function (node) {
	if (this.constructor === visualizer.nodeholders.Node) {
		throw new Error("Can't instantiate abstract class!");
	}
	this._id = node.id;
	this._size = {
		'width': node.width,
		'height': node.height
	};
}

// returns the node layout descriptor ID
visualizer.nodeholders.Node.prototype.getID = function () {
	return this._id;
}

// returns the JointJS model
visualizer.nodeholders.Node.prototype.getNode = function () {
	return this._node;
}

// A nodeholder for classes
visualizer.nodeholders.ClassNode = function (node) {
	visualizer.nodeholders.Node.call(this, node);
	var attributes = [];
	var operations = [];

	// populate attributes
	_.each(node.attributes, function (attribute) {
		attributes.push(this._memberToString(attribute, false));
	}, this);

	// populate operations
	_.each(node.operations, function (operation) {
		operations.push(this._memberToString(operation, true));
	}, this)

	// JointJS model data
	var classData = {
		'name': node.name,
		'id': node.id,
		'attributes': attributes,
		'methods': operations,
		'position': node.position,
		'size': this._size
	}
	console.log(classData);

	switch (node.type) {
	case 'class':
		this._node = new visualizer.shapes.Class(classData);
		break;
	case 'abstract':
		this._node = new visualizer.shapes.Abstract(classData);
		break;
	default:
		throw new Error('Unexpected class type: ' + node.type);
		break;
	}
}

// prototype chaining
visualizer.nodeholders.ClassNode.prototype = Object
	.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.ClassNode.prototype.constructor = visualizer.nodeholders.ClassNode;

// returns the textual representation of the class member passed
// (if isOperation is true then member will be handled as an operation)
visualizer.nodeholders.ClassNode.prototype._memberToString = function (member,
	isOperation) {
	var memberString = visualizer.Utils.MAPS.VISIBILITY_MAP[member.visibility]
		 + ' ' + member.name;
	if (isOperation) {
		var argsString = member.args.map(function (arg) {
				return arg.name + ' : ' + arg.type;
			}).join(', ');
		memberString += '(' + argsString + ')';
		if (_.has(member, 'returnType')) {
			memberString += ' : ' + member.returnType;
		}
	} else {
		memberString += ' : ' + member.type;
	}
	return memberString;
}

// A nodeholder for states
visualizer.nodeholders.StateNode = function (node) {
	visualizer.nodeholders.Node.call(this, node);
	var nodeData = {
		'name': node.name,
		'id': node.id,
		'position': node.position,
		'size': this._size
	}
	this._node = new visualizer.shapes.State(nodeData);

}

// prototype chaining
visualizer.nodeholders.StateNode.prototype = Object
	.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.StateNode.prototype.constructor = visualizer.nodeholders.StateNode;

// A nodeholder for pseudo states which can not be scaled (currently only the
// initial)
visualizer.nodeholders.PseudoStateNode = function (node) {
	visualizer.nodeholders.Node.call(this, node);

	// JointJS model data
	var nodeData = {
		'attrs': {
			'text': {
				'text': node.name
			}
		},
		'id': node.id,
		'position': node.position,
		'size': this._size
	}

	switch (node.kind) {
	case 'initial':
		this._node = new visualizer.shapes.StartState(nodeData);
		break;
	case 'choice':
		this._node = new visualizer.shapes.Choice(nodeData);
		break;
	default:
		throw new Error('Unexpected pseudostate type: ' + node.kind);
		break;
	}

}

// prototype chaining
visualizer.nodeholders.PseudoStateNode.prototype = Object
	.create(visualizer.nodeholders.Node.prototype);
visualizer.nodeholders.PseudoStateNode.prototype.constructor = visualizer.nodeholders.PseudoStateNode;
