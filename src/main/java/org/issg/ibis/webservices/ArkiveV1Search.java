package org.issg.ibis.webservices;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import org.issg.ibis.domain.WebserviceCache;
import org.jrc.edit.Dao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class ArkiveV1Search extends BaseJsonClient {

	private static final int ONE_WEEK = 604800000;
	private static final String ARKIVE_PORTLET_URI = "http://www.arkive.org/api/71N4VG1CPQ/portlet/latin/%s/1";
	private Dao dao;

	private Logger logger = LoggerFactory.getLogger(ArkiveV1Search.class);

	@Inject
	public ArkiveV1Search(Dao dao) {
		this.dao = dao;
	}

	public String getSpeciesImage(String species) {
		String url;

		try {
			url = String.format(ARKIVE_PORTLET_URI,
					URLEncoder.encode(species, UTF_8));

			WebserviceCache wsc = dao.find(WebserviceCache.class, url);
			Date now = new Date();

			if (wsc != null) {
				Date ad = wsc.getAccessDate();
				if (now.getTime() - ad.getTime() < ONE_WEEK) {
					logger.info("retreived from cache");
					return wsc.getResult();
				}
			}

			/*
			 * The cache is either stale or the webservice has not been
			 * consulted yet.
			 */

			JSONObject json = readJsonFromUrl(url);

			if (json == null) {
				return null;
			}

			if (wsc == null) {
				wsc = new WebserviceCache();
			}

			JSONArray jsonArray = (JSONArray) json.get("results");
			for (int i = 0; i < jsonArray.length(); i++) {
				String result = (String) jsonArray.get(i);

				wsc.setId(url);
				wsc.setResult(result);

				if (wsc.getAccessDate() != null) {
					wsc.setAccessDate(now);
					logger.info("merging");
					dao.merge(wsc);
				} else {
					wsc.setAccessDate(now);
					logger.info("persisting");
					dao.persist(wsc);
				}
				return result;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		// EntityM
		// ArchiveV1 v = new ArkiveV1Search();
		// String x =
		// ArkiveV1Search.getSpeciesImage("Vateriopsis seychellarum");
		// System.out.println(x);
	}
}
