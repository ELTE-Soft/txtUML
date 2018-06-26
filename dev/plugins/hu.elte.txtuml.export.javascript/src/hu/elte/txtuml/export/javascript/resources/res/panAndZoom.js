var svgZoom = svgPanZoom('#paper > svg', {
		center: true,
		zoomEnabled: true,
		panEnabled: true,
		controlIconsEnabled: false,
		dblClickZoomEnabled: false,
		fit: false,
		minZoom: 0.1,
		maxZoom:3,
		zoomScaleSensitivity: 0.5
	});

function fitMaxHeightToParent(element){
	var parent = element.parent();
	element[0].style.maxHeight = (parent.innerHeight()) + 'px';
}

//replace the generated height to fit available space
var canvas = $("#paper > svg");
fitMaxHeightToParent(canvas);
canvas.removeAttr("height");

//attach zoom controls
document.getElementById('zoom-in').addEventListener('click', function(ev){
	svgZoom.zoomIn()
});

document.getElementById('zoom-out').addEventListener('click', function(ev){
	svgZoom.zoomOut()
});

document.getElementById('reset-view').addEventListener('click', function(ev){
	svgZoom.reset()
});

//center diagram
svgZoom.resize();
svgZoom.center();

//add mouse pan and wheel zoom toggle control
document.getElementById('toggle-pan').addEventListener('click', function(ev){
	if(panEnabled){
		svgZoom.disablePan();
	}
	else{
		svgZoom.enablePan()
	}
	panEnabled = !panEnabled;
});

var wheelZoomEnabled = true;
document.getElementById('toggle-wheel-zoom').addEventListener('click', function(ev){
	if(wheelZoomEnabled){
		svgZoom.disableMouseWheelZoom();
	}
	else{
		svgZoom.enableMouseWheelZoom()
	}
	wheelZoomEnabled = !wheelZoomEnabled;
});
