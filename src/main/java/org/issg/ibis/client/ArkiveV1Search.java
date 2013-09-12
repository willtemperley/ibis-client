package org.issg.ibis.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArkiveV1Search {

    private static final String UTF_8 = "UTF-8";

    public static String getSpeciesImage(String species) {
        String url;
        try {
            url = String.format(
                    "http://www.arkive.org/api/71N4VG1CPQ/portlet/latin/%s/1",
                    URLEncoder.encode(species, UTF_8));

            JSONObject json = readJsonFromUrl(url);

            if (json == null) {
                return "<div class='blank-species'/>";
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
        ArkiveV1Search av = new ArkiveV1Search();
        String x = av.getSpeciesImage("Vateriopsis seychellarum");
        System.out.println(x);
    }

    private static JSONObject readJsonFromUrl(String url) {
        InputStream is;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName(UTF_8)));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (MalformedURLException e) {
            
        } catch (IOException e) {
            
        } catch (JSONException e) {
            
        }
        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
