package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.appstate;

import android.content.Context;
import android.view.MenuItem;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IOperatorState;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.DefaultMapOperatorState;

/**
 * Created by Vincent on 08.08.2017.
 */

public class StateHandler {

    private IOperatorState activeOperator;
    private Context context;

    private String AppBarTitle = "";


    public StateHandler(MainActivity context){
        this.context = context;
        activeOperator = new DefaultMapOperatorState(context);
    }
    public void setupNextState(IOperatorState nextState){

        activeOperator.dispose();
        activeOperator = nextState;
        activeOperator.init();

    }

    public IOperatorState getActiveOperator() {
        return activeOperator;
    }

    public void replaceActiveOperator(IOperatorState activeOperator) {
        this.activeOperator = activeOperator;
    }

}
