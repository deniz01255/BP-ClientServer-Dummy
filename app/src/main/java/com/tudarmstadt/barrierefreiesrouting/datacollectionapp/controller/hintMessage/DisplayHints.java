package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.hintMessage;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by deniz on 27.09.17.
 */

public class DisplayHints {

    private Context context;

    public DisplayHints(Context context){
        this.context = context;
    }

    private void simpleHint(String title, String s){
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        builder1.setTitle("Start Hilfe");
        builder1.setMessage("Um die umliegenden Straßen \"chlickbar\" zu machen, erfordert dies ein längeres gedrückthalten des fingers von ca. 2-3sec. auf den Bildschirm");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
