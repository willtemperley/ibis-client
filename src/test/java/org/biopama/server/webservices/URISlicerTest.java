package org.biopama.server.webservices;

import org.biopama.ibis.webservices.DownloadDescriptor;
import org.biopama.ibis.webservices.URISlicer;
import org.junit.Assert;
import org.junit.Test;

public class URISlicerTest {

//	@Test
	public void speciesOccurrence() {
		
		URISlicer urlSlicer = new URISlicer("/ibis-client");
		
		DownloadDescriptor d = urlSlicer.getDownload("/ibis-client/download/species/42/occurrence/xlsx");

		Assert.assertEquals(d.getFormat(), "xlsx");
		Assert.assertEquals(d.getEntityType(),"species");
		Assert.assertEquals(d.getEntityId(), new Long(42));

	}
}
