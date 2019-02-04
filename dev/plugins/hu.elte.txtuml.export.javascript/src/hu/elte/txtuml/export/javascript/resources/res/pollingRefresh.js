//include after animation.js

var errorLabelElement = $('#debug-port-error');
var debugContainer = $('#debug-toggle-container');
var stateMachineDelay = $('#animation-delay-slider').val();

//try to load from sessionStorage
var port = sessionStorage['diagnosticsPort'];
$('#debug-port-input').val(port);

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
function refreshElements(){
	$.ajax({
		url: 'http://localhost:' + port + '/' + DIAGNOSTICS_PATH,
		type: 'GET',
		dataType: 'json'
	}).complete(function(response){
		if(response.status == 200 && isPolling){
			setActiveElements((JSON.parse(response.responseText))
				.map(entry => _visualizer.getShapeIdByElementName(entry.location)));
			hideError();
		}
		else{
			clearCurrentActiveElements();
			if(isPolling){
				showError();
			}else{
				hideError();
			}
		}
	});
}

function pollingRefresh(){
	refreshElements();
	refreshDelayInput();
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
	port = $('#debug-port-input').val();
	sessionStorage['diagnosticsPort'] = port;
}

//add event listeners to the debug input
$('#debug-port-input').on('keyup change input', function(){
	clearTimeout(typingTimer);
	if($('#debug-port-input').val()){
		typingTimer = setTimeout(doneTyping, DONE_TYPING_TIMEOUT_IN_MILLISECONDS);
	}
});

//send animation delay to the server
function sendSMTime(){
	$.ajax({
		url: 'http://localhost:' + port + '/delay',
		type: 'POST',
		dataType: 'json',
		data: {delayTime:stateMachineDelay},
	    success: function(response) {
        	if(response.status == 200 && isPolling){
			}
    	}
	});
}

//add event listeners to the state machine's delay range
$('#animation-delay-slider').on('input change', function(){
	stateMachineDelay = this.value;
	$('#animation-delay-input')[0].value = parseInt(this.value);
	$('#animation-delay-slider')[0].value = parseInt(this.value);
	sendSMTime();
});

//add event listeners to the state machine's delay number input
$('#animation-delay-input').on('input change', function(){
	stateMachineDelay = this.value;
	$('#animation-delay-slider')[0].value = parseInt(this.value);
	$('#animation-delay-input')[0].value = parseInt(this.value); 
	sendSMTime();
});

function refreshDelayInput(){
	$.ajax({
		url: 'http://localhost:' + port + '/delayInput',
		type: 'GET',
		dataType: 'json'
	}).complete(function(response){
		if(response.status == 200 && isPolling){
			var data = JSON.parse(response.responseText);
			stateMachineDelay = parseInt(data[0].delayTime);
			$('#animation-delay-slider')[0].value = parseInt(data[0].delayTime);
			$('#animation-delay-input')[0].value = parseInt(data[0].delayTime);
		}
	});
}