package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.appstate;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IOperatorState;

/**
 * Created by Vincent on 08.08.2017.
 */

public class StateHandler {

    private IOperatorState activeOperator;

    private String title;


    public StateHandler(){

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
