package org.issg.ibis.webservices;

import org.issg.ibis.domain.Species;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Quick hack for getting gbif JSON
 * 
 * @author Will Temperley
 * 
 */
public class GbifApi09 extends BaseJsonClient {

    private static String REDLIST_URL_TEMPLATE = "http://api.gbif.org/v0.9/species?datasetKey=19491596-35ae-4a91-9a98-85cf505f1bd3&sourceId=%s";

    private static String OCCURRENCE_URL_TEMPLATE = "http://api.gbif.org/v0.9/species/%s";

    public static void populateSpeciesFromGbifUri(Species species) {

        String url = species.getUri();
        url = rewriteGbifURI(url);
        JSONObject spJson = readJsonFromUrl(url);
        populateSpecies(species, spJson);

    }

    public static void populateSpeciesFromRedlistId(Species species) {

        try {

            Integer rlId = species.getRedlistId();
            String url = String.format(REDLIST_URL_TEMPLATE, rlId);
            JSONObject json = readJsonFromUrl(url);

            prettyPrint(json.toString());
            // return json.toString();

            JSONArray jsonArray = (JSONArray) json.get("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject res = (JSONObject) jsonArray.get(i);
                populateSpecies(species, res);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void populateSpecies(Species species, JSONObject spJson) {
        try {

            Integer nubKey = null;
            if (spJson.has("nubKey")){
                nubKey = spJson.getInt("nubKey");
                species.setNubKey(Long.valueOf(nubKey));
            } else if (spJson.has("speciesKey")) {
                nubKey = spJson.getInt("speciesKey");
                species.setNubKey(Long.valueOf(nubKey));
            }

            String authority = spJson.getString("authorship");
            species.setAuthority(authority);

            species.setGbifJson(spJson.toString());
            
            if (nubKey != null) {
                String gbifUri = "http://www.gbif.org/species/" + nubKey;
                if (species.getUri() == null || species.getUri().isEmpty()) {
                    species.setUri(gbifUri);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        // String g = "http://www.gbif.org/species/3056367";
        // String url = rewriteGbifURI(g);
        // System.err.println("GBIF");
        // getSpecies(url);

        int redlistId = 133558;

        Species sp = new Species();
        sp.setRedlistId(redlistId);
        populateSpeciesFromRedlistId(sp);
    }

    /*
     * 
     */
    private static String rewriteGbifURI(String url) {
        String rwUrl = url.replace("www", "api");
        rwUrl = rwUrl.replace("species", "v0.9/species");
        return rwUrl;
    }

}
