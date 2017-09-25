package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.utils;

import android.content.Context;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

import bp.common.model.ObstacleTypes;

/**
 * Translator Utilities for the Obstacle
 */
public class ObstacleTranslator {

    /**
     * Get the Localisation string for the Obstacle Type
     *
     * @param context a context to get the localisation string from
     * @param type    the type to translate
     * @return the translation
     */
    public static String getTranslationFor(Context context, ObstacleTypes type) {

        switch (type) {
            case CONSTRUCTION:
                return context.getString(R.string.obstacle_label_construction);
            case STAIRS:
                return context.getString(R.string.obstacle_label_stairs);
            case ELEVATOR:
                return context.getString(R.string.obstacle_label_elevator);
            case UNEVENNESS:
                return context.getString(R.string.obstacle_label_unevenness);
            case TIGHT_PASSAGE:
                return context.getString(R.string.obstacle_label_tight_passage);
            case FAST_TRAFFIC_LIGHT:
                return context.getString(R.string.obstacle_label_fast_traffic_light);

        }

        // Type is not specified, so just call it obstacle
        return context.getString(R.string.obstacle);
    }

    /**
     * Is Used to get the Spinner position if an Obstacle is edited by the user, so the spinner
     * can start at the correct position.
     *
     * @param type
     * @return
     */
    public static int getSpinnerPositionFromType(ObstacleTypes type) {
        switch (type) {
            case STAIRS:
                return 0;
            case UNEVENNESS:
                return 2;
            case CONSTRUCTION:
                return 3;
            case FAST_TRAFFIC_LIGHT:
                return 4;
            case ELEVATOR:
                return 5;
            case TIGHT_PASSAGE:
                return 6;

        }


        return 5;
    }

}
