package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts;

import org.osmdroid.util.GeoPoint;

/**
 * Alternative Overpass API.
 * <p>
 * Use this in case the main API is down.
 */
public class RamplerOverpassAPI {

    /**
     * The base URL where to send the api requests. Append the path to the proper API resource
     */
    public static String baseURL = "http://overpass.osm.rambler.ru";

    /**
     * The Path to the Stairs Resource on the routing server
     */
    public static String stairsResource = "/cgi/interpreter";

    /**
     * If the api key requires an apikey, load the api key to this variable.
     * NOTE: It is not recommended to publish API Keys with the source code.
     * API keys should be loaded from an external secret config file.
     * Encrypting the secret config file is recommended.
     */
    public static String apiKey = "key-not-loaded";

    public static String getNearestHighwaysPayload(GeoPoint p, int radius) {

        String payload = "(way(around:" + radius + "," + p.getLatitude() + "," + p.getLongitude() + ")[highway~\"^(.*)$\"];>;);out;";

        return payload;
    }
}
