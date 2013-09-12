window.org_issg_ibis_client_js_LeafletMap =
	
function() {
    // Create the component
    var leafletMap =
        new leafletLibrary.LeafletMap(this.getElement());
    
    // Handle changes from the server-side
    this.onStateChange = function() {
        leafletMap.fitBounds(this.getState().bounds);
        leafletMap.setSpecies(this.getState().species);
    };

    // Pass user interaction to the server-side
    var connector = this;
    leafletMap.click = function() {
        connector.onClick(leafletMap.getValue());
    };
    
    
};