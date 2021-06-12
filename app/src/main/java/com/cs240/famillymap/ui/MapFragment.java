package com.cs240.famillymap.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cs240.famillymap.R;

import com.cs240.famillymap.model.DataCache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import model.Event;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private DataCache cache;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cache = DataCache.getInstance();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //true for good, false for bad.
                System.out.println("You clicked");
                Event e = (Event) marker.getTag();
                System.out.println(e.getCity());
                return true;
            }
        });
        map.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        populateMap();
    }

    private void populateMap(){

        Map<String, Event> events = cache.getEventsMap();
        Event event;

        for(String s: events.keySet()){
            event = events.get(s);
            LatLng newll = new LatLng(event.getLatitude(),events.get(s).getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(newll));
            marker.setTag(event);
        }
    }
}
