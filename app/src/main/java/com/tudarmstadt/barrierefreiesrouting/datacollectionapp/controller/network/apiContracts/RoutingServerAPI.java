package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts;

/**
 * Created by Vincent on 01.08.2017.
 */

public class RoutingServerAPI {

    /**
     * The base URL where to send the api requests. Append the path to the proper API resource
     */
    public static String baseURL = "https://routing.vincinator.de";

    /**
     * The Path to the Stairs Resource on the routing server
     */
    public static String stairsResource = "/api/barriers/stairs";

    /**
     * If the api key requires an apikey, load the api key to this variable.
     * NOTE: It is not recommended to publish API Keys with the source code.
     * API keys should be loaded from an external secret config file.
     * Encrypting the secret config file is recommended.
     */
    public static String apiKey = "key-not-loaded";

}
