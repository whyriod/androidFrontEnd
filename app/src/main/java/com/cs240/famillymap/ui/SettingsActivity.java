package com.cs240.famillymap.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.cs240.famillymap.R;
import com.cs240.famillymap.model.DataCache;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    private Switch lifeStoryLines;
    private Switch familyTreeLines;
    private Switch spouseLines;
    private Switch fathersSide;
    private Switch mothersSide;
    private Switch maleEvents;
    private Switch femaleEvents;
    private Button logout;

    private DataCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lifeStoryLines = findViewById(R.id.life_story_switch);
        familyTreeLines = findViewById(R.id.family_lines_switch);
        spouseLines = findViewById(R.id.spouse_lines_switch);
        fathersSide = findViewById(R.id.fathers_side_switch);
        mothersSide = findViewById(R.id.mothers_side_switch);
        maleEvents = findViewById(R.id.male_events_switch);
        femaleEvents = findViewById(R.id.female_events_switch);
        logout = findViewById(R.id.logout);
        cache = DataCache.getInstance();
        setSwitch();

        //Setup Logout listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setLifeStoryLines(isChecked);
                cache.setFilter(true);
            }
        });

        familyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setFamilyTreeLines(isChecked);
                cache.setFilter(true);
            }
        });

        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSpouseLines(isChecked);
                cache.setFilter(true);
            }
        });

        fathersSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setFathersSide(isChecked);
                cache.setFilter(true);
            }
        });

        mothersSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setMothersSide(isChecked);
                cache.setFilter(true);
            }
        });

        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setMaleEvents(isChecked);
                cache.setFilter(true);
            }
        });

        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setFemaleEvents(isChecked);
                cache.setFilter(true);
            }
        });
    }

    private void logout(){
        cache.setLoggedIn(false);
        finish();
    }

    private void setSwitch(){
        checkLifeStoryLines();
        checkFamilyTreeLines();
        checkSpouseLines();
        checkFatherSide();
        checkMotherSide();
        checkMaleEvents();
        checkFemaleEvents();
    }

    private void checkLifeStoryLines(){
        lifeStoryLines.setChecked(cache.isLifeStoryLines());
    }

    private void checkFamilyTreeLines(){
        familyTreeLines.setChecked(cache.isFamilyTreeLines());
    }

    private void checkSpouseLines(){
        spouseLines.setChecked(cache.isSpouseLines());
    }

    private void checkFatherSide(){
        fathersSide.setChecked(cache.isFathersSide());
    }

    private void checkMotherSide(){
        mothersSide.setChecked(cache.isMothersSide());
    }

    private void checkMaleEvents(){
        maleEvents.setChecked(cache.isMaleEvents());
    }

    private void checkFemaleEvents(){
        femaleEvents.setChecked(cache.isMothersSide());
    }
}