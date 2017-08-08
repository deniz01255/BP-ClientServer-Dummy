package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.MainOverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RamplerOverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.DefaultNearestRoadsDirector;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlay;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.OsmParser;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IOperatorState;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.Polyline.OnClickListener;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class does not implement an Operator Interface - Not beautiful..
 * This is just the onClick Listener for the overlay.
 * In fact, one can argue that we need both Operators at the same time..
 * Naming this class PlaceObstacleOperatorState leads to a less cluttered file structure..
 * In addition, semantically this behaves like an operator for the user.
 * <p>
 * Previous Step: get nearest roads
 * This Step: A new Obstacle is positioned on the Overlay
 * Next Step: get Details for Obstacle
 */

public class PlaceObstacleOperatorState implements OnClickListener, IOperatorState {

    protected NearestRoadsOverlay roadsOverlay;
    protected MapEditorFragment mapEditorFragment;
    MainOverpassAPI overpassAPI = new MainOverpassAPI();
    private MainActivity mainActivity;

    public PlaceObstacleOperatorState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public boolean dispose() {
        return false;
    }

    @Override
    public NavigationBarState getNavigationState() {
        return NavigationBarState.PLACE_BARRIER;
    }

    @Override
    public String getTopBarTitle() {
        return mainActivity.getString(R.string.navigation_place_obstacle);
    }

    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
        mainActivity.mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        try {
            Point projectedPoint = mapView.getProjection().toProjectedPixels(eventPos.getLatitude(), eventPos.getLongitude(), null);
            Point finalPoint = mapView.getProjection().toPixelsFromProjected(projectedPoint, null);
            GeoPoint obstacleGeoPoint = getClosesPointOnPolyLine(mapView, polyline, finalPoint);
            if (obstacleGeoPoint == null)
                return false;

            OverlayItem newOverlayItem = new OverlayItem("", "", obstacleGeoPoint);
            if (newOverlayItem == null)
                return false;

            // TODO: Das Icon soll eindeutiger einer Position zugeordnet werden können.
            // newOverlayItem.setMarker(mapView.getContext().getResources().getDrawable(R.mipmap.ramppic));

            mainActivity.mapEditorFragment.placeNewObstacleOverlay.addItem(newOverlayItem);

            // Workaround: display the tempOverlay on top
            //mapView.getOverlays().add( mainActivity.getStateHandler().getPlaceNewObstacleOverlay());
            mainActivity.getStateHandler().setNewObstaclePosition(obstacleGeoPoint);
            mainActivity.getStateHandler().updateNavigationBarState();

            mapView.invalidate();

            Snackbar.make(mainActivity.findViewById(R.id.placeSnackBar), R.string.action_barrier_placed, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private GeoPoint getClosesPointOnPolyLine(MapView mapView, Polyline polyline, Point point) {

        if (polyline.getPoints().size() < 2)
            return null;

        GeoPoint start = null;
        GeoPoint end = null;
        GeoPoint candidate = null;
        Point candidatePoint = null;

        for (int curIndex = 0; curIndex + 1 < polyline.getPoints().size(); curIndex++) {
            start = polyline.getPoints().get(curIndex);
            end = polyline.getPoints().get(curIndex + 1);

            try {
                Point projectedPointStart = mapView.getProjection().toProjectedPixels(start.getLatitude(), start.getLongitude(), null);
                Point pointA = mapView.getProjection().toPixelsFromProjected(projectedPointStart, null);

                Point projectedPointEnd = mapView.getProjection().toProjectedPixels(end.getLatitude(), end.getLongitude(), null);
                Point pointB = mapView.getProjection().toPixelsFromProjected(projectedPointEnd, null);

                // calc closest point on line between start and end

                if (isProjectedPointOnLineSegment(pointA, pointB, point)) {
                    Point closestPointOnLine = getClosestPointOnLine(pointA, pointB, point);

                    if (candidate == null || (getDistance(candidatePoint, point) > getDistance(closestPointOnLine, point))) {
                        candidatePoint = closestPointOnLine;
                        candidate = (GeoPoint) mapView.getProjection().fromPixels(closestPointOnLine.x, closestPointOnLine.y);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return candidate;
    }

    public double getDistance(Point a, Point b) {
        double temp = Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
        return Math.sqrt(temp);

    }

    public boolean isProjectedPointOnLineSegment(Point v1, Point v2, Point p) {
        Point e1 = new Point(v2.x - v1.x, v2.y - v1.y);
        int recAreaToTest = dot(e1, e1);

        Point e2 = new Point(p.x - v1.x, p.y - v1.y);
        double value = dot(e1, e2);
        return (value > 0 && value < recAreaToTest);
    }

    public boolean isProjectedPointOnLineSegment(Point v1, Point v2, Point p, double tolerance) {
        Point e1 = new Point(v2.x - v1.x, v2.y - v1.y);
        int recAreaToTest = dot(e1, e1);

        Point e2 = new Point(p.x - v1.x, p.y - v1.y);
        double value = dot(e1, e2);
        return ((value > 0 + tolerance || value > 0 - tolerance) && (value < recAreaToTest - tolerance || value < recAreaToTest + tolerance));
    }

    /**
     * Search closest point between the line from pointA to pointB and pointC
     */
    private Point getClosestPointOnLine(Point v1, Point v2, Point p) {

        // get dot product of e1, e2
        Point e1 = new Point(v2.x - v1.x, v2.y - v1.y);
        Point e2 = new Point(p.x - v1.x, p.y - v1.y);
        double valDp = dot(e1, e2);

        double lenLineE1 = Math.sqrt(e1.x * e1.x + e1.y * e1.y);
        double lenLineE2 = Math.sqrt(e2.x * e2.x + e2.y * e2.y);
        double cos = valDp / (lenLineE1 * lenLineE2);

        double projLenOfLine = cos * lenLineE2;
        Point p2 = new Point((int) (v1.x + (projLenOfLine * e1.x) / lenLineE1),
                (int) (v1.y + (projLenOfLine * e1.y) / lenLineE1));
        return p2;
    }

    private int dot(Point v1, Point v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    @Override
    public boolean longPressHelper(GeoPoint p, MainActivity mainActivity, MapEditorFragment mapEditorFragment) {

        this.mapEditorFragment = mapEditorFragment;
        DefaultNearestRoadsDirector roadsDirector = new DefaultNearestRoadsDirector(new NearestRoadsOverlayBuilder());
        roadsOverlay = roadsDirector.construct(p);

        PlaceObstacleOperatorState.GetHighwaysFromOverpassAPITask task = new PlaceObstacleOperatorState.GetHighwaysFromOverpassAPITask(mainActivity);
        task.execute(roadsOverlay.center, roadsOverlay.radius);

        return true;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment) {
        return false;
    }

    protected void processRoads(Response response) {
        if (response != null && response.isSuccessful()) {
            mapEditorFragment.map.getOverlays().removeAll(mainActivity.getStateHandler().getCurrentRoadOverlays());
            mainActivity.getStateHandler().getCurrentRoadOverlays().clear();
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                OsmParser parser = new OsmParser();
                String ss = response.body().string();
                InputSource source = new InputSource(new StringReader(ss));

                saxParser.parse(source, parser);

                roadsOverlay.nearestRoads = parser.getRoads();

                if (roadsOverlay.nearestRoads.isEmpty() || roadsOverlay.nearestRoads.getFirst().getRoadPoints().isEmpty())
                    return;

                for (Road r : roadsOverlay.nearestRoads) {

                    Polyline polyline = new Polyline();
                    polyline.setPoints(r.getRoadPoints());
                    polyline.setColor(Color.BLACK);
                    polyline.setWidth(18);
                    polyline.setOnClickListener(this);

                    mainActivity.getStateHandler().getCurrentRoadOverlays().add(polyline);

                }
                for (Polyline p : mainActivity.getStateHandler().getCurrentRoadOverlays()) {
                    mapEditorFragment.map.getOverlays().add(p);

                }
                mapEditorFragment.map.getOverlays().remove(mainActivity.mapEditorFragment.placeNewObstacleOverlay);
                mapEditorFragment.map.getOverlays().add(mainActivity.mapEditorFragment.placeNewObstacleOverlay);

                mapEditorFragment.map.invalidate();

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            View contextView = mapEditorFragment.getActivity().findViewById(R.id.placeSnackBar);
            Snackbar.make(contextView, R.string.server_response_roads_loaded, Snackbar.LENGTH_SHORT)
                    .show();
            return;
        } else {
            return;
        }
    }

    class GetHighwaysFromOverpassAPITask extends AsyncTask<Object, Object, Response> {
        ProgressDialog progressDialog;
        private MainActivity activity;

        GetHighwaysFromOverpassAPITask(MainActivity activity) {

            this.activity = activity;
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.setMessage("Lade Straßen in der nähe..");
            this.progressDialog.show();
        }

        @Override
        protected Response doInBackground(Object... params) {

            GeoPoint p = (GeoPoint) params[0];
            int radius = (int) params[1];
            DownloadObstaclesTask task = new DownloadObstaclesTask();
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), RamplerOverpassAPI.getNearestHighwaysPayload(p, radius));

            Request request = new Request.Builder()
                    .url(RamplerOverpassAPI.baseURL + RamplerOverpassAPI.stairsResource)
                    .method("POST", body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (!response.isSuccessful()) {
                //TODO: handle unsuccessful server responses
                System.out.print("This line exists only for break pointability ;P ... Server responded not successfully");
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            processRoads(result);

        }
    }

}
