visualizer.Font = function(font){
	this._font = font;
};

visualizer.Font.prototype.getContainerBoxSize = function(text, widthPadding, heightPadding){
	return {
		'width' : text.length * this._font.containerWidth + widthPadding * 2,
		'height' : this._font.containerHeight + heightPadding * 2
	}
}

visualizer.Font.prototype.getFamily = function(){
	return this._font.family;
}

visualizer.Font.prototype.getSize = function(){
	return this._font.size;
}

visualizer.Font.prototype.getContainerHeight = function(padding){
	return this._font.containerHeight + padding * 2;
}

visualizer.Font.prototype.getContainerWidth = function(padding){
	return this._font.containerWidth + padding * 2;
}

visualizer.Font.FONTDATA = {	
	'default':{
		'family': '"Lucida Console", Monaco, monospace',
		'size': 14,
		'containerWidth': 8.8,
		'containerHeight': 14
	},
	'links':{
		'family': '"Lucida Console", Monaco, monospace',
		'size': 14,
		'containerWidth': 8.8,
		'containerHeight': 14
	},
	'pseudostates':{
		'family': '"Lucida Console", Monaco, monospace',
		'size': 12,
		'containerWidth': 7.2,
		'containerHeight': 12
	}
}

visualizer.Fonts = {};
_.each(visualizer.Font.FONTDATA, function(font, fontName){
	visualizer.Fonts[fontName] = new visualizer.Font(font);
});