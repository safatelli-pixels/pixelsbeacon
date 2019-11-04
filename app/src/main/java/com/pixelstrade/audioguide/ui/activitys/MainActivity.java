package com.pixelstrade.audioguide.ui.activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.ui.fragments.AboutFragment;
import com.pixelstrade.audioguide.ui.fragments.MuseumFragment;
import com.pixelstrade.audioguide.ui.fragments.ScanFragment;
import com.pixelstrade.audioguide.ui.fragments.SettingsFragment;
import com.pixelstrade.audioguide.ui.fragments.VisitFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Runnable runnable;
    private Fragment fragment = null;
    private DrawerLayout drawer;

    @BindView(R.id.titleToolbar)
    TextView titleToolbar;

    private NavigationView navigationView;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);


            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                //create new thread for change fragment when drawer change state
                if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                    runnable.run();
                    runnable = null;
                }
            }

        };

        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
            }
        });
        //set your own
        toggle.syncState();


        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpFirstFragment();
    }

    private void setUpFirstFragment() {

        //set chacked item
        navigationView.setCheckedItem(R.id.nav_museum);
        titleToolbar.setText(getString(R.string.la_mus_e));
        fragment = new MuseumFragment();
        displayFragment();
        runnable.run();
        runnable =null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       // if (id == R.id.nav_visite) {

            //set title toolbar
          /*  titleToolbar.setText(R.string.ma_visite);
            fragment = new VisitFragment();
            displayFragment();*/

      //  } else */

            if (id == R.id.nav_scan) {

            //set title toolbar
            titleToolbar.setText(R.string.scan);
            fragment = new ScanFragment();
            displayFragment();

        } else if (id == R.id.nav_museum) {

            //set title toolbar
            titleToolbar.setText(R.string.la_mus_e);
            fragment = new MuseumFragment();
            displayFragment();

        } else if (id == R.id.nav_settings) {


            //set title toolbar
            titleToolbar.setText(R.string.param_tres);
            fragment = new SettingsFragment();
            displayFragment();

        } else if (id == R.id.nav_about) {

            //set title toolbar
            titleToolbar.setText(R.string.a_propos);
            fragment = new AboutFragment();
            displayFragment();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayFragment()
    {
        if (fragment != null) {
            //change fragment in another thread for not bloc UI
            runnable = new Runnable() {
                @Override
                public void run() {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };

        }
        //close drawer after selected item in navigation view
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
