package com.cs240.famillymap.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.cs240.famillymap.R;
import android.os.Bundle;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}