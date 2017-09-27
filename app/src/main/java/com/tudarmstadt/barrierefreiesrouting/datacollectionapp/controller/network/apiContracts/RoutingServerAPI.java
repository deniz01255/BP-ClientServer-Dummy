package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts;

/**
 * API Contract for the BP Routing server web api.
 * <p>
 * Calls to the obstacleResource
 * POST: send a new obstacle
 * GET: get all obstacles
 */
public class RoutingServerAPI {

    /**
     * The base URL where to send the api requests. Append the path to the proper API resource
     */
    public static String baseURL = "http://routing.vincinator.de";

    /**
     * The Path to the Stairs Resource on the routing server
     */
    public static String obstacleResource = "/api/barriers/";

    public static String roadResource = "/api/barriers/ways";

    /**
     * If the api key requires an apikey, load the api key to this variable.
     * NOTE: It is not recommended to publish API Keys with the source code.
     * API keys should be loaded from an external secret config file.
     * Encrypting the secret config file is recommended.
     */
    public static String apiKey = "key-not-loaded";

}
