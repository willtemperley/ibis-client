window.org_issg_ibis_client_js_OpenLayersMap =
	
function() {
    // Create the component
    var openLayersMap =
        new openLayersLibrary.OpenLayersMap(this.getElement());
    
    // Handle changes from the server-side
    this.onStateChange = function() {
        openLayersMap.fitBounds(this.getState().bounds);
    };

    // Pass user interaction to the server-side
    var connector = this;
    openLayersMap.click = function() {
        connector.onClick(openLayersMap.getValue());
    };
    
    
};