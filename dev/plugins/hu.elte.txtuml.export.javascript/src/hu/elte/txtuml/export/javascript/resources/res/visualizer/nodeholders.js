visualizer.nodeholders = {}
// This is an abstract base class for wrapper classes around JointJS Node
// models, and abstract layout informations
visualizer.nodeholders.Node = function (node) {
	if (this.constructor === visualizer.nodeholders.Node) {
		throw new Error("Can't instantiate abstract class!");
	}
	this._name = node.id;
	this._size = {
		'width': node.width,
		'height': node.height
	};
}

// returns the fully qualified name of the enclosed node element
visualizer.nodeholders.Node.prototype.getElementName = function () {
	return this._name;
}

// returns the id of the enclosed node shape
visualizer.nodeholders.Node.prototype.getShapeId = function () {
	return this._node.id;
};

// returns the JointJS model
visualizer.nodeholders.Node.prototype.getNode = function () {
	return this._node;
}

// adds modifier CSS classes to visualized nodes
visualizer.nodeholders.Node.prototype.addModifierClasses = function(paper) {}

// A nodeholder for classes
visualizer.nodeholders.ClassNode = function (node) {
	visualizer.nodeholders.Node.call(this, node);
	var attributes = [];
	var operations = [];
	this._attrClasses = [];
	this._opClasses = [];

	// populate attributes and compute their modifier CSS classes
	_.each(node.attributes, function (attribute) {
		attributes.push(this._memberToString(attribute, false));
		this._attrClasses.push(visualizer.Utils.getModifierClasses(attribute));
	}, this);

	// populate operations and compute their modifier CSS classes
	_.each(node.operations, function (operation) {
		operations.push(this._memberToString(operation, true));
		this._opClasses.push(visualizer.Utils.getModifierClasses(operation));
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

// adds modifier CSS classes to visualized class nodes
visualizer.nodeholders.ClassNode.prototype.addModifierClasses = function(paper) {
	var clazz = this;
	var box = $(paper.findViewByModel(clazz._node).el);

	box.find('.uml-class-attrs-text').find('.v-line').each(function(key, tspan) {
		V(tspan).addClass(clazz._attrClasses[key]);
	});

	box.find('.uml-class-methods-text').find('.v-line').each(function(key, tspan) {
		V(tspan).addClass(clazz._opClasses[key]);
	});
}

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
