package com.cs240.famillymap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs240.famillymap.R;
import com.cs240.famillymap.model.DataCache;
import com.cs240.famillymap.model.Helper;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

/***
 * Sets up the Search view.
 */
public class SearchActivity extends AppCompatActivity {

    private static final int PEOPLE_POSITION = 0;
    private static final int EVENT_POSITION = 1;
    RecyclerView recyclerView;
    private SearchView searchView;
    private DataCache cache;
    private ArrayList<Event> events;
    private ArrayList<Person> relatives;
    private Helper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        events = new ArrayList<>();
        relatives = new ArrayList<>();
        cache = DataCache.getInstance();
        helper = new Helper();

        searchView = this.findViewById(R.id.search_bar);
        recyclerView = this.findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        RecycleAdapter adapter = new RecycleAdapter(relatives,events);
        recyclerView.setAdapter(adapter);

        //Basically, if there is a new search, make a new adapter with the filtered events.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RecycleAdapter adapter = new RecycleAdapter(relatives,events);

                helper.filter(query,events,relatives);
                recyclerView.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



    /***
     * Class example was a life saver. This works, but Im not going to comment it.
     */
    private class RecycleAdapter extends RecyclerView.Adapter<SearchItemHolder> {

        private final List<Person> relatives;
        private final List<Event> events;

        RecycleAdapter(ArrayList<Person> relatives, ArrayList<Event> events) {
            this.relatives = relatives;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < relatives.size() ? PEOPLE_POSITION : EVENT_POSITION;
        }

        @NonNull
        @Override
        public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PEOPLE_POSITION) {
                view = getLayoutInflater().inflate(R.layout.item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.item, parent, false);
            }

            return new SearchItemHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchItemHolder holder, int position) {
            if(position < relatives.size()) {
                holder.bind(relatives.get(position));
            } else {
                holder.bind(events.get(position - relatives.size()));
            }
        }

        @Override
        public int getItemCount() {
            return relatives.size() + events.size();
        }


    }




    private class SearchItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView top;
        private final TextView bottom;
        private final ImageView image;

        private final int viewType;
        private Person person;
        private Event event;

        SearchItemHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            top = itemView.findViewById(R.id.top);
            bottom = itemView.findViewById(R.id.bottom);
            image = itemView.findViewById(R.id.item_image);
        }

        private void bind(Person person) {
            this.person = person;
            top.setText(person.getFirstName() + " " + person.getLastName());

            if(person.getGender().equals("m")){
                Drawable genderIcon =
                        new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male)
                                .colorRes(R.color.blue).sizeDp(40);
                image.setImageDrawable(genderIcon);
            }
            else if(person.getGender().equals("f")){
                Drawable genderIcon =
                        new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female)
                                .colorRes(R.color.red).sizeDp(40);
                image.setImageDrawable(genderIcon);
            }
        }

        private void bind(Event event) {
            this.event = event;

            String title = event.getEventType() + ": " +
                    event.getCity() + ", " +
                    event.getCountry() + " (" +
                    event.getYear() + ")";

            Person person = cache.getPeopleMap().get(event.getPersonID());

            top.setText(title);
            bottom.setText(person.getFirstName() + " " + person.getLastName());
        }

        @Override
        public void onClick(View view) {
            if(viewType == PEOPLE_POSITION) {
                Intent p = new Intent(SearchActivity.this, PersonActivity.class);
                p.putExtra("PersonID", person.getPersonID());
                startActivity(p);
            }
            else {
                Intent e = new Intent(SearchActivity.this, EventActivity.class);
                e.putExtra("EventID",event.getEventID());
                startActivity(e);
            }
        }

    }
}