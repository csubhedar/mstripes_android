package com.practice.mstripesapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by This Pc on 06-06-2017.
 */
public class chowki_details extends AppCompatActivity {
    private String mchowkiId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView datetextView= (TextView) findViewById(R.id.date);
        String currentDate= new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String displayDate="Date:   "+currentDate;
        datetextView.setText(displayDate);
        Button mstart = (Button) findViewById(R.id.start);
        mstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),map_display.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {super.onResume();}

    @Override
    protected void onStop()
    {
        super.onStop();
    }


}
