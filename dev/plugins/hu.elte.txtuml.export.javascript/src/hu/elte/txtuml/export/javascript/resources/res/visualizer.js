//this script starts the visualization

try {
	var selector = new visualizer.Selector(input); // create selector
	selector.putLinks($('#selector')); // place links to other diagrams
	var diagram = selector.getSelectedDiagram(); //get selected diagram
	_visualizer = null;

	//create suitable diagram visualizer
	switch (diagram.type) {
	case 'class':
		_visualizer = new visualizer.visualizers.CDVisualizer(diagram, 50);
		break;
	case 'state':
		_visualizer = new visualizer.visualizers.SMVisualizer(diagram, 50);
		break;
	default:
		throw Error('Unexpected diagram type: ' + diagram.type);
	}

	//visualize diagram
	_visualizer.visualize($('#paper'));
} catch (err) {
	//notify user if error happens
	alert('Error during visualization:\n\n' + err);
	throw err;
}
