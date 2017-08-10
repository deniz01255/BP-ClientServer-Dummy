package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts;

import org.osmdroid.util.GeoPoint;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by vincent on 8/10/17.
 */

public class MainNomatimAPI {

    /**
     * The base URL where to send the api requests. Append the path to the proper API resource
     */
    public static String baseURL = "http://nominatim.openstreetmap.org/";


    /**
     * If the api key requires an apikey, load the api key to this variable.
     * NOTE: It is not recommended to publish API Keys with the source code.
     * API keys should be loaded from an external secret config file.
     * Encrypting the secret config file is recommended.
     */
    public static String apiKey = "key-not-loaded";

    public static String getLocationSuggestions(String query) {


        try {
            return "/search/p=" + URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }


}
