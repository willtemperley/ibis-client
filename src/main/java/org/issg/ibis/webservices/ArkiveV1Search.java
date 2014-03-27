package org.issg.ibis.webservices;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArkiveV1Search extends BaseJsonClient {

    public static String getSpeciesImage(String species) {
        String url;
        try {
            url = String.format(
                    "http://www.arkive.org/api/71N4VG1CPQ/portlet/latin/%s/1",
                    URLEncoder.encode(species, UTF_8));

            JSONObject json = readJsonFromUrl(url);

            if (json == null) {
                return null;
            }

            JSONArray jsonArray = (JSONArray) json.get("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                return (String) jsonArray.get(i);
            }

            return json.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String x = ArkiveV1Search.getSpeciesImage("Vateriopsis seychellarum");
        System.out.println(x);
    }
}
