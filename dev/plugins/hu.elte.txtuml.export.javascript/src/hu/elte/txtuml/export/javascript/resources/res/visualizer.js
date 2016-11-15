var selector = new visualizer.Selector(input);
selector.putLinks($('#selector'));
var diagram = selector.getSelectedDiagram();
_visualizer = null;
switch (diagram.type){
	case 'class' : _visualizer = new visualizer.visualizers.CDVisualizer(diagram, 50);break;
	case 'state' : _visualizer = new visualizer.visualizers.SMVisualizer(diagram, 50);break;
}
_visualizer.visualize($('#paper'));

