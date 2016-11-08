var selector = new visualizer.Selector(input);
selector.putLinks($('#selector'));

var graph = new joint.dia.Graph();




var diagram = selector.getSelectedDiagram();
var classes = [];
var relations = [];
var labels = [];



$.each(diagram.classes, function (key, clazz) {
    classes.push(new visualizer.nodeholders.ClassNode(clazz));
});

$.each(diagram.attributeLinks, function (key, link) {
    relations.push(new visualizer.linkholders.ClassAttributeLink(link));
});
$.each(diagram.nonAttributeLinks, function (key, link) {
    relations.push(new visualizer.linkholders.ClassNonAttributeLink(link));
});


var grid = new visualizer.Grid(classes,relations,50);

var totalSize = grid.getTotalPixelSize();

var paper = new joint.dia.Paper({
    'el': $('#paper'),
    'width': totalSize.width,
    'height': totalSize.height,
    'gridSize': 1,
    'model': graph,
    'perpendicularLinks': true,
    'linkView': visualizer.shapes.AttributeAssociationView,
	'interactive': { labelMove: true }
});

$.each(classes, function (key, clazz) {
	var bounds = grid.getPixelBounds(clazz.getGridPosition(), clazz.getGridSize());
	clazz.setBounds(bounds);
	graph.addCell(clazz.getNode());
	clazz.addMemberClasses(paper);


});



$.each(relations, function (key, relation) {
	
	var route = grid.translateRoute(relation.getRoute());
	relation.setPixelRoute(route);
	graph.addCell(relation.getLink());

});
