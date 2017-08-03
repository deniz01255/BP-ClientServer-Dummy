package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.OverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RoutingServerAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;

import org.osmdroid.util.GeoPoint;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by Vincent on 03.08.2017.
 */

public class NearestRoadsOverlayBuilder  implements INearestRoadsOverlayBuilder {

    private NearestRoadsOverlay roadsOverlay;
    private MainActivity context;
    protected Response response = null;
    protected LinkedList<Road> roads = null;

    public NearestRoadsOverlayBuilder(MainActivity context){
        this.context = context;
        roadsOverlay = new NearestRoadsOverlay();
    }

    @Override
    public NearestRoadsOverlay build() {

        //http://overpass-api.de/api/interpreter

        roadsOverlay.nearestRoads = new LinkedList<>();

        return roadsOverlay;
    }



    @Override
    public INearestRoadsOverlayBuilder setRadius(int radius) {
        this.roadsOverlay.radius = radius;
        return this;
    }

    @Override
    public INearestRoadsOverlayBuilder setCenter(GeoPoint center) {
        this.roadsOverlay.center = center;

        return this;
    }

    @Override
    public INearestRoadsOverlayBuilder setTypes(String typeExpression) {
        this.roadsOverlay.highwayTypes = typeExpression;
        return this;
    }




}
