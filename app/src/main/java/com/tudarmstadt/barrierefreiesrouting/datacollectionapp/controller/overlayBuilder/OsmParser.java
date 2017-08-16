package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Node;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;

import org.osmdroid.util.GeoPoint;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Parse the Response from the overpass api to a list of roads (and nodes).
 */
public class OsmParser extends DefaultHandler {

    boolean bNd = false;
    private HashMap<Long, Node> nodes = new HashMap<>();
    private LinkedList<Road> roads = new LinkedList<>();
    private Road currentRoad;
    private Node currentNode;

    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase("node")) {
            currentNode = new Node();

            currentNode.id = Long.parseLong(attributes.getValue("id"));
            double lat = Double.parseDouble(attributes.getValue("lat"));
            double lon = Double.parseDouble(attributes.getValue("lon"));
            currentNode.geoPoint = new GeoPoint(lat, lon);

        } else if (qName.equalsIgnoreCase("way")) {
            currentRoad = new Road();
            currentRoad.id = Long.parseLong(attributes.getValue("id"));

        } else if (qName.equalsIgnoreCase("nd")) {
            currentRoad.getRoadPoints().add(
                    nodes.get(Long.parseLong(attributes.getValue("ref"))).geoPoint

            );
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("node")) {
            nodes.put(currentNode.id, currentNode);

        } else if (qName.equalsIgnoreCase("way")) {
            roads.add(currentRoad);
        }
    }

    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {

    }

    public LinkedList<Road> getRoads() {
        return roads;
    }
}
