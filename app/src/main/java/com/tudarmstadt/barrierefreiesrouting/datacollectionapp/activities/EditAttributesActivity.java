package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

public class EditAttributesActivity extends AppCompatActivity {


    private LinearLayout myLayout;

    private LinearLayout.LayoutParams defaultParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attributes);
        myLayout = (LinearLayout) findViewById(R.id.EditViewList);

        Bundle extras = getIntent().getExtras();


        defaultParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        defaultParams.setMargins(0, 40, 0, 40);

        addEditText();
        addCheckbox();
        addButton();
    }



    public void addCheckbox(){
        CheckBox myCheckbox = new CheckBox(this);

        myCheckbox.setLayoutParams(defaultParams);

        myCheckbox.setText("Checkbox");
        myLayout.addView(myCheckbox);

    }


    public void addEditText(){

        EditText myEditText = new EditText(this);

        myEditText.setLayoutParams(defaultParams);

        myEditText.setText("Text Edit");

        myLayout.addView(myEditText);
    }

    public void addButton(){

        Button myButton = new Button(this);
        myButton.setLayoutParams(defaultParams);

        myButton.setText("Button");

        myLayout.addView(myButton);
    }



}
