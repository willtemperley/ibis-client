package org.biopama.ibis.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class BaseJsonClient {
    
    private static Logger logger = LoggerFactory.getLogger(BaseJsonClient.class);

    protected static final String UTF_8 = "UTF-8";

    protected static JSONObject readJsonFromUrl(String url) {
        InputStream is;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName(UTF_8)));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (MalformedURLException e) {
            logger.debug(e.getMessage());
        } catch (IOException e) {
            logger.debug(e.getMessage());
        } catch (JSONException e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    protected static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static void prettyPrint(String uglyJSONString) {
	
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    JsonParser jp = new JsonParser();
	    JsonElement je = jp.parse(uglyJSONString);
	    String prettyJsonString = gson.toJson(je);
	    System.out.println(prettyJsonString);
	}

	public BaseJsonClient() {
        super();
    }

}