package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.ObstacleDetailsFragment;

/**
 * Created by Vincent on 07.08.2017.
 */

public class ObstacleDetailDialog extends DialogFragment {

    public static ObstacleDetailDialog newInstance() {
        ObstacleDetailDialog yourDialogFragment = new ObstacleDetailDialog();

        return yourDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //read the int from args

        View view = inflater.inflate(R.layout.obstacle_details_dialog, container);
        getChildFragmentManager().beginTransaction().replace(R.id.details_fragment_container, new ObstacleDetailsFragment()).commit();


        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog){


    }


}
