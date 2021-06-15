package com.cs240.famillymap.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.cs240.famillymap.R;
import com.cs240.famillymap.model.DataCache;
import com.google.android.gms.maps.model.Marker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import model.Event;

//Handles when someone selects a person in the person activity
public class EventActivity extends AppCompatActivity {

    private String eventID;
    private MapFragment mapFragment;
    private DataCache cache;



    /***
     * Setup a new mapfragment, and set the focus to the event passed in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //Setup Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cache = DataCache.getInstance();

        //Get caller arguments.
        Intent intent = getIntent();
        eventID = intent.getStringExtra("EventID");
        mapFragment = new MapFragment();

        //Set Marker focus if the event exists.
        if(cache.getMapMarkers().get(eventID) != null){
            mapFragment.setFocusMarker(cache.getMapMarkers().get(eventID));
        }

        //Add the new Fragment.
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.eventFragmentContainer,mapFragment)
                .commit();
    }
}