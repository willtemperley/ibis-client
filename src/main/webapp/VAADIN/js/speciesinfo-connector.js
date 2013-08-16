window.org_issg_ibis_client_js_SpeciesInfo =
function() {
    // Create the component
    var mycomponent =
        new speciesLibrary.SpeciesInfo(this.getElement());
    
    // Handle changes from the server-side
    this.onStateChange = function() {
        mycomponent.setSpecies(this.getState().species);
    };

    // Pass user interaction to the server-side
    var connector = this;
    mycomponent.click = function() {
        connector.onClick(mycomponent.getSpecies());
    };
};