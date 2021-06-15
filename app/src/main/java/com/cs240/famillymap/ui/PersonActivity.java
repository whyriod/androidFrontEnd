package com.cs240.famillymap.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.cs240.famillymap.R;
import com.cs240.famillymap.model.DataCache;
import com.cs240.famillymap.model.Helper;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Person;

/***
 * Sets up the Person view.
 */
public class PersonActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private ExpandableListView listView;

    private DataCache cache;
    private String personID;
    private Helper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        cache = DataCache.getInstance();
        helper = new Helper();

        Intent intent = getIntent();
        Iconify.with(new FontAwesomeModule());

        firstName = this.findViewById(R.id.person_first);
        lastName = this.findViewById(R.id.person_last);
        gender = this.findViewById(R.id.person_gender);
        listView = findViewById(R.id.expandableListView);

        personID = intent.getStringExtra("PersonID");
        Person person = cache.getPeopleMap().get(personID);

        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());

        String g = person.getGender().equals("m") ? "Male" : "Female";
        gender.setText(g);

        listView.setAdapter(new ExpandableListAdapter());
    }



    /***
     * Class example was a life saver. This works, but Im not going to comment it.
     */
    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_POSITION = 0;
        private static final int PEOPLE_POSITION = 1;

        private final List<Event> events;
        private final List<Person> relatives;

        ExpandableListAdapter() {
            Person person = cache.getPeopleMap().get(personID);
            events = cache.getFilterEvents().get(personID);

            //Find Father, Mother, Spouse, and Child
            List<Person> r = new ArrayList<>();
            if(person.getFatherID() != null){
                r.add(cache.getPeopleMap().get(person.getFatherID()));
            }
            if(person.getMotherID() != null){
                r.add(cache.getPeopleMap().get(person.getMotherID()));
            }
            if(person.getSpouseID() != null){
                r.add(cache.getPeopleMap().get(person.getSpouseID()));
            }

            for(String s: cache.getPeopleMap().keySet()){
                String fatherID = person.getFatherID();
                String motherID = person.getMotherID();

                //Mother || Father
                if ((fatherID != null && fatherID.equals(personID)) ||
                    (motherID != null && motherID.equals(personID))){
                    r.add(cache.getPeopleMap().get(s));
                }
            }

            relatives = r;

        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                //Events
                case EVENT_POSITION:
                    if(events == null){
                        return 0;
                    }
                    return events.size();
                //People
                case PEOPLE_POSITION:
                    if(relatives == null){
                        return 0;
                    }
                    return relatives.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return getString(R.string.life_events);
                case PEOPLE_POSITION:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return events.get(childPosition);
                case PEOPLE_POSITION:
                    return relatives.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_POSITION:
                    titleView.setText(R.string.life_events);
                    break;
                case PEOPLE_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PEOPLE_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        private void initializeEventView(View item, final int childPosition) {
            TextView top = item.findViewById(R.id.top);
            String title = events.get(childPosition).getEventType() + ": " +
                           events.get(childPosition).getCity() + ", " +
                           events.get(childPosition).getCountry() + " (" +
                           events.get(childPosition).getYear() + ")";

            top.setText(title);
            Person person = cache.getPeopleMap().get(events.get(childPosition).getPersonID());
            TextView bottom = item.findViewById(R.id.bottom);
            bottom.setText((person.getFirstName() + " " + person.getLastName()));

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent e = new Intent(PersonActivity.this, EventActivity.class);
                    e.putExtra("EventID",events.get(childPosition).getEventID());
                    startActivity(e);
                }
            });
        }

        private void initializeFamilyView(View item, final int childPosition) {
            ImageView image = item.findViewById(R.id.item_image);
            TextView top = item.findViewById(R.id.top);
            final Person famPerson =
                    cache.getPeopleMap().get(relatives.get(childPosition).getPersonID());
            top.setText((famPerson.getFirstName() + " " + famPerson.getLastName()));

            String position;
            Person currentPerson = cache.getPeopleMap().get(personID);

            position = helper.familyRelationship(currentPerson,relatives.get(childPosition).getPersonID());

            if(famPerson.getGender().equals("m")){
                Drawable genderIcon =
                        new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                                .colorRes(R.color.blue).sizeDp(40);
                image.setImageDrawable(genderIcon);
            }

            else if(famPerson.getGender().equals("f")){
                Drawable genderIcon =
                        new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                                .colorRes(R.color.red).sizeDp(40);
                image.setImageDrawable(genderIcon);
            }

            TextView bottom = item.findViewById(R.id.bottom);
            bottom.setText(position);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent p = new Intent(PersonActivity.this, PersonActivity.class);
                    p.putExtra("PersonID", famPerson.getPersonID());
                    startActivity(p);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}