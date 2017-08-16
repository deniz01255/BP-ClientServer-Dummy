package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import android.graphics.Point;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstaclePositionSelectedOnPolylineEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

import org.greenrobot.eventbus.EventBus;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

/**
 * Created by vincent on 8/16/17.
 */

public class PlaceObstacleOnPolygonListener implements Polyline.OnClickListener {


    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
        ObstacleDataSingleton.getInstance().currentPositionOfSetObstacle = null;

        try {
            Point projectedPoint = mapView.getProjection().toProjectedPixels(eventPos.getLatitude(), eventPos.getLongitude(), null);
            Point finalPoint = mapView.getProjection().toPixelsFromProjected(projectedPoint, null);

            EventBus.getDefault().post(new ObstaclePositionSelectedOnPolylineEvent(getClosesPointOnPolyLine(mapView,polyline, finalPoint)));

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
}
