package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacleViews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

public class ObstacleSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obstacle_selection);
    }

    public void openStairs(View view) {
        Intent intent = new Intent(this, selectionStairs.class);
        startActivity(intent);
    }

}
