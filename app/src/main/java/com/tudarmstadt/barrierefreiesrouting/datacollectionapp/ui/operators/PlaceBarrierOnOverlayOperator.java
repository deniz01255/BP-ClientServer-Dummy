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
        GeoPoint closestGeoPointOnLine = null;

        for (int curIndex = 0; curIndex + 1 < polyline.getPoints().size(); curIndex++) {
            start = polyline.getPoints().get(curIndex);
            end = polyline.getPoints().get(curIndex + 1);

            Point projectedPointStart = mapView.getProjection().toProjectedPixels(start.getLatitude(), start.getLongitude(), null);
            Point pointA = mapView.getProjection().toPixelsFromProjected(projectedPointStart, null);

            Point projectedPointEnd = mapView.getProjection().toProjectedPixels(end.getLatitude(), end.getLongitude(), null);
            Point pointB = mapView.getProjection().toPixelsFromProjected(projectedPointEnd, null);

            // calc closest point on line between start and end
            Point closestPointOnLine = getClosesPointOnLine(pointA, pointB, point);

            closestGeoPointOnLine = (GeoPoint) mapView.getProjection().fromPixels(closestPointOnLine.x, closestPointOnLine.y);

        }

        return closestGeoPointOnLine;
    }

    /**
     * Search closest point between the line from pointA to pointB and pointC
     */
    private Point getClosesPointOnLine(Point pointA, Point pointB, Point pointC) {

        // Get XY Coordinates
        //https://stackoverflow.com/questions/1369512/converting-longitude-latitude-to-x-y-on-a-map-with-calibration-points

        // Point to Line distance
        //http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html

        // closest point to another point
        // https://math.stackexchange.com/questions/717746/closest-point-on-a-line-to-another-point

        return pointA;

    }

}
