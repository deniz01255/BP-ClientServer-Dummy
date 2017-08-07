package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IMapOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.DefaultMapOperator;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import bp.common.model.Obstacle;

public class MapEditorFragment extends Fragment implements MapEventsReceiver {

    public MyLocationNewOverlay mLocationOverlay;
    public MapView map;
    public ItemizedOverlayWithFocus<OverlayItem> obstacleOverlay;
    public ItemizedOverlayWithFocus<OverlayItem> placeNewObstacleOverlay;

    private MapEventsOverlay evOverlay;
    private RoadManager roadManager;
    private IMapController mapController;
    private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    private OnFragmentInteractionListener mListener;
    public IMapOperator activeMapOperator;

    // Leerer Constructor wird benötigt
    public MapEditorFragment() {
    }

    public static MapEditorFragment newInstance(Obstacle obs) {
        MapEditorFragment fragment = new MapEditorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activeMapOperator = new DefaultMapOperator(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_editor_fragment, container, false);
        MainActivity activity = (MainActivity) getActivity();

        Context context = activity.getApplicationContext();

        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.setClickable(true);

        this.evOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(evOverlay);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
        this.mLocationOverlay.enableMyLocation();
        mLocationOverlay.setDrawAccuracyEnabled(false);
        map.getOverlays().add(this.mLocationOverlay);

        // Auf der Startposition wird die Map zentriert - 49.8728, 8.6512 => Luisenplatz ;)
        GeoPoint startPoint = new GeoPoint(49.8728, 8.6512);
        mapController = map.getController();
        mapController.setCenter(startPoint);
        map.setMaxZoomLevel(21);

        map.getController().setZoom(19);
        map.setTilesScaledToDpi(true);

        mLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                map.getController().animateTo(mLocationOverlay.getMyLocation());
            }
        });

        obstacleOverlay = new ItemizedOverlayWithFocus<>(activity, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        placeNewObstacleOverlay = new ItemizedOverlayWithFocus<>(activity, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int i, OverlayItem overlayItem) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(int i, OverlayItem overlayItem) {
                        return false;
                    }
                });

        obstacleOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(obstacleOverlay);
        map.getOverlays().add(placeNewObstacleOverlay);

        v.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return activeMapOperator.singleTapConfirmedHelper(p, (MainActivity) getActivity(), this);
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return activeMapOperator.longPressHelper(p, (MainActivity) getActivity(), this);
    }

    public void addObstacle(OverlayItem overlayItem) {
        obstacleOverlay.addItem(overlayItem);
    }

    public void refresh() {
        map.invalidate();

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
