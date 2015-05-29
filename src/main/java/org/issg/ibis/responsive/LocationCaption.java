package org.issg.ibis.responsive;

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocationCaption {

	public static final String LOCATION_BUNDLE = "LocationBundle";
	public static final String COUNTRY_BUNDLE = "CountryBundle";

	private final Format[] formats;
	private final ResourceBundle bundle;
	
	public static LocationCaption getInstance(Locale currentLocale, String bundleName) {
		return new LocationCaption(currentLocale, bundleName);
	}

	LocationCaption(Locale currentLocale, String bundleName) {

		if (currentLocale == null) {
			currentLocale = Locale.getDefault();
		}

		bundle = ResourceBundle.getBundle(bundleName,
				currentLocale);

		MessageFormat messageForm = new MessageFormat("");
		messageForm.setLocale(currentLocale);

		double[] fileLimits = { 0, 1, 2 };
		String[] fileStrings = { bundle.getString("zero"),
				bundle.getString("one"), bundle.getString("multiple") };

		// fileLimits and fileStrings are mapped to each other.
		ChoiceFormat choiceForm = new ChoiceFormat(fileLimits, fileStrings);


		formats = new Format[]{ choiceForm, null, NumberFormat.getInstance() };
		
	}

	public String getMessage(String areaName, int numLocs) {
		Object[] messageArguments = { numLocs, areaName, numLocs };

		String patternType;
		if(areaName == null) {
			patternType = "pattern0";
		} else {
			patternType = "pattern1";
		}
		String pattern = bundle.getString(patternType);

		MessageFormat messageForm = new MessageFormat("");
		messageForm.applyPattern(pattern);
		messageForm.setFormats(formats);
		String result = messageForm.format(messageArguments);

		return result;
	}
	
}
