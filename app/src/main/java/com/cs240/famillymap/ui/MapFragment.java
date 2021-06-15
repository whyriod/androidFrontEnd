package com.cs240.famillymap.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs240.famillymap.R;

import com.cs240.famillymap.model.DataCache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private DataCache cache;
    private View eventInformation;
    private TextView eventName;
    private TextView eventDesc;
    private ImageView eventImage;
    private String currentPerson;
    protected Marker focusMarker;
    protected String eventID;

    //Google Colors
    private static final int googleWhite = 0xffffffff;
    private static final int googleOrange = 0xffF57F17;
    private static final int googleGreen = 0xff81C784;



    /***
     * Sets up a new google map fragment and necessary functionality.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Iconify.with(new FontAwesomeModule());

        //Wire widgest
        cache = DataCache.getInstance();
        eventName = view.findViewById(R.id.event_name);
        eventDesc = view.findViewById(R.id.event_desc);
        eventImage = view.findViewById(R.id.event_image);
        eventInformation = view.findViewById(R.id.event_information);

        //Onclick so that eventInformation switches to personActivity
        eventInformation.setOnClickListener(v -> {
            Intent person = new Intent(getActivity(), PersonActivity.class);
            person.putExtra("PersonID",currentPerson);
            startActivity(person);
        });
        eventInformation.setEnabled(false);

        return view;
    }


    /***
     * Once the Map is ready, set up the onclick listener.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(marker -> {
            setFocus(marker);
            return true;
        });
        map.setOnMapLoadedCallback(this);
    }


    /***
     * If there is a marker to focus on (via eventActivity), move the map to it.
     */
    @Override
    public void onMapLoaded() {
        populateMap();
        if(focusMarker != null){
            setFocus(focusMarker);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Reload lines to be correct
        reset();
        reloadLines();
    }



    /***
     * For each item in the filteredEventMap (filtered via the filter functions to to a change
     * in settings from the settings Activity), display the event on the map.
     */
    private void populateMap(){

        Map<String, Event> events = cache.getFilterEventMap();
        Map<String, Float> eventColors = cache.geteventColors();
        Map<String, Marker> mapMarkers = new HashMap<>();
        Event event;

        //For each item, set up a marker, and correctly color code it.
        for(String s: events.keySet()){
            event = events.get(s);
            LatLng newll = new LatLng(event.getLatitude(),events.get(s).getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(newll)
                    .icon(BitmapDescriptorFactory.defaultMarker(eventColors.get(event.getEventType()))));
            marker.setTag(event);
            mapMarkers.put(event.getEventID(),marker);
        }
        cache.setMapMarkers(mapMarkers);
    }



    /***
     * If there is a marker to focus on, populate the view below the googleMap fragment with
     * pertinent information, and move the camera to that marker.
     */
    public void setFocus(Marker marker){
        //true for good, false for bad.
        Event e = (Event) marker.getTag();
        eventID = e.getEventID();
        Map<String, Person> people = cache.getFilterPeopleMap();
        Person p = people.get(e.getPersonID());

        eventName.setText(p.getFirstName() + " " + p.getLastName() + ": " +  e.getEventType());
        eventDesc.setText(e.getCity() + ", " + e.getCountry() + " (" + e.getYear() + ")");

        if(p.getGender().equals("m")){
            Drawable genderIcon =
                    new IconDrawable(getActivity(),FontAwesomeIcons.fa_male)
                            .colorRes(R.color.blue).sizeDp(40);
            eventImage.setImageDrawable(genderIcon);
        }
        else if(p.getGender().equals("f")){
            Drawable genderIcon =
                    new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                            .colorRes(R.color.red).sizeDp(40);
            eventImage.setImageDrawable(genderIcon);
        }

        reloadLines();
        currentPerson = p.getPersonID();
        eventInformation.setEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
    }


    /***
     * First clear all pre-existing lines. Then check the settings and draw lines
     * that are set to be drawn.
     */
    private void reloadLines(){
        clearLines();

        //Make sure an event is selected.
        if(eventID != null){
            if(cache.isLifeStoryLines()){
                drawStory(eventID);
            }
            if(cache.isFamilyTreeLines()){
                drawFamily(eventID);
            }
            if(cache.isSpouseLines()){
                drawSpouse(eventID);
            }
        }
    }



    /***
     * Remove all lines, and clear the arraylist.
     */
    void clearLines(){
        ArrayList<Polyline> lines = cache.getMaplines();
        for(Polyline p: lines){
            p.remove();
        }
        lines.clear();
    }


    /***
     * This simple helper function only extracts out the personID before passing it onto
     * the real function to being the familyTree drawing process.
     *
     * @param eventID - selected Event.
     */
    private void drawFamily(String eventID){
        String personID = cache.getEventsMap().get(eventID).getPersonID();
        drawTree(personID, eventID, 15);
    }



    /***
     * Begins the recursion process with the currently selected event. If the person has a
     * father or mother, draw a line between them. drawLine will recursively call drawTree
     * to continue up the family tree.
     *
     * @param personID - personId of selected Event.
     * @param eventID - eventID of selected Event.
     */
    private void drawTree(String personID, String eventID,int width){
        Person person = cache.getFilterPeopleMap().get(personID);
        if(person.getFatherID() != null){
            drawLine(person.getFatherID(), eventID, width);
        }
        if(person.getMotherID() != null){
            drawLine(person.getMotherID(), eventID, width);
        }
    }


    /***
     * Checks to see if there is an event that can be drawable;
     *
     * @param personID
     */
    private void drawLine(String personID,String eventID,int width){
        List<Event> events = cache.getFilterEvents().get(personID);
        //Do they have events?
        if(events != null){
            Event first = cache.getEventsMap().get(eventID);
            Event second = events.get(0);
            //Do they have a first event?
            if(second != null){
                Marker marker2 = cache.getMapMarkers().get(second.getEventID());
                //Is there a marker for said event?
                if(marker2 != null){
                    Polyline poly = newPolyline(first,second,googleGreen);
                    poly.setWidth(width);
                    cache.addLine(poly);
                    drawTree(personID,second.getEventID(),(width-3));
                }
            }
        }
    }



    /***
     * Find the person, and found their spouse. If they have a spouse, find the first event.
     * If they have a first event, then draw a line to it.
     * @param eventID
     */
    private void drawSpouse(String eventID){
        String personID = cache.getEventsMap().get(eventID).getPersonID();
        String spouseID  = cache.getFilterPeopleMap().get(personID).getSpouseID();
        //Is there a spouse?
        if(spouseID != null){
            List<Event> events = cache.getFilterEvents().get(spouseID);
            //Do they have events?
            if(events != null){
                Event first = cache.getEventsMap().get(eventID);
                Event second = events.get(0);
                //Do they have a first event?
                if(second != null){
                    Marker marker2 = cache.getMapMarkers().get(second.getEventID());
                    //Is there a marker for said event?
                    if(marker2 != null){
                        Polyline poly = newPolyline(first,second,googleWhite);
                        cache.addLine(poly);
                    }
                }
            }
        }
    }



    /***
     * Takes the current id and finds their events. It then draws lines in order from the most
     * recent to the next most recent.
     *
     * @param eventID - Id of selected event.
     */
    private void drawStory(String eventID){
        String personID = cache.getEventsMap().get(eventID).getPersonID();
        List<Event> lifeEvents = cache.getFilterEvents().get(personID);

        if(lifeEvents.size() > 1){
            for(int i = 0; i < lifeEvents.size()-1; i++){
                Marker marker1 = cache.getMapMarkers().get(lifeEvents.get(i).getEventID());
                Marker marker2 = cache.getMapMarkers().get(lifeEvents.get(i+1).getEventID());
                Event first = (Event) marker1.getTag();
                Event second = (Event) marker2.getTag();

                Polyline poly = newPolyline(first,second,googleOrange);
                cache.addLine(poly);
            }
        }
    }



    /***
     * Helper fuction to draw a poloyline.
     *
     * @param first - First event (get coordinates)
     * @param second - Second event (get coordinates)
     * @param color - Color to set line to.
     *
     * @return - The new polyline.
     */
    private Polyline newPolyline(Event first, Event second, int color){
        return map.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(
                        new LatLng(first.getLatitude(), first.getLongitude()),
                        new LatLng(second.getLatitude(), second.getLongitude())).color(color));
    }


    /***
     * If the settings are changed, reset the text and image at the bottom of the screen.
     */
    protected void reset(){
        eventName.setText(R.string.event_name);
        eventDesc.setText(R.string.event_desc);
        eventImage.setImageResource(R.drawable.ic_baseline_location_on_24);

    }

    protected void setFocusMarker(Marker marker){
        focusMarker = marker;
    }
}
