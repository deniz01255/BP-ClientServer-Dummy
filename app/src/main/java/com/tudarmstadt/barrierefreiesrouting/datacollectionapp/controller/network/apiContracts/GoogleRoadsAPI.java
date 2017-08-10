package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts;

/**
 * Created by Vincent on 01.08.2017.
 */

public class GoogleRoadsAPI {

    /**
     * The base URL where to send the api requests. Append the path to the proper API resource
     */
    public static String baseURL = "https://roads.googleapis.com/";

    /**
     * Path to the nearest Routes Resource.
     * append ?points=lat,long?key=*apikey*
     */
    public static String nearesRoutesResource = "/v1/nearestRoads";

    /**
     * If the api key requires an apikey, load the api key to this variable.
     * NOTE: It is not recommended to publish API Keys with the source code.
     * API keys should be loaded from an external secret config file.
     * Encrypting the secret config file is recommended.
     */
    public static String apiKey = "AIzaSyAZfrKvAgPmHvi4t70-UYMBOMGOQxV7m7I";

}
