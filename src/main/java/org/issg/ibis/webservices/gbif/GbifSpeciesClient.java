package org.issg.ibis.webservices.gbif;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.issg.ibis.domain.json.GbifSpecies;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GbifSpeciesClient  {

    protected static final String UTF_8 = "UTF-8";

    private static String redlistGbifURL = "http://api.gbif.org/v1/species?datasetKey=19491596-35ae-4a91-9a98-85cf505f1bd3&sourceId=";
	private static String baseSuggestURL = "http://api.gbif.org/v1/species/suggest?rank=species&q=";
	private static String baseSpeciesURL = "http://api.gbif.org/v1/species/";
	
	private Gson gson = new Gson();

	public List<GbifSpecies> getSuggestions(String search)  {
		
            System.out.println("Search: " + search);
            String jsonText = "[]";
			try {
				jsonText = queryGbif(search);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            Type listType = new TypeToken<ArrayList<GbifSpecies>>(){}.getType();

            List<GbifSpecies> l = new Gson().fromJson(jsonText, listType);
			return l;
            
	}


	private String queryGbif(String search) throws IOException,
			MalformedURLException {

		search = URLEncoder.encode(search, UTF_8);
		return readURL(baseSuggestURL + search);
	}
	
	/**
	 * 
	 * @param rd
	 * @return
	 * @throws IOException
	 */
    protected static String readURL(String url) throws IOException {
		InputStream is = new URL(url).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName(UTF_8)));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		GbifSpeciesClient c = new GbifSpeciesClient();
		c.getSuggestions("kerear");
	}


	public GbifSpecies getSpecies(String url) throws IOException {
        String rwUrl = url.replace("www", "api");
        rwUrl = rwUrl.replace("species", "v1/species");

        String json = readURL(rwUrl);
        GbifSpecies sp = gson.fromJson(json, GbifSpecies.class);
        return sp;
	}
	
	public void getVernacularNames(String speciesURL) {
        try {
			String json = readURL(speciesURL + "/vernacularNames");
			System.out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public GbifSpecies getSpeciesFromRedlist(String value) {
        String json;
		try {
			System.out.println(redlistGbifURL + value);
			json = readURL(redlistGbifURL + value);
			
			json = json.substring(54, json.length()-2);
			System.out.println(json);
			
			GbifSpecies sp = gson.fromJson(json, GbifSpecies.class);
			return sp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
