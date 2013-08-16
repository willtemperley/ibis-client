//Define the namespace
var speciesLibrary = speciesLibrary || {};

speciesLibrary.SpeciesInfo = function (element) {
    this.element = element;

    // Getter and setter for the value property
    this.getSpecies = function () {
        return this.element.
            getElementsByTagName("input")[0].value;
    };
    this.setSpecies = function (value) {
    	
    	if (value != null) {
    		var obj = JSON.parse(value);
    		var speciesName = obj['name'];
    		var commonName = obj['commonName'];
    		var imgUrl = obj['imgUrl'];
    		var redlistImgUrl = obj['redlistImgUrl'];
		    this.element.innerHTML =
		        "<div>" + speciesName + "</div>" +
		        "<div>" + commonName + "</div>" +
		        "<div><img src='" + imgUrl + "'/></div>" +
		        "<div><img src='" + redlistImgUrl + "'/></div>";
    		
    	}
    	
    };

    // Default implementation of the click handler
    this.click = function () {
        alert("Error: Must implement click() method");
    };

    // Set up button click
    var button = this.element;
    var component = this; // Can't use this inside the function
    button.onclick = function () {
        component.click();
    };
};

