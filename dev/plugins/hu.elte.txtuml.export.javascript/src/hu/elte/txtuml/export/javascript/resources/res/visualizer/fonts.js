// A helper class which holds information and provides measuring functions for fonts (currently only supporting monospace fonts)
visualizer.Font = function (font) {
	this._font = font;
};

// returns the given text estimated size with the given padding
visualizer.Font.prototype.getContainerBoxSize = function (text, widthPadding, heightPadding) {
	if (typeof widthPadding === 'undefined')
		widthPadding = 0;
	if (typeof heightPadding === 'undefined')
		heightPadding = 0;
	return {
		'width': text.length * this._font.containerWidth + widthPadding * 2,
		'height': this._font.containerHeight + heightPadding * 2
	}
}

// returns the font's family
visualizer.Font.prototype.getFamily = function () {
	return this._font.family;
}

// returns the font's size
visualizer.Font.prototype.getSize = function () {
	return this._font.size;
}

// returns a single letter's maximum height
visualizer.Font.prototype.getContainerHeight = function (padding) {
	return this._font.containerHeight + padding * 2;
}

// returns a single letter's maximum width
visualizer.Font.prototype.getContainerWidth = function (padding) {
	return this._font.containerWidth + padding * 2;
}

// font informations
visualizer.Font.FONTDATA = {
	'default': {
		'family': '"Lucida Console", Monaco, monospace',
		'size': 14,
		'containerWidth': 8.8,
		'containerHeight': 14
	},
	'links': {
		'family': '"Lucida Console", Monaco, monospace',
		'size': 14,
		'containerWidth': 8.8,
		'containerHeight': 14
	},
	'pseudostates': {
		'family': '"Lucida Console", Monaco, monospace',
		'size': 12,
		'containerWidth': 7.2,
		'containerHeight': 12
	}
}

// collection of instantiated Fonts
visualizer.Fonts = {};

// populates visualizer.Fonts
_.each(visualizer.Font.FONTDATA, function (font, fontName) {
	visualizer.Fonts[fontName] = new visualizer.Font(font);
});
