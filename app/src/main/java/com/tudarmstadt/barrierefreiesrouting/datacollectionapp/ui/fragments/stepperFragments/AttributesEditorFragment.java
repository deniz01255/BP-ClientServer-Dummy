package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.SelectedObstacleTypeChangedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.utils.ObstacleTranslator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bp.common.model.obstacles.Construction;
import bp.common.model.obstacles.Elevator;
import bp.common.model.obstacles.FastTrafficLight;
import bp.common.model.obstacles.Obstacle;
import bp.common.model.obstacles.Stairs;
import bp.common.model.obstacles.TightPassage;
import bp.common.model.obstacles.Unevenness;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.AttributeFragmentFactory.insertAttributeEditFragments;
import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleToViewConverter.convertObstacleToViewModel;

/**
 * This Fragment holds a Obstacle Type dropdown selection (a spinner)
 * <p>
 * Selecting an item from the spinner, the current Obstacle Type is updated in the
 * ObstacleDataSingleton. This Obstacle Type is used to initialize the Attribute List
 * via the AttributeFragmentFactory.
 */
public class AttributesEditorFragment extends Fragment implements Step {

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stepfragment_attributes_edit, container, false);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            Spinner spinner = (Spinner) view.findViewById(R.id.spinner_obstacle_selection);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.BARRIER_TYPES, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (ObstacleDataSingleton.getInstance().getObstacle() != null)
                spinner.setSelection(ObstacleTranslator.getSpinnerPositionFromType(ObstacleDataSingleton.getInstance().getObstacle().getTypeCode()));


            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // Send Event that Selection of Obstacle has changed.
                    // HINT: BrowseMapActivity subscribes to this Event.
                    EventBus.getDefault().post(new SelectedObstacleTypeChangedEvent(getObstacleFrom(position)));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ObstacleViewModel obstacleViewModel = convertObstacleToViewModel(ObstacleDataSingleton.getInstance().getObstacle(), getActivity());
            ObstacleDataSingleton.getInstance().setObstacleViewModel(obstacleViewModel);

        } catch (InflateException e) {

        }
        return view;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public VerificationError verifyStep() {

        return null;
    }

    @Override
    public void onSelected() {
        if (!ObstacleDataSingleton.getInstance().editorIsSyncedWithSelection) {
            // this.obstacleViewModel must be initialized first.
            //insertAttributeEditFragments(this);

            ObstacleDataSingleton.getInstance().editorIsSyncedWithSelection = true;
        }

    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {

    }


    private Obstacle getObstacleFrom(int pos) {

        Obstacle result;
        switch (String.valueOf(pos)) {
            case "0":
                result = new Stairs();
                break;
            case "2":
                result = new Unevenness();
                break;
            case "3":
                result = new Construction();
                break;
            case "4":
                result = new FastTrafficLight();
                break;
            case "5":
                result = new Elevator();
                break;
            case "6":
                result = new TightPassage();
                break;
            default:
                result = new Stairs();
                break;

        }
        result.setLatitudeEnd(ObstacleDataSingleton.getInstance().currentEndPositionOfSetObstacle.getLatitude());
        result.setLongitudeEnd(ObstacleDataSingleton.getInstance().currentEndPositionOfSetObstacle.getLongitude());

        result.setLatitudeStart(ObstacleDataSingleton.getInstance().currentStartingPositionOfSetObstacle.getLatitude());
        result.setLongitudeStart(ObstacleDataSingleton.getInstance().currentStartingPositionOfSetObstacle.getLongitude());
        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectedObstacleTypeChangedEvent event) {

        ObstacleViewModel obstacleViewModel = convertObstacleToViewModel(event.getObstacle(), getActivity());
        ObstacleDataSingleton.getInstance().setObstacleViewModel(obstacleViewModel);

        ObstacleDataSingleton.getInstance().editorIsSyncedWithSelection = false;
        insertAttributeEditFragments(this);


    }

}
