package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacleViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacles.Stairs;

public class selectionStairs extends AppCompatActivity {

    private Stairs stairs;
    EditText etNumberOfStairs, etHeightOfStairs;
    CheckBox cbHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_stairs);

        stairs = new Stairs();
        etHeightOfStairs = (EditText)findViewById(R.id.editText2);
        etNumberOfStairs = (EditText)findViewById(R.id.editText3);
        cbHandle = (CheckBox)findViewById(R.id.checkBox);

    }

    /** Called when the user clicks the send Data Button **/
    public void onClickSendData(View view) {
        int height = Integer.parseInt(etHeightOfStairs.getText().toString());
        int number = Integer.parseInt(etNumberOfStairs.getText().toString());
        boolean handle = cbHandle.isChecked();
        stairs.setHeightOfStairs(height);
        stairs.setNumberOfStairs(number);
        stairs.setHandleAvailable(handle);
        Gson gson = new Gson();
        Log.v("Object stairs in JSON", gson.toJson(this.stairs));
    }
}
