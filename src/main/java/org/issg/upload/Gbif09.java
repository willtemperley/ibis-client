package org.issg.upload;

import org.issg.ibis.domain.Species;
import org.issg.ibis.ws.BaseJsonClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * Quick hack for getting gbif JSON
 * 
 * @author Will Temperley
 * 
 */
public class Gbif09 extends BaseJsonClient {

    private static String REDLIST_TEMPLATE_URL = "http://api.gbif.org/v0.9/species?datasetKey=19491596-35ae-4a91-9a98-85cf505f1bd3&sourceId=%s";

    public static void populateSpeciesFromGbifUri(Species species) {

        String url = species.getUri();
        url = rewriteGbifURI(url);
        JSONObject spJson = readJsonFromUrl(url);
        populateSpecies(species, spJson);

    }

    public static void populateSpeciesFromRedlistId(Species species) {

        try {

            Integer rlId = species.getRedlistId();
            String url = String.format(REDLIST_TEMPLATE_URL, rlId);
            JSONObject json = readJsonFromUrl(url);

            // return json.toString();

            JSONArray jsonArray = (JSONArray) json.get("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject res = (JSONObject) jsonArray.get(i);
                populateSpecies(species, res);
            }

//            prettyPrint(json.toString());

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

    private static void prettyPrint(String uglyJSONString) {
        // TODO Auto-generated method stub

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);
    }

    public static void main(String[] args) {

        // String g = "http://www.gbif.org/species/3056367";
        // String url = rewriteGbifURI(g);
        // System.err.println("GBIF");
        // getSpecies(url);

        int redlistId = 133558;
        System.err.println("RL");

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
