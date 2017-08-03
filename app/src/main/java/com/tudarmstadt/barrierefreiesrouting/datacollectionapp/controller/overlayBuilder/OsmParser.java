package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;

/**
 * Created by Vincent on 03.08.2017.
 */

public class OsmParser extends DefaultHandler {
    boolean bNode = false;
    boolean bNote = false;
    boolean bTag = false;
    boolean bMarks = false;

    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase("node")) {
            String id = attributes.getValue("id");
            String lat = attributes.getValue("lat");
            String lon = attributes.getValue("lon");

            System.out.println("id : " + id);
            System.out.println("lat : " + lat);
            System.out.println("lon : " + lon);

        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("node")) {
            System.out.println("Node :" + qName);

        }
    }

    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {

    }

}
