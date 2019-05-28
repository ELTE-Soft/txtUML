//include after animation.js

var errorLabelElement = $('#debug-port-error');
var debugContainer = $('#debug-toggle-container');
var confirmedAnimationDelay = 1.0;

//try to load from sessionStorage
var currentPort = sessionStorage['diagnosticsPort'];
$('#debug-port-input').val(currentPort);

var isPolling = false;
if(sessionStorage['isPolling'] == "true") {
	isPolling = true;
	$('#toggle-debug-checkbox').attr("checked", false);
	pollingRefresh();
}

/**
 * Queries the server for diagnostics information.
 * If successful, updates the diagram with `setActiveElements` from `animation.js`
 * If not, clears active elements, and alerts the user.
 */
function refreshElements(queryPort){
	$.ajax({
		url: 'http://localhost:' + queryPort + '/' + DIAGNOSTICS_PATH,
		type: 'GET',
		dataType: 'json'
	}).complete(function(response){
		if(!isPolling || currentPort != queryPort) return;

		if(response.status == 200){
			var json = JSON.parse(response.responseText);
			var activeElements = json
				.filter(diag => !selector._selected || selector._selected.inst == diag.name)
				.map(diag => _visualizer.getShapeIdByElementName(diag.location));
			//skip update if current diagram does not change
			if (activeElements.length > 0)
				setActiveElements(activeElements);

			selector.setInstances(json);
			hideError();
		}
		else{
			clearCurrentActiveElements();
			showError();
		}
	});
}

function pollingRefresh(){
	refreshElements(currentPort);
	refreshAnimationDelay(currentPort);
	if(isPolling){
		setTimeout(pollingRefresh, REFRESH_INTERVAL_IN_MILLISECONDS);
	}
}

//add event listener to the checkbox
$("#toggle-debug-checkbox").on("change", function(){
	if(!this.checked){
		//on
		isPolling = true;
		sessionStorage['isPolling'] = true;
		pollingRefresh();
	}
	else{
		//off
		isPolling = false;
		sessionStorage['isPolling'] = false;
		hideError();
		clearCurrentActiveElements();
	}
});

function showError(){
	errorLabelElement.slideDown();
}

function hideError(){
	errorLabelElement.slideUp();
}

var typingTimer;
var DONE_TYPING_TIMEOUT_IN_MILLISECONDS = 500;

function doneTyping(){
	hideError();
	clearCurrentActiveElements();

	currentPort = $('#debug-port-input').val();
	sessionStorage['diagnosticsPort'] = currentPort;
}

//add event listener to the debug input
$('#debug-port-input').on('keyup change input', function(){
	clearTimeout(typingTimer);
	if($('#debug-port-input').val()){
		typingTimer = setTimeout(doneTyping, DONE_TYPING_TIMEOUT_IN_MILLISECONDS);
	}
});

//send animation delay to the server
function sendAnimationDelay(animationDelayToSend){
	$.ajax({
		url: 'http://localhost:' + currentPort + '/delay',
		type: 'POST',
		dataType: 'json',
		data: { animationDelay: animationDelayToSend * 1000 },
		success: function(response) {}
	});
}

$('#animation-delay-slider').val(confirmedAnimationDelay);
$('#animation-delay-label').text(confirmedAnimationDelay + " sec");

//add event listener to the animation delay slider
$('#animation-delay-slider').on('input change', function(){
	if(this.value != confirmedAnimationDelay){
		var valueToSend = this.value;
		this.value = confirmedAnimationDelay;
		if(isPolling) sendAnimationDelay(valueToSend);
	}
});

function refreshAnimationDelay(queryPort){
	$.ajax({
		url: 'http://localhost:' + queryPort + '/delay',
		type: 'GET',
		dataType: 'json'
	}).complete(function(response){
		if(!isPolling || currentPort != queryPort) return;

		if(response.status == 200){
			confirmedAnimationDelay = JSON.parse(response.responseText).animationDelay / 1000;
			$('#animation-delay-slider').val(confirmedAnimationDelay);
			$('#animation-delay-label').text(confirmedAnimationDelay + " sec");
		}
	});
}
