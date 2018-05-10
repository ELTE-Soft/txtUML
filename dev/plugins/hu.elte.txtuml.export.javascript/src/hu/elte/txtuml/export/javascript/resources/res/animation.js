var currentActiveElements = [];

function deactivactivateElement(element){
    element.removeAttr("active");
}

function activactivateElement(element){
    element.attr("active","");
}

function clearCurrentActiveElements(){
    currentActiveElements.forEach((element) => {
        deactivactivateElement(element);
    });
    currentActiveElements = [];
}

/**
 * Sets the elements identified by their 'model-id' attributes as the currently active elements.
 * Does not account for multiple instances of the same element.
 * Previously active elements will be deactivated.
 * 
 * @param  {Array<String>} ids
 *          example: ids = ["car.model.Engine", "car.model.Ignition"]
 *          example 2: ids = ["pingpong.j.model.Racket.Check"]
 */
function setActiveElements(ids){
    clearCurrentActiveElements();
    ids.forEach((id) => {
        element = $("#paper g[model-id='" + id + "']");
        activactivateElement(element);
        currentActiveElements.push(element);
    });
}

function refreshElements(){
	$.ajax({
		url: 'http://localhost:' + DIAGNOSTICS_PORT + '/' + DIAGNOSTICS_PATH,
	    type: 'GET',
	    dataType: 'json'
	}).complete(function(response){
        if(response.status == 200){
            setActiveElements((JSON.parse(response.responseText)).map( e => e.element));
        }
        else{
            setActiveElements([]);
        }
	});
}

function pollingRefresh(){
    refreshElements();
    setTimeout(pollingRefresh, REFRESH_INTERVAL_IN_MILISECONDS);
}

pollingRefresh();