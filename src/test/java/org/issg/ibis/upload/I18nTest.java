package org.issg.ibis.upload;

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

public class I18nTest {
	@Test
	public void y() {

	}
	
	@Test
	public void x() {
		
		Locale currentLocale = Locale.US;
		ResourceBundle bundle = ResourceBundle.getBundle(
			    "ChoiceBundle", currentLocale);
		
		MessageFormat messageForm = new MessageFormat("");
		messageForm.setLocale(currentLocale);
		
		double[] fileLimits = {0,1,2};
		String [] fileStrings = {
		    bundle.getString("noFiles"),
		    bundle.getString("oneFile"),
		    bundle.getString("multipleFiles")
		};
		
		//fileLimits and fileStrings are mapped to each other.
		ChoiceFormat choiceForm = new ChoiceFormat(fileLimits, fileStrings);

		String pattern = bundle.getString("pattern");
		messageForm.applyPattern(pattern);
		
		Format[] formats = {choiceForm, null, NumberFormat.getInstance()};
		messageForm.setFormats(formats);
		
		Object[] messageArguments = {null, "XDisk", null};

		for (int numFiles = 0; numFiles < 4; numFiles++) {
		    messageArguments[0] = new Integer(numFiles);
		    messageArguments[2] = new Integer(numFiles);
		    String result = messageForm.format(messageArguments);
		    System.out.println(result);
		}


	}

}
