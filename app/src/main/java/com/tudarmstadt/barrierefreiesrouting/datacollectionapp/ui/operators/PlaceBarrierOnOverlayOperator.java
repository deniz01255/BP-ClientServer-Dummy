package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import android.graphics.Point;
import android.graphics.Rect;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.Polyline.OnClickListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class does not implement an Operator Interface - Not beautiful..
 * This is just the onClick Listener for the overlay.
 * In fact, one can argue that we need both Operators at the same time..
 * Naming this class PlaceBarrierOnOverlayOperator leads to a less cluttered file structure..
 * In addition, semantically this behaves like an operator for the user.
 * <p>
 * Previous Step: get nearest roads
 * This Step: A new Obstacle is positioned on the Overlay
 * Next Step: get Details for Obstacle
 */

public class PlaceBarrierOnOverlayOperator implements OnClickListener {

    private OverlayItem overlayItem;
    private ItemizedOverlayWithFocus setNewObstaclePositionOverlay;

    public PlaceBarrierOnOverlayOperator(ItemizedOverlayWithFocus setNewObstaclePositionOverlay, OverlayItem overlayItem) {

        this.overlayItem = overlayItem;
        this.setNewObstaclePositionOverlay = setNewObstaclePositionOverlay;
    }

    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
        setNewObstaclePositionOverlay.removeAllItems();

        Point projectedPoint = mapView.getProjection().toProjectedPixels(eventPos.getLatitude(), eventPos.getLongitude(), null);
        Point finalPoint = mapView.getProjection().toPixelsFromProjected(projectedPoint, null);

        OverlayItem newOverlayItem = new OverlayItem(overlayItem.getTitle(), overlayItem.getTitle(), getClosesPointOnPolyLine(mapView, polyline, finalPoint));


        if(newOverlayItem == null)
            return false;

        overlayItem.setMarker(mapView.getContext().getResources().getDrawable(R.mipmap.ramppic));

        setNewObstaclePositionOverlay.addItem(newOverlayItem);
        overlayItem = newOverlayItem;

        // Workaround: display the tempOverlay on top
        mapView.getOverlays().remove(setNewObstaclePositionOverlay);
        mapView.getOverlays().add(setNewObstaclePositionOverlay);

        mapView.invalidate();

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

            Point projectedPointStart = mapView.getProjection().toProjectedPixels(start.getLatitude(), start.getLongitude(), null);
            Point pointA = mapView.getProjection().toPixelsFromProjected(projectedPointStart, null);

            Point projectedPointEnd = mapView.getProjection().toProjectedPixels(end.getLatitude(), end.getLongitude(), null);
            Point pointB = mapView.getProjection().toPixelsFromProjected(projectedPointEnd, null);

            // calc closest point on line between start and end

            if(isProjectedPointOnLineSegment(pointA, pointB, point, 0.1)){
                Point closestPointOnLine = getClosestPointOnLine(pointA, pointB, point);
                if(candidate == null || getDistance(candidatePoint, point) < getDistance(closestPointOnLine, point) ){
                    candidatePoint = closestPointOnLine;
                    candidate = (GeoPoint) mapView.getProjection().fromPixels(closestPointOnLine.x, closestPointOnLine.y);
                }
            }

        }
        return candidate;
    }

    public double getDistance(Point a, Point b){
        try{
            return Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y) );

        }catch (Exception e){
            return 0;
        }
    }

    public boolean isProjectedPointOnLineSegment(Point v1, Point v2, Point p, double tolerance)
    {
        Point e1 = new Point(v2.x - v1.x, v2.y - v1.y);
        int recAreaToTest = dot(e1, e1);

        Point e2 = new Point(p.x - v1.x, p.y - v1.y);
        double value = dot(e1, e2);
        return ((value > 0+tolerance || value > 0 - tolerance) && (value < recAreaToTest - tolerance || value < recAreaToTest + tolerance) );
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
        Point p2 = new Point((int)(v1.x + (projLenOfLine * e1.x) / lenLineE1),
                (int)(v1.y + (projLenOfLine * e1.y) / lenLineE1));
        return p2;
    }
    

    private int dot(Point v1, Point v2){
        return v1.x * v2.x + v1.y * v2.y;
    }
}
