package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces;

/**
 * Operators are used by the User (or a Service) in order to change the application state.
 *
 * Operators are initialized before they are ready for usage.
 * Before a new Operator can start, the currently active Operator must be disposed.
 */
public interface IOperator {

    boolean init();

    boolean dispose();

}
