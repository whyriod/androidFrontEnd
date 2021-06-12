package com.cs240.famillymap.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cs240.famillymap.R;
import com.cs240.famillymap.model.DataCache;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private MapFragment mapFragment;
    private DataCache cache;

    /***
     * Create The view with a login fragment.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Initalize Activity (Constructor of sorts)
        setContentView(R.layout.activity_main);//Inflate Widget Tree
        FragmentManager fm = this.getSupportFragmentManager();
        cache = DataCache.getInstance();

        loginFragment = new LoginFragment();
        fm.beginTransaction()
                .add(R.id.fragmentContainer,loginFragment)
                .commit();
    }



    /***
     * Create the menu for the Main Activity and make it invisible
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(this).inflate(R.menu.menu, menu);
        DataCache cache = DataCache.getInstance();
        cache.setMenu(menu);

        if(!cache.isLoggedIn()){
            menuDisappear();
        }
        else{
            menuAppear();
        }
        return true;
    }


    /***
     * When an option is selected, route to proper activity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                return true;
            case R.id.settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /***
     * When you come back to the Main Activity, check to see if you are logged in. If you are,
     * give them the map fragment. If not, make the menu options disappear, serve the login fragment
     * and destroy the map fragment.
     */
    @Override
    public void onResume() {
        super.onResume();
        FragmentManager fm = this.getSupportFragmentManager();
        if(!cache.isLoggedIn() && cache.getMenu() != null){
            logout();
        }
        if(cache.isFilter() && cache.isLoggedIn()){
            applyFilter();
            cache.setFilter(false);
        }
    }



    /***
     * Turns menu options on, creates a new mapFragment, and replaces the old mapFragment.
     */
    protected void login(){
        cache.setLoggedIn(true);
        menuAppear();
        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = new MapFragment();
        fm.beginTransaction().replace(R.id.fragmentContainer,mapFragment).commit();
    }


    /***
     * Turns menu options off, deletes the old mapFragment, and replaces it with a login fragment.
     */
    private void logout(){
        menuDisappear();
        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = null;
        fm.beginTransaction().replace(R.id.fragmentContainer,loginFragment).commit();
    }



    /***
     * Turns on Search and settings menu options
     */
    private void menuAppear(){
        Menu menu = cache.getMenu();
        menu.findItem(R.id.search).setVisible(true);
        menu.findItem(R.id.settings).setVisible(true);
    }



    /***
     * Turns off Search and settings menu options
     */
    private void menuDisappear(){
        Menu menu = cache.getMenu();
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
    }


    /***
     *
     */
    private void applyFilter(){
        System.out.println("New Filter");
    }
}