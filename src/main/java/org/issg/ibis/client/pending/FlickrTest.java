package org.issg.ibis.client.pending;

import java.io.IOException;

import org.json.JSONException;

import com.google.inject.Singleton;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.SearchParameters;

@Singleton
public class FlickrTest {

    private Flickr flickr;

    public static void main(String[] args) {
        try {
            new FlickrTest();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FlickrException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public FlickrTest() throws IOException, FlickrException, JSONException {

        flickr = new Flickr("6d3e521646cdd1391a6dee32d8e54d62");

    }

    public String getSingleImageUrl(String searchText) {
        SearchParameters params = new SearchParameters();
        params.setText(searchText);

        PhotoList x;
        try {
            x = flickr.getPhotosInterface().search(params, 1, 1);
            if (x.size() == 1) {
                return x.get(0).getSmall320Url();
            }
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FlickrException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
